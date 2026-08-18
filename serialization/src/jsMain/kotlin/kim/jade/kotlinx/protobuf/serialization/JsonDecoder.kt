package kim.jade.kotlinx.protobuf.serialization

import kim.jade.kotlinx.protobuf.converter.ProtobufConverter
import kim.jade.kotlinx.protobuf.converter.mapper.ProtobufJsMapper
import kim.jade.kotlinx.protobuf.converter.protobufjs.ProtobufJsMessage
import kim.jade.kotlinx.protobuf.converter.protobufjs.structuralJson
import kim.jade.kotlinx.protobuf.type.ProtobufMessage
import kotlinx.serialization.modules.SerializersModule

class JsonDecoder(
    val json: String,
    serializersModule: SerializersModule,
) : ProtobufConverterDecoder(serializersModule) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ProtobufMessage> deserialize(converter: ProtobufConverter<T>): T {
        if (converter !is ProtobufJsMapper<*, *>) {
            throw IllegalArgumentException("Only acceptable ProtobufJsMapper")
        }

        val mapper = converter as ProtobufJsMapper<T, ProtobufJsMessage>
        val type = mapper.protobufJsFile.lookupType(mapper.typeName)
        val structural = structuralJson(type, JSON.parse(json))

        return mapper.convert(type.fromObject(structural).unsafeCast<ProtobufJsMessage>())
    }
}
