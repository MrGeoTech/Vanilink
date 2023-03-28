import net.mrgeotech.blockdata.BlockDataSupplier

class TestBlockDataSupplier: BlockDataSupplier {

    override fun getUnitData(
        startX: Int,
        startY: Int,
        startZ: Int,
        sizeX: Int,
        sizeY: Int,
        sizeZ: Int
    ): List<String> {
        return listOf(
            "minecraft:stone",
            "minecraft:stone",
            "minecraft:stone",
            "minecraft:andesite",
            "minecraft:andesite",
            "minecraft:cracked_stone_bricks",
            "minecraft:gravel",
            "minecraft:acacia_log[axis=x]",
            "minecraft:acacia_log[axis=z]"
        )
    }

}