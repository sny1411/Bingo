import java.util.Properties

plugins {
    java
    alias(libs.plugins.run.paper)
}

group = "fr.sny1411"
version = "1.0"

dependencies {
    compileOnly(libs.paper.api)
}

tasks {
    withType<JavaCompile>().configureEach {
        options.release = 25
        options.encoding = "UTF-8"
    }

    processResources {
        val version = project.version.toString()
        inputs.property("version", version)
        filesMatching("plugin.yml") {
            expand("version" to version)
        }
    }

    runServer {
        minecraftVersion(libs.versions.minecraft.get())
        // Running the test server means accepting the Minecraft EULA (https://aka.ms/MinecraftEULA).
        systemProperty("com.mojang.eula.agree", "true")

        // Spigot ignores --online-mode, so the test server settings are written to server.properties.
        val serverProperties = runDirectory.file("server.properties")
        val bukkitYml = runDirectory.file("bukkit.yml")
        val paperGlobalYml = runDirectory.file("config/paper-global.yml")
        val port = providers.gradleProperty("testServerPort")
        doFirst {
            val file = serverProperties.get().asFile
            val properties = Properties()
            if (file.exists()) {
                file.reader().use(properties::load)
            }
            properties["online-mode"] = "false"
            properties["server-ip"] = "127.0.0.1"
            properties["server-port"] = port.get()
            // The server settings recommended in the README
            properties["spawn-protection"] = "0"
            properties["gamemode"] = "survival"
            properties["force-gamemode"] = "false"
            properties["generate-structures"] = "true"
            file.parentFile.mkdirs()
            file.writer().use { properties.store(it, null) }

            // These files are created on the first start, with the Nether and the End enabled
            setYamlValue(bukkitYml.get().asFile, "allow-end", "true")
            setYamlValue(paperGlobalYml.get().asFile, "enable-nether", "true")
        }
    }
}

fun setYamlValue(file: File, key: String, value: String) {
    if (file.exists()) {
        file.writeText(file.readText().replace(Regex("(?m)^(\\s*$key:).*$"), "$1 $value"))
    }
}
