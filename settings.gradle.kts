pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
    }
}

plugins {
    // Declares Loom's repositories here, with the other repositories of the build.
    // It also puts Loom on the build classpath: client/ applies it without a version.
    id("net.fabricmc.fabric-loom-repositories") version "1.18.2"
    // Lets updateDaemonJvm write the JDK download URLs of gradle/gradle-daemon-jvm.properties (./gradlew updateDaemonJvm --jvm-version=25).
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "Bingo"

dependencyResolutionManagement {
    // Repositories added by plugins in projects (Loom adds one for LWJGL) are ignored: only these are used.
    repositoriesMode = RepositoriesMode.PREFER_SETTINGS
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

// Loom downloads Minecraft as soon as client/ is configured, so it's only included for runClients:
// building or running the server doesn't need the Minecraft client.
if (gradle.startParameter.taskNames.any { it.endsWith("runClients") }) {
    include("client")
}
