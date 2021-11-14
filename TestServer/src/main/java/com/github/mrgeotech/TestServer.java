package com.github.mrgeotech;

import net.minestom.server.MinecraftServer;

public class TestServer {

    public static void main(String[] args) {
        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();

        // Start the server
        minecraftServer.start("0.0.0.0", 25565);
    }

}
