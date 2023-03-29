package net.mrgeotech.network

import com.github.luben.zstd.Zstd
import net.mrgeotech.blockdata.BlockDataSupplier
import tlschannel.ServerTlsChannel
import tlschannel.TlsChannel
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Logger
import javax.net.ssl.SSLContext

class ChunkServer(
    private val port: Int,
    private val trustedAddresses: List<ByteArray>,
    private val sslContext: SSLContext = SSLContext.getDefault(),
    private val blockDataSupplier: BlockDataSupplier,
    private val logger: Logger,
    private val scheduler: (Runnable) -> Unit
) {

    private val responses = mutableMapOf<InetSocketAddress, ByteBuffer>()
    private val isEditingResponses = AtomicBoolean(false)
    private val isRunning = AtomicBoolean(false)

    fun run() {
        isRunning.set(true)

        val serverSocket = ServerSocketChannel.open()
        serverSocket.bind(InetSocketAddress(port))
        serverSocket.configureBlocking(false)

        val selector = Selector.open()
        serverSocket.register(selector, SelectionKey.OP_ACCEPT)

        while (isRunning.get()) {
            selector.select()
            val keys = selector.selectedKeys()
            val iterator = keys.iterator()

            while (iterator.hasNext()) {
                // Get the next key
                val key = iterator.next()
                iterator.remove()
                // Process the key
                if (key.isAcceptable)
                    accept(key)
                else if (key.isReadable)
                    read(key)
                else if (key.isWritable)
                    write(key)
            }
        }
    }

    fun stop() {
        isRunning.set(false)
    }

    private fun accept(key: SelectionKey) {
        val serverSocket = key.channel() as ServerSocketChannel
        val rawChannel = serverSocket.accept()
        rawChannel.configureBlocking(false)
        // Check if the address is trusted
        if (!trustedAddresses.contains((rawChannel.remoteAddress as InetSocketAddress).address.address)) {
            rawChannel.close()
            return
        }
        // Create a TLS channel
        val tlsChannel = ServerTlsChannel
            .newBuilder(rawChannel, sslContext).build()
        // Register the channel for reading
        val newKey = rawChannel.register(key.selector(), SelectionKey.OP_READ)
        newKey.attach(tlsChannel)
    }

    private fun read(key: SelectionKey) {
        val tlsChannel = key.attachment() as TlsChannel
        val remoteAddress = (key.channel() as SocketChannel).remoteAddress as InetSocketAddress
        // Read the request
        val request = tlsChannel.readToBuffer(24).asIntBuffer().array()
        // Logging
        val start = request.sliceArray(0 until 3)
        val end = request.sliceArray(3 until 6)
        logger.info("Received request for chunk data from $remoteAddress from $start to $end")
        // Process the request asynchronously
        scheduler.invoke {
            val blockData = blockDataSupplier.getUnitData(request[0], request[1], request[2], request[3], request[4], request[5])
            val uncompressed = blockData.joinToString("|").encodeToByteArray()
            val compressed = Zstd.compress(uncompressed)
            val response = ByteArray(8 + compressed.size)
            response.putInt(0, compressed.size)
            response.putInt(4, uncompressed.size)
            compressed.copyInto(response, 8)
            isEditingResponses.waitTillFalseThenFlip()
            responses[remoteAddress] = ByteBuffer.wrap(response)
            isEditingResponses.set(false)
        }
        // Register for writing
        key.interestOps(SelectionKey.OP_WRITE)
    }

    private fun write(key: SelectionKey) {
        val tlsChannel = key.attachment() as TlsChannel
        val remoteAddress = (key.channel() as SocketChannel).remoteAddress as InetSocketAddress
        // Get the response
        isEditingResponses.waitTillFalseThenFlip()
        val response = responses[remoteAddress]
        isEditingResponses.set(false)
        if (response == null) return
        // Send the response
        tlsChannel.write(response)
        // Remove the response
        isEditingResponses.waitTillFalseThenFlip()
        responses.remove(remoteAddress)
        isEditingResponses.set(false)
        // Logging
        logger.info("Sent response to $remoteAddress")
        // Register for reading again
        key.interestOps(SelectionKey.OP_READ)
    }

}

private fun AtomicBoolean.waitTillFalseThenFlip() {
    while (this.get()) {
        Thread.sleep(1)
    }
    this.set(true)
}

private fun ByteArray.putInt(index: Int, value: Int) {
    this[index] = (value shr 24).toByte()
    this[index + 1] = (value shr 16).toByte()
    this[index + 2] = (value shr 8).toByte()
    this[index + 3] = value.toByte()
}

private fun TlsChannel.readToBuffer(size: Int): ByteBuffer {
    val buffer = ByteBuffer.allocate(size)
    while (buffer.hasRemaining()) {
        this.read(buffer)
    }
    buffer.flip()
    return buffer
}