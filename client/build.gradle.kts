// Minecraft client used to test the plugin: no mod code, it only joins the test server with offline usernames.
plugins {
    // For unobfuscated Minecraft versions (26.1 and newer); older versions use net.fabricmc.fabric-loom-remap.
    id("net.fabricmc.fabric-loom")
    id("bingo.run-clients")
}

dependencies {
    minecraft(libs.minecraft)
    implementation(libs.fabric.loader)
}

loom {
    runs {
        // The test server is Paper (root runServer task), not a Fabric server.
        remove(getByName("server"))
    }
}
