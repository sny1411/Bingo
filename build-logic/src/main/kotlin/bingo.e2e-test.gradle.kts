// Adds the end-to-end tests: src/e2eTest, run by ./gradlew e2eTest on a real Paper server (see CONTRIBUTING).
// ./gradlew build doesn't run them.

plugins {
    java
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

val e2eTest = sourceSets.create("e2eTest")

dependencies {
    add(e2eTest.implementationConfigurationName, platform(libs.findLibrary("junit-bom").get()))
    add(e2eTest.implementationConfigurationName, libs.findLibrary("junit-jupiter").get())
    add(e2eTest.runtimeOnlyConfigurationName, libs.findLibrary("junit-platform-launcher").get())
}

val downloadPaper = tasks.register<DownloadPaper>("downloadPaper") {
    description = "Downloads the Paper server the end-to-end tests run on."
    minecraftVersion = libs.findVersion("minecraft").get().requiredVersion
    build = libs.findVersion("paper-server").get().requiredVersion
    serverJar = minecraftVersion.zip(build) { version, build -> layout.buildDirectory.file("e2e/paper-$version-$build.jar").get() }
}

tasks.register<Test>("e2eTest") {
    description = "Runs the end-to-end tests on a real Paper server with the plugin."
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    testClassesDirs = e2eTest.output.classesDirs
    classpath = e2eTest.runtimeClasspath
    useJUnitPlatform()
    shouldRunAfter(tasks.named("test"))
    // The server and the world change from one run to the next
    outputs.upToDateWhen { false }

    val paperJar = downloadPaper.flatMap { it.serverJar }
    val pluginJar = tasks.named<Jar>("jar").flatMap { it.archiveFile }
    val e2eDirectory = layout.buildDirectory.dir("e2e")
    inputs.file(paperJar)
    inputs.file(pluginJar)
    jvmArgumentProviders.add {
        listOf(
            "-Dbingo.e2e.paperJar=${paperJar.get().asFile}",
            "-Dbingo.e2e.pluginJar=${pluginJar.get().asFile}",
            "-Dbingo.e2e.directory=${e2eDirectory.get().asFile}",
        )
    }
}
