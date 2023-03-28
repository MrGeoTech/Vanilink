package net.mrgeotech.networking

import com.karangandhi.networking.TCP.Connection
import com.karangandhi.networking.TCP.TCPServer
import com.karangandhi.networking.utils.Message

class ServerImpl: TCPServer("0.0.0.0", 25565, 50, true) {

    override fun onMessageReceived(receivedMessage: Message<*, *>?, client: Connection<*>?) {
        TODO("Not yet implemented")
    }

    override fun onClientConnected(clientConnection: Connection<TCPServer>?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onClientDisConnected(clientConnection: Connection<*>?) {
        TODO("Not yet implemented")
    }

}