package net.mrgeotech.network

import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.ByteBuffer.allocate
import java.nio.channels.SocketChannel
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

class ChannelManager {

    private val openChannels = LinkedBlockingQueue<SocketChannel>()
    private val closed = AtomicBoolean(false)

    fun createNewChannel(address: SocketAddress, secret: String): SocketChannel {
        val channel = SocketChannel.open(address)
        registerNewChannel(channel, secret)
        return channel
    }

    fun registerNewChannel(channel: SocketChannel, secret: String) {
        Thread {
            try {
                var buffer = allocate(5 + secret.length)
                buffer.put(0x01.toByte())
                buffer.putInt(secret.length)
                buffer.put(secret.encodeToByteArray())
                buffer.flip()
                channel.write(buffer)
                buffer = channel.readToBuffer(2)
                val id = buffer.get()
                if (id != 0x01.toByte()) throw IllegalStateException("Invalid packet ID")
                val success = buffer.get()
                if (success != 0x01.toByte()) throw IllegalStateException("Secret is incorrect")
                println("New channel registered")

                if (closed.get()) channel.terminateConnection()
                else openChannels.put(channel)
            } catch (e: Exception) {
                e.printStackTrace()
                channel.terminateConnection()
            }
        }.start()
    }

    fun getChannel(): SocketChannel = openChannels.take()

    fun returnChannel(channel: SocketChannel) {
        if (closed.get()) channel.terminateConnection()
        else openChannels.put(channel)
    }

    fun close() {
        println("Closing channel manager")
        closed.set(true)
        openChannels.forEach { it.terminateConnection() }
    }

}

fun SocketChannel.readToBuffer(size: Int) : ByteBuffer {
    val buffer = allocate(size)
    var read = read(buffer)
    while (read < size) {
        read += read(buffer)
    }
    buffer.position(0)
    return buffer
}

fun SocketChannel.terminateConnection() {
    val buffer = allocate(1)
    buffer.put(0xFF.toByte())
    buffer.flip()
    write(buffer)
    close()
}