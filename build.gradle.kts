plugins {
    java
    alias(libs.plugins.shadow)
}

group = "fr.sny1411"
version = "1.0"

dependencies {
    compileOnly(libs.paper.api)
    implementation(libs.paperlib)
}

tasks {
    withType<JavaCompile>().configureEach {
        options.release = 17
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
}
