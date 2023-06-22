package kr.jadekim.protobuf.kotlinx

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

class ProtobufFormat(override val serializersModule: SerializersModule = EmptySerializersModule()) : BinaryFormat {

    override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
        val encoder = ProtobufConverterEncoder(serializersModule)
        serializer.serialize(encoder, value)
        return encoder.result
    }

    override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
        val decoder = ProtobufConverterDecoder(bytes, serializersModule)
        return deserializer.deserialize(decoder)
    }
}
