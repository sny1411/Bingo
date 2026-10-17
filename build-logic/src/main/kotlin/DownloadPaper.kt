import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.security.DigestInputStream
import java.security.MessageDigest
import java.util.HexFormat

/**
 * Downloads a Paper server build from the PaperMC API and checks its SHA-256.
 */
@DisableCachingByDefault(because = "Downloads a file, faster than caching it")
abstract class DownloadPaper : DefaultTask() {

    @get:Input
    abstract val minecraftVersion: Property<String>

    @get:Input
    abstract val build: Property<String>

    @get:OutputFile
    abstract val serverJar: RegularFileProperty

    @TaskAction
    fun download() {
        val client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build()
        val buildUrl = "https://fill.papermc.io/v3/projects/paper/versions/${minecraftVersion.get()}/builds/${build.get()}"
        val response = client.send(request(buildUrl), HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 200) {
            throw GradleException("Paper ${minecraftVersion.get()} build ${build.get()} not found ($buildUrl: ${response.statusCode()})")
        }
        val download = JsonSlurper().parseText(response.body()).let { it as Map<*, *> }
            .let { it["downloads"] as Map<*, *> }
            .let { it["server:default"] as Map<*, *> }
        val expectedSha256 = (download["checksums"] as Map<*, *>)["sha256"] as String

        val target = serverJar.get().asFile.toPath()
        val temporary = Files.createTempFile(target.parent, "paper", ".jar")
        try {
            val digest = MessageDigest.getInstance("SHA-256")
            client.send(request(download["url"] as String), HttpResponse.BodyHandlers.ofInputStream()).body().use { body ->
                DigestInputStream(body, digest).use { Files.copy(it, temporary, StandardCopyOption.REPLACE_EXISTING) }
            }
            val sha256 = HexFormat.of().formatHex(digest.digest())
            if (sha256 != expectedSha256) {
                throw GradleException("Wrong SHA-256 for ${download["name"]}: $sha256 instead of $expectedSha256")
            }
            Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING)
        } finally {
            Files.deleteIfExists(temporary)
        }
    }

    // PaperMC asks the API clients to say who they are
    private fun request(url: String): HttpRequest = HttpRequest.newBuilder(URI(url))
        .header("User-Agent", "Bingo-e2e-tests (https://github.com/sny1411/Bingo)")
        .build()
}
