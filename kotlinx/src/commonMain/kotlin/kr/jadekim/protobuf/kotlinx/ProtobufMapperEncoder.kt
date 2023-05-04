package kr.jadekim.protobuf.kotlinx

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
class ProtobufMapperEncoder(override val serializersModule: SerializersModule) : AbstractEncoder() {

    var result: ByteArray = byteArrayOf()
        private set

    fun encodeValue(value: ByteArray) {
        this.result = value
    }
}