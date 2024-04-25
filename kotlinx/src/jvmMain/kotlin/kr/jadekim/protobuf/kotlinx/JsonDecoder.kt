package kr.jadekim.protobuf.kotlinx

import com.google.protobuf.Message
import com.google.protobuf.util.JsonFormat.Parser
import kotlinx.serialization.modules.SerializersModule
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper
import kr.jadekim.protobuf.type.ProtobufMessage

class JsonDecoder(
    val json: String,
    private val parser: Parser,
    serializersModule: SerializersModule,
) : ProtobufConverterDecoder(serializersModule) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ProtobufMessage> deserialize(converter: ProtobufConverter<T>): T {
        if (converter !is ProtobufTypeMapper<*, *>) {
            throw IllegalArgumentException("Only acceptable ProtobufTypeMapper")
        }

        val mapper = converter as ProtobufTypeMapper<T, Message>
        val builder = mapper.default.newBuilderForType()
        parser.merge(json, builder)

        return mapper.convert(builder.build())
    }
}