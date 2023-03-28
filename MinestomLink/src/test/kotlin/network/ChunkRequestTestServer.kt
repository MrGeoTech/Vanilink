package network

import com.github.luben.zstd.Zstd
import net.mrgeotech.network.readToBuffer
import java.net.InetSocketAddress
import java.nio.ByteBuffer.allocate
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

class ChunkRequestTestServer {

    private val server = ServerSocketChannel.open()

    fun start(port: Int) {
        server.bind(InetSocketAddress(port))
    }

    fun accept(): SocketChannel = server.accept()!!

    fun handleConnectionRequest(client: SocketChannel) {
        println("Handling connection request")
        val buffer = client.readToBuffer(5)
        val id = buffer.get()
        if (id != 0x01.toByte()) throw IllegalStateException("Invalid packet ID")
        val secretLength = buffer.int
        val secret = client.readToBuffer(secretLength).array().decodeToString()
        assert(secret == "secret")
        println("Secret is correct")

        val response = allocate(2)
        response.put(0x01.toByte())
        response.put(0x01.toByte())
        response.flip()
        client.write(response)
        println("Finished connection request")
    }

    fun handleChunkDataRequest(client: SocketChannel) {
        println("Handling chunk data request")

        val buffer = allocate(1024)
        client.read(buffer)
        buffer.position(0)

        val id = buffer.get()
        if (id != 0x02.toByte()) throw IllegalStateException("Invalid packet ID")

        val startX = buffer.int
        val startY = buffer.int
        val startZ = buffer.int
        val endX = buffer.int
        val endY = buffer.int
        val endZ = buffer.int

        assert(startX == 0)
        assert(startY == 0)
        assert(startZ == 0)
        assert(endX == 1)
        assert(endY == 10)
        assert(endZ == 1)

        val response = listOf(
            "minecraft:stone",
            "minecraft:stone",
            "minecraft:stone",
            "minecraft:dirt",
            "minecraft:dirt",
            "minecraft:grass_block",
            "minecraft:oak_log[axis=y]",
            "minecraft:oak_log[axis=y]",
            "minecraft:oak_log[axis=y]",
            "minecraft:air"
        )

        val responseBytes = response.joinToString("|").toByteArray()
        val compressed = Zstd.compress(responseBytes)

        val responseBuffer = allocate(9 + compressed.size)
        responseBuffer.put(0x02.toByte())
        responseBuffer.putInt(compressed.size)
        responseBuffer.putInt(responseBytes.size)
        responseBuffer.put(compressed)
        responseBuffer.flip()

        client.write(responseBuffer)

        println("Finished chunk data request")
    }

    fun stop() {
        server.close()
    }

}