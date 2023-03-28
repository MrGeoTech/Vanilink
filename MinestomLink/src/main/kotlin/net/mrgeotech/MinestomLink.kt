package net.mrgeotech

import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.extensions.Extension
import net.minestom.server.tag.Tag
import net.minestom.server.timer.TaskSchedule
import net.mrgeotech.biomes.initBiomes
import net.mrgeotech.network.ChannelManager
import net.mrgeotech.network.ClientImpl
import java.net.InetSocketAddress
import java.nio.channels.SocketChannel

class MinestomLink: Extension() {

    private val clients: MutableList<ClientImpl> = mutableListOf()

    override fun initialize() {
        instance = this

        loadConfig()
        initBiomes()

        val secret = CONFIG.getProperty("secret", "secret")!!
        registerClients(secret)

        MinecraftServer.getSchedulerManager().buildTask(this::checkInstances)
            .repeat(TaskSchedule.seconds(1))
            .schedule()
    }

    override fun terminate() {
        clients.forEach { it.disconnect() }
    }

    private fun checkInstances() {
        MinecraftServer.getInstanceManager().instances.forEach { instance ->
            if (!instance.hasTag(Tag.Boolean("minestomlink"))) return@forEach
            if (instance.generator() is ConnectedChunkLoader) return@forEach
            instance.setGenerator(ConnectedChunkLoader(channelManager))
        }
    }

    private fun registerClients(secret: String) {
        val channelsPerAddress = CONFIG.getProperty("channels-per-address", "1").toInt()
        CONFIG.getStringList("addresses").forEach {
            val split = it.split(":")
            for (i in 0 until channelsPerAddress)
                clients.add(ClientImpl(split[0], split[1].toInt(), secret))
        }
    }

    companion object {
        lateinit var instance: MinestomLink
    }

}