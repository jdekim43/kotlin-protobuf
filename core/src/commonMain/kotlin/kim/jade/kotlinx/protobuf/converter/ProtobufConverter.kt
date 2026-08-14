package kim.jade.kotlinx.protobuf.converter

import kim.jade.kotlinx.protobuf.type.ProtobufMessage

interface ProtobufConverter<KotlinType : ProtobufMessage> {

    fun serialize(obj: KotlinType): ByteArray

    fun deserialize(bytes: ByteArray): KotlinType

    fun KotlinType.toByteArray(): ByteArray = serialize(this)
}

fun <M : ProtobufMessage> ByteArray.parseProtobuf(converter: ProtobufConverter<M>): M = converter.deserialize(this)
