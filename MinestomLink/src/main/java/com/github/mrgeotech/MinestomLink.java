package com.github.mrgeotech;

import com.github.mrgeotech.lighting.LightEngine;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import net.minestom.server.world.biomes.Biome;
import net.minestom.server.world.biomes.BiomeEffects;
import net.minestom.server.world.biomes.BiomeManager;

import java.io.IOException;

public class MinestomLink extends Extension {

    private InstanceContainer container;

    private static final BiomeEffects DEFAULT_EFFECTS = BiomeEffects.builder()
            .fogColor(0xC0D8FF)
            .skyColor(0x78A7FF)
            .waterColor(0x3F76E4)
            .waterFogColor(0x50533)
            .build();
    /*m private static final BiomeEffects BROWN_EFFECT = BiomeEffects.builder()
            .fogColor(0xC0D8FF)
            .skyColor(0x78A7FF)
            .waterColor(0x3F76E4)
            .waterFogColor(0x50533)
            .build();
    private static final BiomeEffects OLIVE_EFFECT = BiomeEffects.builder()
            .fogColor(0xC0D8FF)
            .skyColor(0x78A7FF)
            .waterColor(0x3F76E4)
            .waterFogColor(0x50533)
            .build();
    private static final BiomeEffects LIME_EFFECT = BiomeEffects.builder()
            .fogColor(0xC0D8FF)
            .skyColor(0x78A7FF)
            .waterColor(0x3F76E4)
            .waterFogColor(0x50533)
            .build();
    private static final BiomeEffects LUSH_EFFECT = BiomeEffects.builder()
            .fogColor(0xC0D8FF)
            .skyColor(0x78A7FF)
            .waterColor(0x3F76E4)
            .waterFogColor(0x50533)
            .build();
    private static final BiomeEffects LIGHT_EFFECT = BiomeEffects.builder()
            .fogColor(0xC0D8FF)
            .skyColor(0x78A7FF)
            .waterColor(0x3F76E4)
            .waterFogColor(0x50533)
            .build();
    private static final BiomeEffects DARK_GREEN_EFFECT = BiomeEffects.builder()
            .fogColor(0xC0D8FF)
            .skyColor(0x78A7FF)
            .waterColor(0x3F76E4)
            .waterFogColor(0x50533)
            .build();
    private static final BiomeEffects GREEN_EFFECT = BiomeEffects.builder()
            .fogColor(0xC0D8FF)
            .skyColor(0x78A7FF)
            .waterColor(0x3F76E4)
            .waterFogColor(0x50533)
            .build();
    private static final BiomeEffects TURQUOISE_EFFECT = BiomeEffects.builder()
            .fogColor(0xC0D8FF)
            .skyColor(0x78A7FF)
            .waterColor(0x3F76E4)
            .waterFogColor(0x50533)
            .build();
    private static final BiomeEffects AQUAMARINE_EFFECT = BiomeEffects.builder()
            .fogColor(0xC0D8FF)
            .skyColor(0x78A7FF)
            .waterColor(0x3F76E4)
            .waterFogColor(0x50533)
            .build();
    private static final BiomeEffects DULL_AQUA_EFFECT = BiomeEffects.builder()
            .fogColor(0xC0D8FF)
            .skyColor(0x78A7FF)
            .waterColor(0x3F76E4)
            .waterFogColor(0x50533)
            .build();*/

    @Override
    public void initialize() {
        System.out.println("Extension loading...");
        try {
            ConfigHandler.init();
        } catch (IOException e) {
            e.printStackTrace();
            MinecraftServer.stopCleanly();
        }

        loadBiomes();

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

    private void loadBiomes() {
        BiomeManager manager = MinecraftServer.getBiomeManager();
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:the_void"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:sunflower_plains"))
                .temperature(0.8F)
                .downfall(0.4F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:snowy_plains"))
                .temperature(0.0F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:ice_spikes"))
                .temperature(0.0F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:desert"))
                .temperature(2.0F)
                .downfall(0.0F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:swamp"))
                .temperature(0.8F)
                .downfall(0.9F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:forest"))
                .temperature(0.7F)
                .downfall(0.8F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:flower_forest"))
                .temperature(0.7F)
                .downfall(0.8F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:birch_forest"))
                .temperature(0.6F)
                .downfall(0.6F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:dark_forest"))
                .temperature(0.7F)
                .downfall(0.8F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:old_growth_birch_forest"))
                .temperature(0.6F)
                .downfall(0.6F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:old_growth_pine_taiga"))
                .temperature(0.3F)
                .downfall(0.8F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:old_growth_spruce_taiga"))
                .temperature(0.25F)
                .downfall(0.8F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:taiga"))
                .temperature(0.25F)
                .downfall(0.8F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:savanna"))
                .temperature(1.2F)
                .downfall(0.0F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:savanna_plateau"))
                .temperature(1.0F)
                .downfall(0.0F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:windswept_hills"))
                .temperature(0.2F)
                .downfall(0.3F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:windswept_gravelly_hills"))
                .temperature(0.2F)
                .downfall(0.3F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:windswept_forest"))
                .temperature(0.2F)
                .downfall(0.3F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:windswept_savanna"))
                .temperature(1.1F)
                .downfall(0.0F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:jungle"))
                .temperature(0.95F)
                .downfall(0.9F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:sparse_jungle"))
                .temperature(0.95F)
                .downfall(0.8F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:bamboo_jungle"))
                .temperature(0.95F)
                .downfall(0.9F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:badlands"))
                .temperature(2.0F)
                .downfall(0.0F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:eroded_badlands"))
                .temperature(2.0F)
                .downfall(0.0F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:wooded_badlands"))
                .temperature(2.0F)
                .downfall(0.0F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:meadow"))
                .temperature(0.5F)
                .downfall(0.8F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:grove"))
                .temperature(-0.2F)
                .downfall(0.8F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:snowy_slopes"))
                .temperature(-0.3F)
                .downfall(0.9F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:frozen_peaks"))
                .temperature(-0.7F)
                .downfall(0.9F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:jagged_peaks"))
                .temperature(-0.7F)
                .downfall(0.9F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:stony_peaks"))
                .temperature(1.0F)
                .downfall(0.3F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:river"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:frozen_river"))
                .temperature(0.0F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:beach"))
                .temperature(0.8F)
                .downfall(0.4F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:snowy_beach"))
                .temperature(0.5F)
                .downfall(0.3F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:stony_shore"))
                .temperature(0.2F)
                .downfall(0.3F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:warm_ocean"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:lukewarm_ocean"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:deep_lukewarm_ocean"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:ocean"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:deep_ocean"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:cold_ocean"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:deep_cold_ocean"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:frozen_ocean"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:deep_frozen_ocean"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:mushroom_fields"))
                .temperature(0.9F)
                .downfall(1.0F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:dripstone_caves"))
                .temperature(0.8F)
                .downfall(0.4F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:lush_caves"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:nether_wastes"))
                .temperature(2.0F)
                .downfall(0.0F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:warped_forest"))
                .temperature(2.0F)
                .downfall(0.0F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:crimson_forest"))
                .temperature(2.0F)
                .downfall(0.0F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:soul_sand_valley"))
                .temperature(2.0F)
                .downfall(0.0F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:basalt_deltas"))
                .temperature(2.0F)
                .downfall(0.0F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:the_end"))
                .temperature(0.8F)
                .downfall(0.4F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:end_highlands"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:end_midlands"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:small_end_islands"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
        manager.addBiome(Biome.builder()
                .name(NamespaceID.from("minecraft:end_barrens"))
                .temperature(0.5F)
                .downfall(0.5F)
                .depth(0.125F)
                .scale(0.05F)
                .effects(DEFAULT_EFFECTS)
                .build());
    }

}
