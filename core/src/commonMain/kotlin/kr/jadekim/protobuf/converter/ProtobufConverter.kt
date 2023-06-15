package kr.jadekim.protobuf.converter

import kr.jadekim.protobuf.type.ProtobufMessage

interface ProtobufConverter<KotlinType : ProtobufMessage> {

    fun serialize(obj: KotlinType): ByteArray

    fun deserialize(bytes: ByteArray): KotlinType

    fun KotlinType.toByteArray(): ByteArray = serialize(this)
}

fun <M : ProtobufMessage> ByteArray.parseProtobuf(mapper: ProtobufConverter<M>): M = mapper.deserialize(this)
