package net.mrgeotech.network

import com.github.luben.zstd.Zstd.decompress
import com.karangandhi.networking.TCP.Connection
import com.karangandhi.networking.TCP.TCPClient
import com.karangandhi.networking.utils.Message

class ClientImpl(address: String, port: Int): TCPClient(address, port, true) {

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
                val compressedLength = data.sliceArray(0..3).toInt()
                val decompressedLength = data.sliceArray(4..7).toInt()
                val compressedData = data.sliceArray(8 until 8 + compressedLength)
                val decompressedData = decompress(compressedData, decompressedLength)
                callback(decompressedData.decodeToString())
            }
        }
    }

    override fun onConnected(): Boolean {
        this.sendMessage(Message(MessageType.HANDSHAKE, "secret"))
        return true
    }

    override fun onDisConnected(connection: Connection<*>?) {
        println("Disconnected from server")
    }

    fun setCallback(callback: (String) -> Unit) {
        this.callback = callback
    }

}