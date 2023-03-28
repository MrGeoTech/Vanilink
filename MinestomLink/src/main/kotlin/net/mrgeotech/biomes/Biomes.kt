package net.mrgeotech.biomes

import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.biomes.Biome
import net.minestom.server.world.biomes.Biome.*
import net.minestom.server.world.biomes.BiomeEffects
import net.minestom.server.world.biomes.BiomeEffects.Music

val BIOMES = mutableListOf<Biome>()

fun initBiomes() {
    BIOMES.add(
        builder()
        .name(NamespaceID.from("minecraft:the_void"))
        .precipitation(Precipitation.NONE)
        .category(Category.NONE)
        .temperature(0.5f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.5f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:plains"))
        .precipitation(Precipitation.RAIN)
        .category(Category.PLAINS)
        .temperature(0.8f)
        .downfall(0.4f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.8f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:sunflower_plains"))
        .precipitation(Precipitation.RAIN)
        .category(Category.PLAINS)
        .temperature(0.8f)
        .downfall(0.4f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.8f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:snowy_plains"))
        .precipitation(Precipitation.SNOW)
        .category(Category.PLAINS)
        .temperature(0.0f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.0f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:ice_spikes"))
        .precipitation(Precipitation.SNOW)
        .category(Category.ICY)
        .temperature(0.0f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.0f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:desert"))
        .precipitation(Precipitation.NONE)
        .category(Category.DESERT)
        .temperature(2.0f)
        .downfall(0.0f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(2.0f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:swamp"))
        .precipitation(Precipitation.RAIN)
        .category(Category.SWAMP)
        .temperature(0.8f)
        .downfall(0.9f)
        .effects(BiomeEffects.builder()
            .waterColor(6388580)
            .waterFogColor(2302743)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.8f))
            .foliageColor(6975545)
            .grassColorModifier(BiomeEffects.GrassColorModifier.SWAMP)
            .moodSound(AMBIENT_CAVE_MOOD)
            .music(SWAMP_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:mangrove_swamp"))
        .precipitation(Precipitation.RAIN)
        .category(Category.SWAMP)
        .temperature(0.8f)
        .downfall(0.9f)
        .effects(BiomeEffects.builder()
            .waterColor(3832426)
            .waterFogColor(5077600)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.8f))
            .foliageColor(6975545)
            .grassColorModifier(BiomeEffects.GrassColorModifier.SWAMP)
            .moodSound(AMBIENT_CAVE_MOOD)
            .music(SWAMP_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:forest"))
        .precipitation(Precipitation.RAIN)
        .category(Category.FOREST)
        .temperature(0.7f)
        .downfall(0.8f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.7f))
            .music(JUNGLE_AND_FOREST_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:flower_forest"))
        .precipitation(Precipitation.RAIN)
        .category(Category.FOREST)
        .temperature(0.7f)
        .downfall(0.8f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.7f))
            .music(JUNGLE_AND_FOREST_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:birch_forest"))
        .precipitation(Precipitation.RAIN)
        .category(Category.FOREST)
        .temperature(0.6f)
        .downfall(0.6f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.6f))
            .music(JUNGLE_AND_FOREST_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:dark_forest"))
        .precipitation(Precipitation.RAIN)
        .category(Category.FOREST)
        .temperature(0.7f)
        .downfall(0.8f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.7f))
            .grassColorModifier(BiomeEffects.GrassColorModifier.DARK_FOREST)
            .moodSound(AMBIENT_CAVE_MOOD)
            .music(JUNGLE_AND_FOREST_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:old_growth_birch_forest"))
        .precipitation(Precipitation.RAIN)
        .category(Category.FOREST)
        .temperature(0.6f)
        .downfall(0.6f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.6f))
            .music(JUNGLE_AND_FOREST_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:old_growth_pine_forest"))
        .precipitation(Precipitation.RAIN)
        .category(Category.FOREST)
        .temperature(0.3f)
        .downfall(0.8f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.3f))
            .music(OLD_GROWTH_TAIGA_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:old_growth_spruce_taiga"))
        .precipitation(Precipitation.RAIN)
        .category(Category.TAIGA)
        .temperature(0.25f)
        .downfall(0.8f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.25f))
            .music(OLD_GROWTH_TAIGA_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:taiga"))
        .precipitation(Precipitation.RAIN)
        .category(Category.TAIGA)
        .temperature(0.25f)
        .downfall(0.8f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.25f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:snowy_taiga"))
        .precipitation(Precipitation.SNOW)
        .category(Category.TAIGA)
        .temperature(-0.5f)
        .downfall(0.4f)
        .effects(BiomeEffects.builder()
            .waterColor(4020182)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(-0.5f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:savanna"))
        .precipitation(Precipitation.NONE)
        .category(Category.SAVANNA)
        .temperature(2.0f)
        .downfall(0.0f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(2.0f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:savanna_plateau"))
        .precipitation(Precipitation.NONE)
        .category(Category.SAVANNA)
        .temperature(2.0f)
        .downfall(0.0f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(2.0f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:windswept_hills"))
        .precipitation(Precipitation.RAIN)
        .category(Category.EXTREME_HILLS)
        .temperature(0.2f)
        .downfall(0.3f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.2f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:windswept_gravelly_hills"))
        .precipitation(Precipitation.RAIN)
        .category(Category.EXTREME_HILLS)
        .temperature(0.2f)
        .downfall(0.3f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.2f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:windswept_forest"))
        .precipitation(Precipitation.RAIN)
        .category(Category.EXTREME_HILLS)
        .temperature(0.2f)
        .downfall(0.3f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.2f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:windswept_savanna"))
        .precipitation(Precipitation.NONE)
        .category(Category.SAVANNA)
        .temperature(2.0f)
        .downfall(0.0f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(2.0f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:jungle"))
        .precipitation(Precipitation.RAIN)
        .category(Category.JUNGLE)
        .temperature(0.95f)
        .downfall(0.9f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.95f))
            .music(JUNGLE_AND_FOREST_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:sparse_jungle"))
        .precipitation(Precipitation.RAIN)
        .category(Category.JUNGLE)
        .temperature(0.95f)
        .downfall(0.8f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.95f))
            .music(JUNGLE_AND_FOREST_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:bamboo_jungle"))
        .precipitation(Precipitation.RAIN)
        .category(Category.JUNGLE)
        .temperature(0.95f)
        .downfall(0.9f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.95f))
            .music(JUNGLE_AND_FOREST_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:badlands"))
        .precipitation(Precipitation.NONE)
        .category(Category.MESA)
        .temperature(2.0f)
        .downfall(0.0f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(2.0f))
            .foliageColor(10387789)
            .grassColor(9470285)
            .moodSound(AMBIENT_CAVE_MOOD)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:eroded_badlands"))
        .precipitation(Precipitation.NONE)
        .category(Category.MESA)
        .temperature(2.0f)
        .downfall(0.0f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(2.0f))
            .foliageColor(10387789)
            .grassColor(9470285)
            .moodSound(AMBIENT_CAVE_MOOD)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:wooded_badlands"))
        .precipitation(Precipitation.NONE)
        .category(Category.MESA)
        .temperature(2.0f)
        .downfall(0.0f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(2.0f))
            .foliageColor(10387789)
            .grassColor(9470285)
            .moodSound(AMBIENT_CAVE_MOOD)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:meadow"))
        .precipitation(Precipitation.RAIN)
        .category(Category.PLAINS)
        .temperature(0.5f)
        .downfall(0.8f)
        .effects(BiomeEffects.builder()
            .waterColor(937679)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.5f))
            .music(MEADOW_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:grove"))
        .precipitation(Precipitation.SNOW)
        .category(Category.TAIGA)
        .temperature(-0.2f)
        .downfall(0.8f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.5f))
            .music(GROVE_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:snowy_slopes"))
        .precipitation(Precipitation.SNOW)
        .category(Category.ICY)
        .temperature(-0.3f)
        .downfall(0.9f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(-0.3f))
            .music(SNOWY_SLOPES_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:frozen_peaks"))
        .precipitation(Precipitation.SNOW)
        .category(Category.ICY)
        .temperature(-0.7f)
        .downfall(0.9f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(-0.7f))
            .music(FROZEN_PEAKS_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:jagged_peaks"))
        .precipitation(Precipitation.SNOW)
        .category(Category.ICY)
        .temperature(-0.7f)
        .downfall(0.9f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(-0.7f))
            .music(JAGGED_PEAKS_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:stony_peaks"))
        .precipitation(Precipitation.RAIN)
        .category(Category.MOUNTAIN)
        .temperature(1.0f)
        .downfall(0.3f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(-0.7f))
            .music(STONY_PEAKS_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:river"))
        .precipitation(Precipitation.RAIN)
        .category(Category.RIVER)
        .temperature(0.5f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.5f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:frozen_river"))
        .precipitation(Precipitation.SNOW)
        .category(Category.RIVER)
        .temperature(0.0f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(3750089)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.5f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:beach"))
        .precipitation(Precipitation.RAIN)
        .category(Category.BEACH)
        .temperature(0.8f)
        .downfall(0.3f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.8f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:snowy_beach"))
        .precipitation(Precipitation.SNOW)
        .category(Category.BEACH)
        .temperature(0.05f)
        .downfall(0.4f)
        .effects(BiomeEffects.builder()
            .waterColor(4020182)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.05f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:stony_beach"))
        .precipitation(Precipitation.RAIN)
        .category(Category.BEACH)
        .temperature(0.2f)
        .downfall(0.4f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.2f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:stony_shore"))
        .precipitation(Precipitation.RAIN)
        .category(Category.BEACH)
        .temperature(0.2f)
        .downfall(0.4f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.2f))
            .build()
        ).build()
    )
    // OCEAN BIOMES
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:warm_ocean"))
        .precipitation(Precipitation.RAIN)
        .category(Category.OCEAN)
        .temperature(0.5f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(4445678)
            .waterFogColor(270131)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.5f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:lukewarm_ocean"))
        .precipitation(Precipitation.RAIN)
        .category(Category.OCEAN)
        .temperature(0.5f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(4566514)
            .waterFogColor(267827)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.5f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:deep_lukewarm_ocean"))
        .precipitation(Precipitation.RAIN)
        .category(Category.OCEAN)
        .temperature(0.5f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(4566514)
            .waterFogColor(267827)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.5f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:ocean"))
        .precipitation(Precipitation.RAIN)
        .category(Category.OCEAN)
        .temperature(0.5f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.5f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:deep_ocean"))
        .precipitation(Precipitation.RAIN)
        .category(Category.OCEAN)
        .temperature(0.5f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.5f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:cold_ocean"))
        .precipitation(Precipitation.RAIN)
        .category(Category.OCEAN)
        .temperature(0.5f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(4020182)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.5f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:deep_cold_ocean"))
        .precipitation(Precipitation.RAIN)
        .category(Category.OCEAN)
        .temperature(0.5f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(4020182)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.5f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:frozen_ocean"))
        .precipitation(Precipitation.SNOW)
        .category(Category.OCEAN)
        .temperature(0.0f)
        .temperatureModifier(TemperatureModifier.FROZEN)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(3750089)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.0f))
            .moodSound(AMBIENT_CAVE_MOOD)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:deep_frozen_ocean"))
        .precipitation(Precipitation.RAIN)
        .category(Category.OCEAN)
        .temperature(0.5f)
        .temperatureModifier(TemperatureModifier.FROZEN)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(3750089)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.5f))
            .moodSound(AMBIENT_CAVE_MOOD)
            .build()
        ).build()
    )
    // OTHER BIOMES
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:mushroom_fields"))
        .precipitation(Precipitation.RAIN)
        .category(Category.MUSHROOM)
        .temperature(0.9f)
        .downfall(1.0f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.9f))
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:dripstone_caves"))
        .precipitation(Precipitation.RAIN)
        .category(Category.UNDERGROUND)
        .temperature(0.8f)
        .downfall(0.4f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.8f))
            .music(DRIPSTONE_CAVES_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:lush_caves"))
        .precipitation(Precipitation.RAIN)
        .category(Category.UNDERGROUND)
        .temperature(0.5f)
        .downfall(0.4f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.5f))
            .music(LUSH_CAVES_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:deep_dark"))
        .precipitation(Precipitation.RAIN)
        .category(Category.UNDERGROUND)
        .temperature(0.8f)
        .downfall(0.4f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(OVERWORLD_FOG_COLOR)
            .skyColor(SKY_COLOR(0.8f))
            .music(DEEP_DARK_MUSIC)
            .build()
        ).build()
    )
    // NETHER BIOMES
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:nether_wastes"))
        .precipitation(Precipitation.NONE)
        .category(Category.NETHER)
        .temperature(2.0f)
        .downfall(0.0f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(3344392)
            .skyColor(SKY_COLOR(2.0f))
            .ambientSound(NamespaceID.from("minecraft:ambient.nether_wastes.loop"))
            .moodSound(AMBIENT_NETHER_WASTES_MOOD)
            .additionsSound(AMBIENT_NETHER_WASTES_ADDITIONS)
            .music(NETHER_WASTES_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:warped_forest"))
        .precipitation(Precipitation.NONE)
        .category(Category.NETHER)
        .temperature(2.0f)
        .downfall(0.0f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(1705242)
            .skyColor(SKY_COLOR(2.0f))
            .ambientSound(NamespaceID.from("minecraft:ambient.warped_forest.loop"))
            .moodSound(AMBIENT_WARPED_FOREST_MOOD)
            .additionsSound(AMBIENT_WARPED_FOREST_ADDITIONS)
            .music(WARPED_FOREST_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:crimson_forest"))
        .precipitation(Precipitation.NONE)
        .category(Category.NETHER)
        .temperature(2.0f)
        .downfall(0.0f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(3343107)
            .skyColor(SKY_COLOR(2.0f))
            .ambientSound(NamespaceID.from("minecraft:ambient.crimson_forest.loop"))
            .moodSound(AMBIENT_CRIMSON_FOREST_MOOD)
            .additionsSound(AMBIENT_CRIMSON_FOREST_ADDITIONS)
            .music(CRIMSON_FOREST_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:soul_sand_valley"))
        .precipitation(Precipitation.NONE)
        .category(Category.NETHER)
        .temperature(2.0f)
        .downfall(0.0f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(1787717)
            .skyColor(SKY_COLOR(2.0f))
            .ambientSound(NamespaceID.from("minecraft:ambient.soul_sand_valley.loop"))
            .moodSound(AMBIENT_SOUL_SAND_VALLEY_MOOD)
            .additionsSound(AMBIENT_SOUL_SAND_VALLEY_ADDITIONS)
            .music(SOUL_SAND_VALLEY_MUSIC)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:basalt_deltas"))
        .precipitation(Precipitation.NONE)
        .category(Category.NETHER)
        .temperature(2.0f)
        .downfall(0.0f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(6840176)
            .skyColor(SKY_COLOR(2.0f))
            .ambientSound(NamespaceID.from("minecraft:ambient.basalt_deltas.loop"))
            .moodSound(AMBIENT_BASALT_DELTAS_MOOD)
            .additionsSound(AMBIENT_BASALT_DELTAS_ADDITIONS)
            .music(BASALT_DELTAS_MUSIC)
            .build()
        ).build()
    )
    // END BIOMES
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:the_end"))
        .precipitation(Precipitation.NONE)
        .category(Category.THE_END)
        .temperature(0.5f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(10518688)
            .skyColor(0)
            .moodSound(AMBIENT_CAVE_MOOD)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:end_highlands"))
        .precipitation(Precipitation.NONE)
        .category(Category.THE_END)
        .temperature(0.5f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(10518688)
            .skyColor(0)
            .moodSound(AMBIENT_CAVE_MOOD)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:end_midlands"))
        .precipitation(Precipitation.NONE)
        .category(Category.THE_END)
        .temperature(0.5f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(10518688)
            .skyColor(0)
            .moodSound(AMBIENT_CAVE_MOOD)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:small_end_islands"))
        .precipitation(Precipitation.NONE)
        .category(Category.THE_END)
        .temperature(0.5f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(10518688)
            .skyColor(0)
            .moodSound(AMBIENT_CAVE_MOOD)
            .build()
        ).build()
    )
    BIOMES.add(builder()
        .name(NamespaceID.from("minecraft:end_barrens"))
        .precipitation(Precipitation.NONE)
        .category(Category.THE_END)
        .temperature(0.5f)
        .downfall(0.5f)
        .effects(BiomeEffects.builder()
            .waterColor(NORMAL_WATER_COLOR)
            .waterFogColor(NORMAL_WATER_FOG_COLOR)
            .fogColor(10518688)
            .skyColor(0)
            .moodSound(AMBIENT_CAVE_MOOD)
            .build()
        ).build()
    )
}
// Mood sounds
val AMBIENT_CAVE_MOOD = BiomeEffects.MoodSound(
    NamespaceID.from("minecraft:ambient.cave"),
    6000,
    8,
    2.0
)
val AMBIENT_NETHER_WASTES_MOOD = BiomeEffects.MoodSound(
    NamespaceID.from("minecraft:ambient.nether_wastes.mood"),
    6000,
    8,
    2.0
)
val AMBIENT_WARPED_FOREST_MOOD = BiomeEffects.MoodSound(
    NamespaceID.from("minecraft:ambient.warped_forest.mood"),
    6000,
    8,
    2.0
)
val AMBIENT_CRIMSON_FOREST_MOOD = BiomeEffects.MoodSound(
    NamespaceID.from("minecraft:ambient.crimson_forest.mood"),
    6000,
    8,
    2.0
)
val AMBIENT_SOUL_SAND_VALLEY_MOOD = BiomeEffects.MoodSound(
    NamespaceID.from("minecraft:ambient.soul_sand_valley.mood"),
    6000,
    8,
    2.0
)
val AMBIENT_BASALT_DELTAS_MOOD = BiomeEffects.MoodSound(
    NamespaceID.from("minecraft:ambient.basalt_deltas.mood"),
    6000,
    8,
    2.0
)

// Additions Sounds
val AMBIENT_NETHER_WASTES_ADDITIONS = BiomeEffects.AdditionsSound(
    NamespaceID.from("minecraft:ambient.nether_wastes.additions"),
    0.0111
)
val AMBIENT_WARPED_FOREST_ADDITIONS = BiomeEffects.AdditionsSound(
    NamespaceID.from("minecraft:ambient.warped_forest.additions"),
    0.0111
)
val AMBIENT_CRIMSON_FOREST_ADDITIONS = BiomeEffects.AdditionsSound(
    NamespaceID.from("minecraft:ambient.crimson_forest.additions"),
    0.0111
)
val AMBIENT_SOUL_SAND_VALLEY_ADDITIONS = BiomeEffects.AdditionsSound(
    NamespaceID.from("minecraft:ambient.soul_sand_valley.additions"),
    0.0111
)
val AMBIENT_BASALT_DELTAS_ADDITIONS = BiomeEffects.AdditionsSound(
    NamespaceID.from("minecraft:ambient.basalt_deltas.additions"),
    0.0111
)

// Music
val SWAMP_MUSIC = Music(
    NamespaceID.from("minecraft:music.overworld.swamp"),
    12000,
    24000,
    true
)
val JUNGLE_AND_FOREST_MUSIC = Music(
    NamespaceID.from("minecraft:music.overworld.jungle_and_forest"),
    6000,
    24000,
    true
)
val OLD_GROWTH_TAIGA_MUSIC = Music(
    NamespaceID.from("minecraft:music.overworld.old_growth_taiga"),
    6000,
    24000,
    true
)
val MEADOW_MUSIC = Music(
    NamespaceID.from("minecraft:music.overworld.meadow"),
    12000,
    24000,
    true
)
val GROVE_MUSIC = Music(
    NamespaceID.from("minecraft:music.overworld.grove"),
    12000,
    24000,
    true
)
val SNOWY_SLOPES_MUSIC = Music(
    NamespaceID.from("minecraft:music.overworld.snowy_slopes"),
    12000,
    24000,
    true
)
val FROZEN_PEAKS_MUSIC = Music(
    NamespaceID.from("minecraft:music.overworld.frozen_peaks"),
    12000,
    24000,
    true
)
val JAGGED_PEAKS_MUSIC = Music(
    NamespaceID.from("minecraft:music.overworld.jagged_peaks"),
    12000,
    24000,
    true
)
val STONY_PEAKS_MUSIC = Music(
    NamespaceID.from("minecraft:music.overworld.stony_peaks"),
    12000,
    24000,
    true
)
val DRIPSTONE_CAVES_MUSIC = Music(
    NamespaceID.from("minecraft:music.overworld.dripstone_caves"),
    12000,
    24000,
    true
)
val LUSH_CAVES_MUSIC = Music(
    NamespaceID.from("minecraft:music.overworld.lush_caves"),
    12000,
    24000,
    true
)
val DEEP_DARK_MUSIC = Music(
    NamespaceID.from("minecraft:music.overworld.deep_dark"),
    12000,
    24000,
    true
)
val NETHER_WASTES_MUSIC = Music(
    NamespaceID.from("minecraft:music.nether.nether_wastes"),
    12000,
    24000,
    true
)
val WARPED_FOREST_MUSIC = Music(
    NamespaceID.from("minecraft:music.nether.warped_forest"),
    12000,
    24000,
    true
)
val CRIMSON_FOREST_MUSIC = Music(
    NamespaceID.from("minecraft:music.nether.crimson_forest"),
    12000,
    24000,
    true
)
val SOUL_SAND_VALLEY_MUSIC = Music(
    NamespaceID.from("minecraft:music.nether.soul_sand_valley"),
    12000,
    24000,
    true
)
val BASALT_DELTAS_MUSIC = Music(
    NamespaceID.from("minecraft:music.nether.basalt_deltas"),
    12000,
    24000,
    true
)

const val NORMAL_WATER_COLOR = 4159204
const val NORMAL_WATER_FOG_COLOR = 329011
const val OVERWORLD_FOG_COLOR = 12638463
val SKY_COLOR = { temperature: Float -> // Formula from PaperMC sources
    var f = temperature / 3.0f
    f = clamp(f, -1.0f, 1.0f)
    hsvToRgb(0.62222224f - f * 0.05f, 0.5f + f * 0.1f, 1.0f)
}

fun hsvToRgb(hue: Float, saturation: Float, value: Float): Int {
    val h: Int = (hue * 6.0f).toInt()
    val f: Float = hue * 6.0f - h
    val p: Float = value * (1.0f - saturation)
    val q: Float = value * (1.0f - f * saturation)
    val t: Float = value * (1.0f - (1.0f - f) * saturation)

    return when (h) {
        0 -> toRGBInt(value, t, p)
        1 -> toRGBInt(q, value, p)
        2 -> toRGBInt(p, value, t)
        3 -> toRGBInt(p, q, value)
        4 -> toRGBInt(t, p, value)
        5 -> toRGBInt(value, p, q)
        else -> throw IllegalArgumentException("Something went wrong when converting from HSV to RGB. Input was $hue, $saturation, $value")
    }
}

fun toRGBInt(r: Float, g: Float, b: Float): Int {
    val clampedR = clamp((r * 255.0f).toInt(), 0, 255)
    val clampedG = clamp((g * 255.0f).toInt(), 0, 255)
    val clampedB = clamp((b * 255.0f).toInt(), 0, 255)
    return clampedR shl 16 or (clampedG shl 8) or clampedB
}

fun clamp(value: Int, min: Int, max: Int): Int {
    return if (value < min) min
    else if (value > max) max
    else value
}

fun clamp(value: Float, min: Float, max: Float): Float {
    return if (value < min) min
    else if (value > max) max
    else value
}