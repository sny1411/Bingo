// Adds a runClients task that starts several offline clients joining the test server:
//   ./gradlew runClients                   -> Player1
//   ./gradlew runClients -Pplayers=3       -> Player1, Player2, Player3
//   ./gradlew runClients -Pplayers=Alice,Bob

pluginManager.withPlugin("net.fabricmc.fabric-loom") {
    val runClient = tasks.named<JavaExec>("runClient")

    tasks.register<RunClients>("runClients") {
        group = "application"
        description = "Starts offline clients that join the test server (-Pplayers=3 or -Pplayers=Alice,Bob)."

        players = providers.gradleProperty("players").orElse("1").map { spec ->
            spec.toIntOrNull()?.let { count -> (1..count).map { "Player$it" } }
                ?: spec.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        }
        server = providers.gradleProperty("testServerPort").map { "localhost:$it" }
        clientsDirectory = rootProject.layout.projectDirectory.dir("run/clients")

        // Same launch setup as Loom's runClient. Its values are read with providers.provider, because
        // runClient.map would make this task depend on runClient and start one more client first.
        dependsOn(providers.provider { runClient.get().let { it.taskDependencies.getDependencies(it) } })
        javaLauncher = providers.provider { runClient.get().javaLauncher.get() }
        mainClass = providers.provider { runClient.get().mainClass.get() }
        jvmArgs = providers.provider { runClient.get().allJvmArgs }
        classpath.from(providers.provider { runClient.get().classpath })
    }
}
