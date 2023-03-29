package net.mrgeotech

import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.instance.generator.Generator
import net.mrgeotech.network.getBlockData

class ConnectedChunkLoader: Generator {

    override fun generate(unit: GenerationUnit) {
        val start = unit.absoluteStart()
        val size = unit.absoluteEnd()
        println("Generating unit starting at ${start.x()}, ${start.y()}, ${start.z()} with a size of ${size.x()}, ${size.y()}, ${size.z()}. Chunk start ${start.chunkX()}, ${start.chunkZ()}")
        val startTime = System.currentTimeMillis()
        val data = getBlockData(start, size)
        var index = 0
        for (x in 0 until size.blockX()) {
            for (y in start.blockY() until size.blockY()) {
                for (z in 0 until size.blockZ()) {
                    unit.modifier().setBlock(x, y, z, data.keys.elementAt(index))
                    unit.modifier().setBiome(x, y, z, data.values.elementAt(index))
                    index++
                }
            }
        }
        val endTime = System.currentTimeMillis()
        println("Finished generating unit. Total time: ${endTime - startTime}ms")
    }
}