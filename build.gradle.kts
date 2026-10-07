import java.util.Properties

plugins {
    java
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
}

group = "fr.sny1411"
version = "1.0"

dependencies {
    compileOnly(libs.paper.api)
    implementation(libs.paperlib)
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

    jar {
        enabled = false
    }

    shadowJar {
        archiveClassifier = ""
        relocate("io.papermc.lib", "fr.sny1411.bingo.paperlib")
    }

    runServer {
        minecraftVersion(libs.versions.minecraft.get())
        // Running the test server means accepting the Minecraft EULA (https://aka.ms/MinecraftEULA).
        systemProperty("com.mojang.eula.agree", "true")

        // Spigot ignores --online-mode, so the test server settings are written to server.properties.
        val serverProperties = runDirectory.file("server.properties")
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
            file.parentFile.mkdirs()
            file.writer().use { properties.store(it, null) }
        }
    }
}
