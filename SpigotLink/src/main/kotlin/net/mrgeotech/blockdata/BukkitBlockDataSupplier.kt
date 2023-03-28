package net.mrgeotech.blockdata

import org.bukkit.Bukkit
import org.bukkit.Location

class BukkitBlockDataSupplier: BlockDataSupplier {

    override fun getUnitData(
        startX: Int,
        startY: Int,
        startZ: Int,
        sizeX: Int,
        sizeY: Int,
        sizeZ: Int
    ): List<String> {
        // Getting the start and size locations
        val start = Location(
            Bukkit.getWorlds()[0],
            startX.toDouble(),
            startY.toDouble(),
            startZ.toDouble()
        )
        val startLoad = System.currentTimeMillis()
        // Making sure the chunk is loaded
        Bukkit.getScheduler().runTask(
            Bukkit.getPluginManager().getPlugin("SpigotLink")!!,
            Runnable { start.chunk.load() }
        )
        // Waiting for the chunk to load
        while (!start.chunk.isLoaded) {
            Thread.sleep(10)
        }
        val endLoad = System.currentTimeMillis()
        println("Chunk load time: ${endLoad - startLoad}ms")
        println("Start: $startX, $startY, $startZ | Size: $sizeX, $sizeY, $sizeZ")
        // Getting a chunk snapshot
        val snapshot = start.chunk.getChunkSnapshot(false, true, false)
        // Getting the block data
        val blocks = mutableListOf<String>()
        for (x in 0..15) {
            for (y in -64..319) {
                for (z in 0..15) {
                    blocks.add(snapshot.getBlockData(x, y, z).asString)
                    blocks.add(snapshot.getBiome(x, y, z).key.toString())
                }
            }
        }
        val end = System.currentTimeMillis()
        println("Chunk read time: ${end - endLoad}ms")
        // Scheduling the chunk to unload
        Bukkit.getScheduler().runTaskLater(
            Bukkit.getPluginManager().getPlugin("SpigotLink")!!,
            Runnable { start.chunk.unload() },
            100
        )

        return blocks
    }

}