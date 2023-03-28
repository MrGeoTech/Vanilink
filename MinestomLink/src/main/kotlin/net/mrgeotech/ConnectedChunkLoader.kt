package net.mrgeotech

import com.github.luben.zstd.Zstd.decompress
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.instance.generator.Generator
import net.minestom.server.world.biomes.Biome
import net.mrgeotech.biomes.BIOMES
import net.mrgeotech.network.ChannelManager
import net.mrgeotech.network.readToBuffer
import net.mrgeotech.network.toBiome
import java.nio.ByteBuffer.allocate

class ConnectedChunkLoader(private val channelManager: ChannelManager): Generator {

    override fun generate(unit: GenerationUnit) {
        val start = unit.absoluteStart()
        val end = unit.absoluteEnd()
        println("Generating unit starting at ${start.x()}, ${start.y()}, ${start.z()} and ending at ${end.x()}, ${end.y()}, ${end.z()}. Chunk start ${start.chunkX()}, ${start.chunkZ()}")
        val startTime = System.currentTimeMillis()
        // Request chunk data
        val request = allocate(25)
        request.put(0x02.toByte())
        request.putInt(start.blockX())
        request.putInt(start.blockY())
        request.putInt(start.blockZ())
        request.putInt(end.blockX())
        request.putInt(end.blockY())
        request.putInt(end.blockZ())
        request.flip()
        val channel = channelManager.getChannel()
        val startSend = System.currentTimeMillis()
        channel.write(request)
        val endSend = System.currentTimeMillis()
        // Read chunk data
        val buffer = channel.readToBuffer(9)
        val endRead = System.currentTimeMillis()
        val id = buffer.get()
        if (id != 0x02.toByte()) {
            throw IllegalStateException("Invalid packet ID: $id")
        }

        val compressedLength = buffer.int
        val uncompressedLength = buffer.int

        val compressed = channel.readToBuffer(compressedLength).array()
        val uncompressed = ByteArray(uncompressedLength)

        // Give back channel
        channelManager.returnChannel(channel)

        decompress(uncompressed, compressed)

        val data = uncompressed.decodeToString().split("|")
        val endNetTime = System.currentTimeMillis()
        // Set blocks in chunk
        val stringIterator = data.iterator()
        for (x in start.blockX() until start.blockX() + 16) {
            for (y in -64..319) {
                for (z in start.blockZ() until start.blockZ() + 16) {
                    unit.modifier().setBlock(x, y, z, stringIterator.next().toBlock())
                    unit.modifier().setBiome(x, y, z, stringIterator.next().toBiome())
                }
            }
        }
        val endTime = System.currentTimeMillis()
        println("Finished generating unit. Send time: ${endSend - startSend}ms, read time: ${endRead - endSend}ms, net time: ${endNetTime - endRead}ms, total time: ${endTime - startTime}ms")
    }

}