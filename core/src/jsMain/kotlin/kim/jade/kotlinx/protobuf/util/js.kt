package kim.jade.kotlinx.protobuf.util

import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set

fun Uint8Array.toByteArray(): ByteArray = ByteArray(length) { this[it] }

fun ByteArray.toUint8Array(): Uint8Array {
    val array = Uint8Array(size)

    for (index in indices) {
        array[index] = this[index]
    }

    return array
}
