package kim.jade.kotlinx.protobuf.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

expect class ProtobufJsonFormat(
    serializersModule: SerializersModule = EmptySerializersModule(),
) : StringFormat {

    override val serializersModule: SerializersModule

    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String

    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T
}
