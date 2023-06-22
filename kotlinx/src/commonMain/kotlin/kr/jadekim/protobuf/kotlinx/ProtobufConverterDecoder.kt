package kr.jadekim.protobuf.kotlinx

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
class ProtobufConverterDecoder(val bytes: ByteArray, override val serializersModule: SerializersModule) :
    AbstractDecoder() {

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        throw IllegalStateException("Not usable decodeElementIndex")
    }

    fun decodeByteArray(): ByteArray = bytes
}