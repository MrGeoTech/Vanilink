package com.github.mrgeotech.lighting;

/*
 * Copyright Waterdev 2022, under the MIT License
 */

public class SectionUtils {

    private final int dimension;

    public SectionUtils() {
        dimension = 16;
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

}