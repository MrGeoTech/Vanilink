package net.mrgeotech.network

import com.github.luben.zstd.Zstd.decompress
import com.karangandhi.networking.TCP.Connection
import com.karangandhi.networking.TCP.TCPClient
import com.karangandhi.networking.utils.Message
import net.minestom.server.coordinate.Point

class ClientImpl(
    address: String,
    port: Int,
    private val secret: String
): TCPClient(address, port, true) {

    private var callback: (String) -> Unit = {}

    override fun onMessageReceived(message: Message<*, *>, connection: Connection<*>?) {
        when (message.id) {
            MessageType.HANDSHAKE -> {
                val success = message.toByteArray()[0]
                if (success != 0x01.toByte()) throw IllegalStateException("Secret is incorrect")
                println("Connected to server")
            }
            MessageType.CHUNK_DATA -> {
                val data = message.messageBody as ByteArray
                val compressedLength = data.getInt(0)
                val decompressedLength = data.getInt(4)
                val compressedData = data.sliceArray(8 until 8 + compressedLength)
                val decompressedData = decompress(compressedData, decompressedLength)
                callback(decompressedData.decodeToString())
            }
        }
    }

    override fun onConnected(): Boolean {
        this.sendMessage(Message(MessageType.HANDSHAKE, secret))
        return true
    }

    override fun onDisConnected(connection: Connection<*>?) {
        println("Disconnected from server")
    }

    fun requestBlockData(start: Point, end: Point, callback: (String) -> Unit) {
        val request = IntArray(6)
        request[0] = start.blockX()
        request[1] = start.blockY()
        request[2] = start.blockZ()
        request[3] = end.blockX()
        request[4] = end.blockY()
        request[5] = end.blockZ()
        this.sendMessage(Message(MessageType.CHUNK_DATA, request))
        this.callback = callback
    }

}