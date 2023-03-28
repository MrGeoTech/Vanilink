package lighting

import net.minestom.server.instance.Chunk
import net.minestom.server.instance.Instance
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or

/*
 * Copyright Waterdev 2022, under the MIT License
 * Translated and updated by MrGeoTech
 */

const val ARRAY_SIZE = 16 * 16 * 16 / (8 / 4) // blocks / bytes per block

var blockLightArray: ByteArray = ByteArray(ARRAY_SIZE)
var skyLightArray: ByteArray = ByteArray(ARRAY_SIZE)

fun Instance.recalculateLight() {
    val chunks = this.chunks.stream().toList()
    chunks.forEach { it.recalculateLight() }
}

fun Chunk.recalculateLight() {
    val exposed = Array(16) {
        BooleanArray(
            16
        )
    }
    for (booleans in exposed) {
        Arrays.fill(booleans, true)
    }
    val sections = this.sections.asReversed()
    for (section in sections) {
        for (x in 0..15) {
            for (z in 0..15) {
                for (y in 15 downTo -1 + 1) {
                    setBlockLight(getIndex(x, y, z), 12)
                    setSkyLight(getIndex(x, y, z), 15)
                }
            }
        }
        section.skyLight = skyLightArray
        section.blockLight = blockLightArray
    }
    this.setBlock(1, 1, 1, this.getBlock(1, 1, 1))
}

// operation type: updating
fun setBlockLight(index: Int, value: Int) {
    val shift = index and 1 shl 2
    val i = index ushr 1
    blockLightArray[i] = (blockLightArray[i] and (0xF0 ushr shift).toByte() or (value shl shift).toByte())
}

fun setSkyLight(index: Int, value: Int) {
    val shift = index and 1 shl 2
    val i = index ushr 1
    skyLightArray[i] = (skyLightArray[i] and (0xF0 ushr shift).toByte() or (value shl shift).toByte())
}

fun getIndex(x: Int, y: Int, z: Int): Int {
    return y shl 16 / 2 or (z shl 16 / 4) or x
}