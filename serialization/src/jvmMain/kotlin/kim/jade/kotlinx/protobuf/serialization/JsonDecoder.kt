package kim.jade.kotlinx.protobuf.serialization

import com.google.protobuf.Message
import com.google.protobuf.util.JsonFormat.Parser
import kim.jade.kotlinx.protobuf.converter.ProtobufConverter
import kim.jade.kotlinx.protobuf.converter.mapper.ProtobufTypeMapper
import kim.jade.kotlinx.protobuf.type.ProtobufMessage
import kotlinx.serialization.modules.SerializersModule

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