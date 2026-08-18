package kim.jade.kotlinx.protobuf.serialization

import kim.jade.kotlinx.protobuf.converter.ProtobufConverter
import kim.jade.kotlinx.protobuf.converter.mapper.ProtobufJsMapper
import kim.jade.kotlinx.protobuf.type.ProtobufMessage
import kotlinx.serialization.modules.SerializersModule

private val JSON_OPTIONS: dynamic = js(
    "({ longs: String, enums: String, bytes: String, json: true, defaults: false })"
)

class JsonEncoder(
    serializersModule: SerializersModule,
) : ProtobufConverterEncoder(serializersModule) {

    var result: String = ""
        private set

    override fun <T : ProtobufMessage> serialize(converter: ProtobufConverter<T>, value: T) {
        if (converter !is ProtobufJsMapper<T, *>) {
            throw IllegalArgumentException("Only acceptable ProtobufJsMapper")
        }

        val type = converter.protobufJsFile.lookupType(converter.typeName)

        result = JSON.stringify(type.toObject(converter.convert(value), JSON_OPTIONS))
    }
}
