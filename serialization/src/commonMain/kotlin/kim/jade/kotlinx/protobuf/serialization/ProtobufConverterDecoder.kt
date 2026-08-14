package kim.jade.kotlinx.protobuf.serialization

import kim.jade.kotlinx.protobuf.converter.ProtobufConverter
import kim.jade.kotlinx.protobuf.type.ProtobufMessage
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.modules.SerializersModule

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
