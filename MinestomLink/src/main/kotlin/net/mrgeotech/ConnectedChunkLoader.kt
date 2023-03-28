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

        val endTime = System.currentTimeMillis()
        println("Finished generating unit. Total time: ${endTime - startTime}ms")
    }

}