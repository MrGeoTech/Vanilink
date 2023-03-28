package net.mrgeotech

import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.extensions.Extension
import net.minestom.server.tag.Tag
import net.minestom.server.timer.TaskSchedule
import net.mrgeotech.biomes.initBiomes
import net.mrgeotech.network.ChannelManager
import java.net.InetSocketAddress
import java.nio.channels.SocketChannel

class MinestomLink: Extension() {

    private val channelManager = ChannelManager()

    override fun initialize() {
        instance = this

        loadConfig()
        initBiomes()

        val secret = CONFIG.getProperty("secret", "secret")!!
        val channelsPerAddress = CONFIG.getProperty("channels-per-address", "1").toInt()
        CONFIG.getStringList("addresses").forEach {
            val split = it.split(":")
            val address = InetSocketAddress(split[0], split[1].toInt())
            for (i in 0 until channelsPerAddress)
                channelManager.registerNewChannel(SocketChannel.open(address), secret)
        }

        MinecraftServer.getSchedulerManager().buildTask(this::checkInstances)
            .repeat(TaskSchedule.seconds(1))
            .schedule()
    }

    override fun terminate() {
        channelManager.close()
    }

    private fun checkInstances() {
        MinecraftServer.getInstanceManager().instances.forEach { instance ->
            if (!instance.hasTag(Tag.Boolean("minestomlink"))) return@forEach
            if (instance.generator() is ConnectedChunkLoader) return@forEach
            instance.setGenerator(ConnectedChunkLoader(channelManager))
        }
    }

    companion object {
        lateinit var instance: MinestomLink
    }

}