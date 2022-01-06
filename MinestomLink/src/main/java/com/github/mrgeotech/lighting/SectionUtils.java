package com.github.mrgeotech.lighting;

/*
 * Copyright Waterdev 2022, under the MIT License
 */

import net.minestom.server.instance.block.Block;

public class SectionUtils {

    private final int dimension;

    public SectionUtils() {
        dimension = 16;
    }

    public SectionUtils(int dimension) {
        this.dimension = dimension;
    }

    /**
     * Util for light arrays
     * @param x only 0 - 16
     * @param y only 0 - 16
     * @param z only 0 - 16
     * @return index of block coordinate for light arrays
     */
    public int getCoordIndex(int x, int y, int z) {
        return y << (dimension / 2) | z << (dimension / 4) | x;
    }

    public boolean lightCanPassThrough(Block block) {
        return !block.isSolid() || block.isAir() || block.compare(Block.BARRIER);
    }

    //https://github.com/PaperMC/Starlight/blob/6503621c6fe1b798328a69f1bca784c6f3ffcee3/src/main/java/ca/spottedleaf/starlight/common/light/SWMRNibbleArray.java#L25
    public static final int ARRAY_SIZE = 16 * 16 * 16 / (8/4); // blocks / bytes per block

    int printed = 0;

    public byte[] shrink(byte[] array) {
        byte[] shrunk = new byte[ARRAY_SIZE];
        for (int i = 0; i < array.length; i+=2) {
            int j = i+1;
            byte iB = array[i];
            byte jB = array[j];
            byte merged = (byte) ((array[i] << 4) | array[j]);
            if(printed < 10) {
                System.out.println(Integer.toBinaryString(iB));
                System.out.println(Integer.toBinaryString(jB));
                System.out.println(Integer.toBinaryString(merged));
                System.out.println(i);
                System.out.println(j);
                System.out.println("---------------");
                printed++;
            }
            shrunk[i/2] = (byte) ((array[i] << 4) | array[j]);
        }
        return shrunk;
    }

}