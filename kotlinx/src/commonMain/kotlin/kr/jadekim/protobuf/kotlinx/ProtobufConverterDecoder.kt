package kr.jadekim.protobuf.kotlinx

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.modules.SerializersModule
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.type.ProtobufMessage

@OptIn(ExperimentalSerializationApi::class)
abstract class ProtobufConverterDecoder(
    override val serializersModule: SerializersModule,
) : AbstractDecoder() {

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        throw IllegalStateException("Not usable decodeElementIndex")
    }

    abstract fun <T : ProtobufMessage> deserialize(converter: ProtobufConverter<T>): T
}

class ProtobufDecoder(
    val bytes: ByteArray,
    serializersModule: SerializersModule,
) : ProtobufConverterDecoder(serializersModule) {

    override fun <T : ProtobufMessage> deserialize(converter: ProtobufConverter<T>): T {
        return converter.deserialize(bytes)
    }
}
