package net.mrgeotech.blockdata

interface BlockDataSupplier {
    fun getUnitData(
        startX: Int,
        startY: Int,
        startZ: Int,
        sizeX: Int,
        sizeY: Int,
        sizeZ: Int
    ): List<String>
}
