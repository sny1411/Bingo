// Minecraft client used to test the plugin: no mod code, it only joins the test server with offline usernames.
plugins {
    // For obfuscated Minecraft versions (1.21.11 and older); newer versions use net.fabricmc.fabric-loom.
    id("net.fabricmc.fabric-loom-remap")
    id("bingo.run-clients")
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    modImplementation(libs.fabric.loader)
}

// There is no mod to package. AbstractArchiveTask also covers Loom's remap tasks, which don't extend the Jar of the Kotlin DSL.
tasks.withType<AbstractArchiveTask>().configureEach {
    enabled = false
}

loom {
    runs {
        // The test server is Paper (root runServer task), not a Fabric server.
        remove(getByName("server"))
    }
}
