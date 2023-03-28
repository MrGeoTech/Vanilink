package net.mrgeotech.network

import net.minestom.server.instance.block.Block
import net.minestom.server.world.biomes.Biome
import net.mrgeotech.biomes.BIOMES

fun String.toDataMap(): Map<Block, Biome> {
    return this.split("|").associate {
        it.toBlockData() to it.toBiome()
    }
}

fun String.toBlockData(): Block {
    val id = this.substringBefore("[")
    if (id == this) return Block.fromNamespaceId(id)
        ?: throw IllegalArgumentException("Invalid block id: $id")

    val properties = this.substringAfter("[").substringBefore("]")
        .split(",")
        .map { it.split("=") }.associate { it[0] to it[1] }
    return Block.fromNamespaceId(id)?.withProperties(properties)
        ?: throw IllegalArgumentException("Invalid block id: $id")
}

fun String.toBiome(): Biome {
    BIOMES.forEach {
        if (it.name().toString() == this) return it
    }
    throw IllegalArgumentException("Invalid biome ID")
}

fun ByteArray.getInt(index: Int): Int {
    return (this[index].toInt() shl 24) or
            (this[index + 1].toInt() shl 16) or
            (this[index + 2].toInt() shl 8) or
            this[index + 3].toInt()
}