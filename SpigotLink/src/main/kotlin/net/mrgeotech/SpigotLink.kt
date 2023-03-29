package net.mrgeotech

import net.mrgeotech.blockdata.BukkitBlockDataSupplier
import net.mrgeotech.network.ChunkServer
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.net.InetSocketAddress
import javax.net.ssl.SSLContext

class SpigotLink: JavaPlugin() {

    private lateinit var chunkServer: ChunkServer

    override fun onEnable() {
        saveDefaultConfig()

        chunkServer = ChunkServer(
            config.getInt("port", 10000),
            config.getStringList("trusted-addresses").map { InetSocketAddress(it, 10000).address.address },
            SSLContext.getDefault(),
            BukkitBlockDataSupplier(),
            this.logger
        ) {
            Bukkit.getScheduler().runTask(this, it)
        }

        Bukkit.getScheduler().runTaskAsynchronously(this, chunkServer::run)

        server.getPluginCommand("blockdata")?.setExecutor(BlockDataCommand())
    }

    override fun onDisable() {
        // Stop task
        Bukkit.getScheduler().cancelTasks(this)
        // Disconnect clients
        chunkServer.stop()
    }

}