package net.mrgeotech

import net.minestom.server.MinecraftServer
import net.minestom.server.extensions.Extension
import net.minestom.server.tag.Tag
import net.minestom.server.timer.TaskSchedule
import net.mrgeotech.biomes.initBiomes
import tlschannel.ClientTlsChannel
import java.net.InetSocketAddress
import java.nio.channels.SocketChannel
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import javax.net.ssl.SSLContext

class MinestomLink: Extension() {

    override fun initialize() {
        instance = this

        loadConfig()
        initBiomes()
        registerClients()

        MinecraftServer.getSchedulerManager().buildTask(this::checkInstances)
            .repeat(TaskSchedule.seconds(1))
            .schedule()
    }

    override fun terminate() {
        val collection = mutableListOf<ClientTlsChannel>()
        channels.drainTo(collection)
        collection.stream().forEach { it.close() }
    }

    private fun checkInstances() {
        MinecraftServer.getInstanceManager().instances.forEach { instance ->
            if (!instance.hasTag(Tag.Boolean("minestomlink"))) return@forEach
            if (instance.generator() is ConnectedChunkLoader) return@forEach
            instance.setGenerator(ConnectedChunkLoader())
        }
    }

    private fun registerClients() {
        val sslContext = SSLContext.getDefault()
        val channelsPerAddress = CONFIG.getProperty("channels-per-address", "1").toInt()

        CONFIG.getStringList("addresses").forEach {
            val split = it.split(":")
            for (i in 0 until channelsPerAddress) {
                registerNewClient(InetSocketAddress(split[0], split[1].toInt()), sslContext)
            }
        }
    }

    private fun registerNewClient(address: InetSocketAddress, sslContext: SSLContext) {
        channels.put(ClientTlsChannel.newBuilder(
            SocketChannel.open(address),
            sslContext).build())
    }

    companion object {
        lateinit var instance: MinestomLink
        private val channels: BlockingQueue<ClientTlsChannel> = LinkedBlockingQueue()

        fun getChannel(): ClientTlsChannel {
            return channels.take()
        }

        fun returnChannel(channel: ClientTlsChannel) {
            channels.put(channel)
        }
    }

}