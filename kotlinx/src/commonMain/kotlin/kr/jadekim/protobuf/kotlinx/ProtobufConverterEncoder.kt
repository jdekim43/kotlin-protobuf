package kr.jadekim.protobuf.kotlinx

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.modules.SerializersModule
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.type.ProtobufMessage

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