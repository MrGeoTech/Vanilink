package net.mrgeotech

import net.mrgeotech.blockdata.BukkitBlockDataSupplier
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class SpigotLink: JavaPlugin() {

    private lateinit var channelManager: ChannelManager

    override fun onEnable() {
        saveDefaultConfig()
        channelManager = ChannelManager(
            BukkitBlockDataSupplier(),
            config.getString("secret", "secret")!!,
            config.getStringList("trusted-addresses")
        ) { task ->
            Bukkit.getScheduler().runTaskAsynchronously(this, task)
        }
        channelManager.start(config.getInt("port"))
        // Start task
        Bukkit.getScheduler().runTaskTimerAsynchronously(
            this,
            channelManager::iterate,
            1,
            1
        )

        server.getPluginCommand("blockdata")?.setExecutor(BlockDataCommand())
    }

    override fun onDisable() {
        // Stop task
        Bukkit.getScheduler().cancelTasks(this)
        channelManager.stop()
    }

}