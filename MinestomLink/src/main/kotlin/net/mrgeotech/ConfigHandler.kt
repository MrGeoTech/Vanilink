package net.mrgeotech

import java.io.File
import java.io.IOException
import java.net.InetSocketAddress
import java.util.Properties

val CONFIG = Properties()

@Throws(IOException::class, IllegalStateException::class)
fun loadConfig() {
    CONFIG.set("addresses", listOf("localhost:10000"))
    CONFIG.setProperty("secret", "secret")
    val file = File(System.getProperty("user.dir"), "extensions/MinestomLink/config.properties")
    file.parentFile.mkdirs()
    if (file.createNewFile())
        CONFIG.store(file.outputStream(), "MinestomLink config")
    else
        CONFIG.load(file.inputStream())
}

fun Properties.getStringList(key: String): List<String> {
    return this.getProperty(key).split(",")
}

fun Properties.set(key: String, value: List<String>) {
    this.setProperty(key, value.joinToString(","))
}

fun getAddress(): InetSocketAddress {
    return CONFIG.getStringList("addresses").map {
        val split = it.split(":")
        return InetSocketAddress(split[0], split[1].toInt())
    }.randomOrNull<InetSocketAddress>() ?: throw IllegalStateException("No addresses found in config")
}