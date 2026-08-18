package kim.jade.kotlinx.protobuf.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kim.jade.kotlinx.protobuf.converter.protobufjs.ProtobufJsFile
import kim.jade.kotlinx.protobuf.converter.protobufjs.ProtobufJsTypeRegistry
import kotlinx.serialization.StringFormat
import kotlinx.serialization.modules.SerializersModule

actual class ProtobufJsonFormat actual constructor(
    actual override val serializersModule: SerializersModule,
) : StringFormat {

    fun addTypes(vararg files: ProtobufJsFile) {
        ProtobufJsTypeRegistry.add(*files)
    }

    fun addTypes(files: Iterable<ProtobufJsFile>) {
        ProtobufJsTypeRegistry.add(files)
    }

    actual override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
        val encoder = JsonEncoder(serializersModule)
        serializer.serialize(encoder, value)

        return encoder.result
    }

    actual override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
        val decoder = JsonDecoder(string, serializersModule)

        return deserializer.deserialize(decoder)
    }
}
