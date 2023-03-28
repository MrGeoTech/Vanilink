package net.mrgeotech.networking

import com.github.luben.zstd.Zstd.compress
import com.karangandhi.networking.TCP.Connection
import com.karangandhi.networking.TCP.TCPServer
import com.karangandhi.networking.utils.Message
import net.mrgeotech.blockdata.BlockDataSupplier
import java.net.InetSocketAddress

class ServerImpl(
    private val port: Int,
    private val secret: String,
    private val trustedAddresses: List<String>,
    private val scheduler: (Runnable) -> Unit,
    private val blockDataSupplier: BlockDataSupplier
): TCPServer("0.0.0.0", 25565, 50, true) {

    override fun onMessageReceived(receivedMessage: Message<*, *>?, client: Connection<*>?) {
        if (receivedMessage == null || client == null) return
        when (receivedMessage.id) {
            MessageType.HANDSHAKE -> {
                val success = if (receivedMessage.messageBody == secret) 0x01.toByte() else 0x00.toByte()
                client.addMessage(Message(MessageType.HANDSHAKE, success))
            }
            MessageType.CHUNK_DATA -> {
                val data = receivedMessage.messageBody as IntArray
                val start = data.sliceArray(0 until 3)
                val end = data.sliceArray(3 until 6)
                println("Received request for chunk data from ${client.ownerSocket.remoteSocketAddress} from $start to $end")
                scheduler.invoke {
                    val blockData = blockDataSupplier.getUnitData(data[0], data[1], data[2], data[3], data[4], data[5])
                    val uncompressed = blockData.joinToString("|").encodeToByteArray()
                    val compressed = compress(uncompressed)
                    val response = ByteArray(8 + compressed.size)
                    response.putInt(0, compressed.size)
                    response.putInt(4, uncompressed.size)
                    compressed.copyInto(response, 8)
                    client.addMessage(Message(MessageType.CHUNK_DATA, response))
                }
            }
        }
    }

    override fun onClientConnected(clientConnection: Connection<TCPServer>?): Boolean {
        if (clientConnection == null) {
            println("Client is null")
            return false
        }
        if (trustedAddresses.contains((clientConnection.ownerSocket.remoteSocketAddress as InetSocketAddress).address.hostAddress))
            return true
        return false
    }

    override fun onClientDisConnected(clientConnection: Connection<*>?) {
        TODO("Not yet implemented")
    }

    private fun ByteArray.putInt(index: Int, value: Int) {
        this[index] = (value shr 24).toByte()
        this[index + 1] = (value shr 16).toByte()
        this[index + 2] = (value shr 8).toByte()
        this[index + 3] = value.toByte()
    }

}