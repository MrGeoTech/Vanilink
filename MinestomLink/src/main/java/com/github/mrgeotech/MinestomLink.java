package com.github.mrgeotech;

import com.github.mrgeotech.lighting.LightEngine;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.world.DimensionType;

import java.io.IOException;

public class MinestomLink extends Extension {

    private InstanceContainer container;

    @Override
    public void initialize() {
        System.out.println("Extension loading...");
        try {
            ConfigHandler.init();
        } catch (IOException e) {
            e.printStackTrace();
            MinecraftServer.stopCleanly();
        }
        container = MinecraftServer.getInstanceManager().createInstanceContainer(DimensionType.OVERWORLD);
        container.setChunkGenerator(new ConnectedChunkLoader());
        MinecraftServer.getInstanceManager().registerInstance(container);
        container.loadChunk(0, 0);
        int viewDistance = Integer.getInteger("minestom.chunk-view-distance", 8);
        for (int x = -viewDistance; x <= viewDistance; x++) {
            for (int y = -viewDistance; y <= viewDistance; y++) {
                container.loadChunk(x, y);
            }
        }
        super.getEventNode().addListener(PlayerLoginEvent.class, event -> {
            Pos spawn = new Pos(0, 65, 0);
            while (!container.getBlock((int) spawn.x(), (int) spawn.y(), (int) spawn.z()).isAir()) {
                spawn = spawn.add(0,1,0);
            }
            event.setSpawningInstance(container);
            event.getPlayer().setRespawnPoint(spawn);
        });
        MinecraftServer.getSchedulerManager().scheduleTask(
                () -> LightEngine.recalculateInstance(container), TaskSchedule.tick(1), TaskSchedule.tick(1));
        System.out.println("Extension loaded!");
    }

    @Override
    public void terminate() {
        container.saveInstance();
        container.saveChunksToStorage();
    }

}
