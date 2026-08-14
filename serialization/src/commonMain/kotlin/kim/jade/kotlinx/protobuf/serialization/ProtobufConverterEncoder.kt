package kim.jade.kotlinx.protobuf.serialization

import kim.jade.kotlinx.protobuf.converter.ProtobufConverter
import kim.jade.kotlinx.protobuf.type.ProtobufMessage
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
abstract class ProtobufConverterEncoder(
    override val serializersModule: SerializersModule,
) : AbstractEncoder() {

    abstract fun <T : ProtobufMessage> serialize(converter: ProtobufConverter<T>, value: T)
}

class ProtobufEncoder(serializersModule: SerializersModule) : ProtobufConverterEncoder(serializersModule) {
    var result: ByteArray = byteArrayOf()
        private set

    override fun <T : ProtobufMessage> serialize(converter: ProtobufConverter<T>, value: T) {
        result = converter.serialize(value)
    }
}