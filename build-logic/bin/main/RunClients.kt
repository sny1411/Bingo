import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.TaskAction
import org.gradle.jvm.toolchain.JavaLauncher
import org.gradle.work.DisableCachingByDefault

/**
 * Starts one Minecraft client per player, all joining the test server at once.
 * Each client gets its own game directory, so settings and logs don't clash.
 */
@DisableCachingByDefault(because = "Runs Minecraft clients")
abstract class RunClients : DefaultTask() {

    @get:Input
    abstract val players: ListProperty<String>

    @get:Input
    abstract val server: Property<String>

    @get:Nested
    abstract val javaLauncher: Property<JavaLauncher>

    @get:Input
    abstract val mainClass: Property<String>

    @get:Input
    abstract val jvmArgs: ListProperty<String>

    @get:Classpath
    abstract val classpath: ConfigurableFileCollection

    @get:Internal
    abstract val clientsDirectory: DirectoryProperty

    @TaskAction
    fun run() {
        val players = players.get()
        validate(players)

        val processes = players.associateWith { start(it) }
        try {
            processes.forEach { (player, process) ->
                val exitCode = process.waitFor()
                logger.lifecycle("$player closed (exit code $exitCode)")
            }
        } finally {
            processes.values.filter { it.isAlive }.forEach { it.destroy() }
        }
    }

    private fun validate(players: List<String>) {
        if (players.isEmpty()) {
            throw GradleException("No player given, use -Pplayers=3 or -Pplayers=Alice,Bob")
        }
        players.filterNot { USERNAME.matches(it) }.takeIf { it.isNotEmpty() }?.let {
            throw GradleException("Invalid usernames $it: 3 to 16 characters, letters, digits or _")
        }
        players.groupingBy { it.lowercase() }.eachCount().filterValues { it > 1 }.keys.takeIf { it.isNotEmpty() }?.let {
            throw GradleException("Duplicate usernames: $it")
        }
    }

    private fun start(player: String): Process {
        val gameDirectory = clientsDirectory.dir(player).get().asFile
        val logs = gameDirectory.resolve("logs").apply { mkdirs() }
        val command = buildList {
            add(javaLauncher.get().executablePath.asFile.absolutePath)
            addAll(jvmArgs.get())
            add("-cp")
            add(classpath.asPath)
            add(mainClass.get())
            addAll(listOf("--username", player))
            addAll(listOf("--gameDir", gameDirectory.absolutePath))
            addAll(listOf("--quickPlayMultiplayer", server.get()))
        }
        logger.lifecycle("Starting $player (logs in $logs)")
        return ProcessBuilder(command)
            .directory(gameDirectory)
            .redirectErrorStream(true)
            .redirectOutput(logs.resolve("stdout.log"))
            .start()
    }

    private companion object {
        val USERNAME = Regex("[A-Za-z0-9_]{3,16}")
    }
}
