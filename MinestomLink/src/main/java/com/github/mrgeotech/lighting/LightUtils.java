package com.github.mrgeotech.lighting;

import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.Section;

import java.util.*;

/*
 * Copyright Waterdev 2022, under the MIT License
 * Edits made by MrGeoTech
 */

public class LightUtils {

    private static final SectionUtils utils = new SectionUtils();

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
                            setBlockLight(utils.getCoordIndex(x,y,z), 12);
                            setSkyLight(utils.getCoordIndex(x, y, z), 15);
                        }
                    }
                }
                section.setSkyLight(skyLightArray);
                section.setBlockLight(blockLightArray);
            }
            chunk.setBlock(1,1,1,chunk.getBlock(1,1,1));
        }));
    }

    // operation type: updating
    public static void setBlockLight(final int index, final int value) {
        final int shift = (index & 1) << 2;
        final int i = index >>> 1;

        LightUtils.blockLightArray[i] = (byte)((LightUtils.blockLightArray[i] & (0xF0 >>> shift)) | (value << shift));
    }

    public static void setSkyLight(final int index, final int value) {
        final int shift = (index & 1) << 2;
        final int i = index >>> 1;

        LightUtils.skyLightArray[i] = (byte)((LightUtils.skyLightArray[i] & (0xF0 >>> shift)) | (value << shift));
    }

}