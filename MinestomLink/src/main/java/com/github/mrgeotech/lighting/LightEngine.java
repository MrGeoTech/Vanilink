package com.github.mrgeotech.lighting;

import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.Section;

import java.util.*;

/*
 * Copyright Waterdev 2022, under the MIT License
 * Edits made by MrGeoTech
 */

public class LightEngine {

    private static final SectionUtils utils = new SectionUtils();

    private static final byte fullbright = 15; // 14
    private static final byte half = 10; // 10
    private static final byte dark = 7; // 7

    //https://github.com/PaperMC/Starlight/blob/6503621c6fe1b798328a69f1bca784c6f3ffcee3/src/main/java/ca/spottedleaf/starlight/common/light/SWMRNibbleArray.java#L25
    public static final int ARRAY_SIZE = 16 * 16 * 16 / (8/4); // blocks / bytes per block

    static byte[] blockLightArray;
    static byte[] skyLightArray;
    public static void recalculateInstance(Instance instance) {
        List<Chunk> chunks = instance.getChunks().stream().toList();
        chunks.forEach((chunk -> {
            boolean[][] exposed = new boolean[16][16];
            for (boolean[] booleans : exposed) {
                Arrays.fill(booleans, true);
            }
            List<Section> sections = new ArrayList<>(chunk.getSections());
            Collections.reverse(sections);
            for (Section section : sections) {
                blockLightArray = new byte[ARRAY_SIZE];
                skyLightArray = new byte[ARRAY_SIZE];
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 15; y > -1; y--) {
                            /* s if(!utils.lightCanPassThrough(Objects.requireNonNull(Block.fromStateId((short) section.blockPalette().get(x, y, z))))) exposed[x][z] = false;
                            if(exposed[x][z]) {
                                set(utils.getCoordIndex(x,y,z), fullbright);
                            } else {
                                set(utils.getCoordIndex(x,y,z), dark);
                            }*/
                            setBlockLight(utils.getCoordIndex(x,y,z), 12);
                            setSkyLight(utils.getCoordIndex(x, y, z), 15);
                        }
                    }
                }
                section.setSkyLight(skyLightArray);
                section.setBlockLight(blockLightArray);
            }
            chunk.setBlock(1,1,1,chunk.getBlock(1,1,1));
            /* h if(chunk instanceof DynamicChunk) {
                DynamicChunk dynamicChunk = (DynamicChunk) chunk;
                try {
                    Field light = dynamicChunk.getClass().getDeclaredField("blockCache");
                    light.setAccessible(true);
                    CachedPacket cachedLight = (CachedPacket) light.get(chunk);
                    cachedLight.invalidate();
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("not dynamic chunk");
            }*/
        }));
    }

    // operation type: updating
    public static void set(final int x, final int y, final int z, final int value) {
        LightEngine.setBlockLight((x & 15) | ((z & 15) << 4) | ((y & 15) << 8), value);
    }

    //https://github.com/PaperMC/Starlight/blob/6503621c6fe1b798328a69f1bca784c6f3ffcee3/src/main/java/ca/spottedleaf/starlight/common/light/SWMRNibbleArray.java#L410
    // operation type: updating
    public static void setBlockLight(final int index, final int value) {
        final int shift = (index & 1) << 2;
        final int i = index >>> 1;

        LightEngine.blockLightArray[i] = (byte)((LightEngine.blockLightArray[i] & (0xF0 >>> shift)) | (value << shift));
    }

    public static void setSkyLight(final int index, final int value) {
        final int shift = (index & 1) << 2;
        final int i = index >>> 1;

        LightEngine.skyLightArray[i] = (byte)((LightEngine.skyLightArray[i] & (0xF0 >>> shift)) | (value << shift));
    }

}