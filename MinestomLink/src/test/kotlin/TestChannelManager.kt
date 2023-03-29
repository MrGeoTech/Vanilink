import com.github.luben.zstd.Zstd.decompress
import net.minestom.server.instance.block.Block
import net.mrgeotech.network.ChannelManager
import net.mrgeotech.network.readToBuffer
import net.mrgeotech.network.toBlockData
import network.ChunkRequestTestServer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import kotlin.test.assertEquals

class TestChannelManager {

    @Test
    fun testStringToBlock() {
        assertEquals(Block.AIR, "minecraft:air".toBlockData())
        assertEquals(Block.STONE, "stone".toBlockData())
        assertEquals(Block.OAK_LOG.withProperties(mapOf(Pair("axis", "z"))), "minecraft:oak_log[axis=z]".toBlockData())

        assertThrows<IllegalArgumentException> { "".toBlockData() }
    }

    @Test
    fun testChannelManager() {
        val manager = ChannelManager()
        val testServer = ChunkRequestTestServer()
        testServer.start(10000)
        val channel = SocketChannel.open(InetSocketAddress("localhost", 10000))
        manager.registerNewChannel(channel, "secret")
        val channelS = testServer.accept()
        testServer.handleConnectionRequest(channelS)
        val channel2 = manager.getChannel()
        // Request chunk data
        println("Requesting chunk data")
        val request = ByteBuffer.allocate(25)
        request.put(0x02.toByte())
        request.putInt(0)
        request.putInt(0)
        request.putInt(0)
        request.putInt(1)
        request.putInt(10)
        request.putInt(1)
        request.flip()
        channel2.write(request)
        // Read chunk data
        println("Reading chunk data")
        testServer.handleChunkDataRequest(channelS)
        val buffer = channel2.readToBuffer(9)
        val id = buffer.get()
        if (id != 0x02.toByte()) throw IllegalStateException("Invalid packet ID")
        println("ID is correct")

        val compressedLength = buffer.int
        val uncompressedLength = buffer.int

        val compressed = channel2.readToBuffer(compressedLength).array()
        val uncompressed = ByteArray(uncompressedLength)

        decompress(uncompressed, compressed)

        val blockString = uncompressed.decodeToString()
        val blocks = blockString.split("|").map { it.toBlockData() }
        assertEquals(10, blocks.size)
        assertEquals(Block.STONE, blocks[0])
        assertEquals(Block.STONE, blocks[1])
        assertEquals(Block.STONE, blocks[2])
        assertEquals(Block.DIRT, blocks[3])
        assertEquals(Block.DIRT, blocks[4])
        assertEquals(Block.GRASS_BLOCK, blocks[5])
        assertEquals(Block.OAK_LOG.withProperties(mapOf(Pair("axis", "y"))), blocks[6])
        assertEquals(Block.OAK_LOG.withProperties(mapOf(Pair("axis", "y"))), blocks[7])
        assertEquals(Block.OAK_LOG.withProperties(mapOf(Pair("axis", "y"))), blocks[8])
        assertEquals(Block.AIR, blocks[9])

        manager.close()
        channel2.close()
        channelS.close()
        testServer.stop()
    }

}