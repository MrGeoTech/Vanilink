package com.github.mrgeotech;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.BlockGetter;
import net.minestom.server.utils.NamespaceID;
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
        DimensionType dimension = DimensionType.builder(NamespaceID.from("minecraft:overworld"))
                .ultrawarm(false)
                .natural(true)
                .piglinSafe(false)
                .respawnAnchorSafe(false)
                .bedSafe(true)
                .raidCapable(true)
                .skylightEnabled(true)
                .ceilingEnabled(false)
                .fixedTime(null)
                .ambientLight(0.0f)
                .height(320)
                .minY(-64)
                .logicalHeight(320)
                .infiniburn(NamespaceID.from("minecraft:infiniburn_overworld"))
                .build();
        MinecraftServer.getDimensionTypeManager().addDimension(dimension);
        container = MinecraftServer.getInstanceManager().createInstanceContainer(dimension);
        container.setChunkGenerator(new ConnectedChunkLoader());
        MinecraftServer.getInstanceManager().registerInstance(container);
        container.loadChunk(0, 0);
        for (int x = -8; x <= 8; x++) {
            for (int y = -8; y <= 8; y++) {
                container.loadChunk(x, y);
            }
        }
        super.getEventNode().addListener(PlayerLoginEvent.class, event -> {
            Pos spawn = new Pos(0, 65, 0);
            while (!container.getBlock((int) spawn.x(), (int) spawn.y(), (int) spawn.z(), BlockGetter.Condition.TYPE).isAir()) {
                spawn = spawn.add(0,1,0);
            }
            event.setSpawningInstance(container);
            event.getPlayer().setRespawnPoint(spawn);
            event.getPlayer().setGameMode(GameMode.CREATIVE);
        });
        System.out.println("Extension loaded!");
    }

    @Override
    public void terminate() {
        container.saveChunksToStorage();
    }

}
