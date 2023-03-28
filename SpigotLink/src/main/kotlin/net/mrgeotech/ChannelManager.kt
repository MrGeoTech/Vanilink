package net.mrgeotech

import com.github.luben.zstd.Zstd
import net.mrgeotech.blockdata.BlockDataSupplier
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.ByteBuffer.allocate
import java.nio.channels.*
import java.nio.channels.SelectionKey.OP_READ
import java.nio.channels.SelectionKey.OP_WRITE

class ChannelManager(
    private val blockDataSupplier: BlockDataSupplier,
    private val secret: String,
    private val trustedAddresses: List<String>,
    private val scheduler: (Runnable) -> Unit
) {

    private val selector = Selector.open()
    private val server = ServerSocketChannel.open()
    private val responses = mutableMapOf<SocketAddress, ByteBuffer>()

    fun start(port: Int) {
        // Register server
        server.bind(InetSocketAddress("0.0.0.0", port))
        server.configureBlocking(false)

        val ops = server.validOps()
        server.register(selector, ops)
    }

    fun stop() {
        selector.select(1000)
        selector.selectedKeys().forEach {
            val buffer = allocate(1).put(0xFF.toByte())
            (it.channel() as SocketChannel).write(buffer)
            it.channel().close()
            it.cancel()
        }
        println("selector keys cancelled")
        // Close server
        server.close()
        selector.close()
    }

    fun iterate() {
        try {
            selector.select(25)
            val iterator = selector.selectedKeys().iterator()

            while (iterator.hasNext()) {
                val key = iterator.next()
                iterator.remove()

                try {
                    if (key.isAcceptable) {
                        val client = (key.channel() as ServerSocketChannel).accept()
                        if (client == null) {
                            println("Client is null")
                            continue
                        }
                        client.configureBlocking(false)
                        client.register(selector, OP_READ)
                    } else if (key.isReadable) {
                        try {
                            read(key)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else if (key.isWritable) {
                        scheduler.invoke {
                            write(key)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    key.channel().close()
                    key.cancel()
                }
            }
        } catch (e: ClosedSelectorException) {
            // Ignore
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun read(key: SelectionKey) {
        val startTime = System.currentTimeMillis()
        key.attach(startTime)
        val client = key.channel() as SocketChannel
        // Validate client
        val address = client.remoteAddress as InetSocketAddress
        if (address.hostString !in trustedAddresses) {
            client.close()
            key.cancel()
            return
        }

        if (responses.contains(client.remoteAddress)) return
        // Get request type
        val buffer = client.readToBuffer(1)

        when (buffer.get()) {
            0xFF.toByte() -> handleCloseRequest(key)
            0x01.toByte() -> handleConnectionRequest(key)
            0x02.toByte() -> scheduler.invoke { handleChunkRequest(key) }
            // TODO: Handle biome request
            else -> {
                client.close()
                key.cancel()
            }
        }
    }

    private fun handleCloseRequest(key: SelectionKey) {
        println("Handling close request")
        val client = key.channel() as SocketChannel
        client.close()
        key.cancel()
    }

    private fun handleConnectionRequest(key: SelectionKey) {
        // Resetting key
        key.interestOps(OP_WRITE)

        println("Handling connection request")
        val client = key.channel() as SocketChannel
        // Getting data
        val buffer = client.readToBuffer(4)
        val size = buffer.int
        val data = client.readToBuffer(size)
        // Getting secret
        val secret = data.array().decodeToString()
        // Checking secret
        if (secret == this.secret)
            responses[client.remoteAddress] = allocate(2).put(0x01).put(0x01)
        else
            responses[client.remoteAddress] = allocate(2).put(0x01).put(0x00)
    }

    private fun handleChunkRequest(key: SelectionKey) {
        // Resetting key
        key.interestOps(OP_WRITE)

        println("Handling chunk request")
        val client = key.channel() as SocketChannel
        // Getting data
        val buffer = client.readToBuffer(28)
        val response = blockDataSupplier.getUnitData(
            buffer.int,
            buffer.int,
            buffer.int,
            buffer.int,
            buffer.int,
            buffer.int
        ).joinToString("|")
        // Compressing data
        val uncompressed = response.encodeToByteArray()
        val compressed = Zstd.compress(uncompressed)
        // Packaging data
        val responseBuffer = allocate(9 + compressed.size)
        responseBuffer.put(0x02)
        responseBuffer.putInt(compressed.size)
        responseBuffer.putInt(uncompressed.size)
        responseBuffer.put(compressed)
        // Adding response to map
        responses[client.remoteAddress] = responseBuffer
    }

    private fun write(key: SelectionKey) {
        // Getting request
        val client = key.channel() as SocketChannel
        val response = responses[client.remoteAddress] ?: return
        responses.remove(client.remoteAddress)
        // Sending data
        client.write(response.flip())
        // Resetting key
        key.interestOps(OP_READ)
        key.attachment()?.let {
            val endTime = System.currentTimeMillis()
            val startTime = it as Long
            println("Request took ${endTime - startTime}ms")
        }
    }

}

fun SocketChannel.readToBuffer(size: Int) : ByteBuffer {
    val buffer = allocate(size)
    read(buffer)
    buffer.position(0)
    return buffer
}