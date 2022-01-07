package com.github.mrgeotech;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.player.PlayerSkinInitEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.optifine.OptifineSupport;

public class TestServer {

    public static void main(String[] args) {
        System.setProperty("minestom.chunk-view-distance", String.valueOf(16));
        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();

        MinecraftServer.getCommandManager().register(new StopCommand());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSkinInitEvent.class,
                event -> event.setSkin(PlayerSkin.fromUsername(event.getPlayer().getUsername())))
                .addListener(PlayerSpawnEvent.class, event -> event.getPlayer().setGameMode(GameMode.CREATIVE));

        OptifineSupport.enable();

        MojangAuth.init();

        // Start the server
        minecraftServer.start("0.0.0.0", 25565);
    }

}
