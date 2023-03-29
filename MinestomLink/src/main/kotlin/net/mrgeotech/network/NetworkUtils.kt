package net.mrgeotech.network

import com.github.luben.zstd.Zstd.decompress
import net.minestom.server.coordinate.Point
import net.minestom.server.instance.block.Block
import net.minestom.server.world.biomes.Biome
import net.mrgeotech.MinestomLink
import net.mrgeotech.biomes.BIOMES
import tlschannel.ClientTlsChannel
import java.nio.ByteBuffer
import java.nio.ByteBuffer.allocate

fun getBlockData(start: Point, size: Point): Map<Block, Biome> {
    val request = allocate(24)
    request.putInt(start.blockX())
    request.putInt(start.blockY())
    request.putInt(start.blockZ())
    request.putInt(size.blockX())
    request.putInt(size.blockY())
    request.putInt(size.blockZ())
    // Write the request to the channel
    val channel = MinestomLink.getChannel()
    channel.write(request)
    // Read the response
    val compressedLength = channel.readToBuffer(4).getInt(0)
    val decompressedLength = channel.readToBuffer(4).getInt(0)
    val compressedData = channel.readToBuffer(compressedLength).array()
    // Free the channel
    channel.free()
    // Decompress and parse the data
    return decompress(compressedData, decompressedLength)
        .decodeToString().toDataMap()
}

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

fun ClientTlsChannel.readToBuffer(size: Int): ByteBuffer {
    val buffer = allocate(size)
    while (buffer.hasRemaining()) {
        this.read(buffer)
    }
    buffer.flip()
    return buffer
}

fun ClientTlsChannel.free() {
    MinestomLink.returnChannel(this)
}