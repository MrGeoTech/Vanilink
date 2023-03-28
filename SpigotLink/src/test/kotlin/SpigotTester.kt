import com.github.luben.zstd.Zstd
import net.mrgeotech.ChannelManager
import org.junit.jupiter.api.Test
import java.net.InetSocketAddress
import java.nio.ByteBuffer.allocate
import java.nio.channels.SocketChannel

class SpigotTester {

    @Test
    fun blockRequestTest() {
        val channelManager = ChannelManager(TestBlockDataSupplier(), "secret", listOf("127.0.0.1")) {
            it.run()
        }
        channelManager.start(10000)

        val channel = SocketChannel.open()
        channel.connect(InetSocketAddress("localhost", 10000))

        val secretEncoded = "secret".encodeToByteArray()
        var buffer = allocate(5 + secretEncoded.size)
        buffer.put(0x01) // 1 byte
        buffer.putInt(secretEncoded.size) // 4 bytes
        buffer.put(secretEncoded) // size bytes
        buffer.flip()
        channel.write(buffer)

        channelManager.iterate()
        channelManager.iterate()
        channelManager.iterate()
        channelManager.iterate()

        buffer = allocate(2)
        channel.read(buffer)
        buffer.position(0)
        val packetId = buffer.get()
        if (packetId != 0x01.toByte()) throw IllegalStateException("Invalid packet ID")
        val success = buffer.get()
        if (success != 0x01.toByte()) throw IllegalStateException("Connection failed")

        buffer = allocate(25)
        buffer.put(0x02) // 1 byte
        buffer.putInt(0) // 4 bytes
        buffer.putInt(0) // 4 bytes
        buffer.putInt(0) // 4 bytes
        buffer.putInt(1) // 4 bytes
        buffer.putInt(9) // 4 bytes
        buffer.putInt(1) // 4 bytes
        buffer.flip()
        channel.write(buffer)

        channelManager.iterate()
        channelManager.iterate()
        channelManager.iterate()
        channelManager.iterate()

        val chunkData = channel.getChunkData()
        val blocks = chunkData.trim().split("|")
        val expected = listOf(
            "minecraft:stone",
            "minecraft:stone",
            "minecraft:stone",
            "minecraft:andesite",
            "minecraft:andesite",
            "minecraft:cracked_stone_bricks",
            "minecraft:gravel",
            "minecraft:acacia_log[axis=x]",
            "minecraft:acacia_log[axis=z]"
        )

        assert(expected == blocks)
    }

    private fun SocketChannel.getChunkData(): String {
        var buffer = allocate(9)

        this.read(buffer)
        buffer.position(0)

        val packetId = buffer.get()
        if (packetId != 0x02.toByte()) throw IllegalStateException("Invalid packet ID")

        val compressedLength = buffer.int
        val uncompressedLength = buffer.int

        buffer = allocate(compressedLength)
        this.read(buffer)
        buffer.position(0)

        val compressed = buffer.array()
        val uncompressed = ByteArray(uncompressedLength)

        Zstd.decompress(uncompressed, compressed)

        return String(uncompressed, Charsets.UTF_8)
    }

}