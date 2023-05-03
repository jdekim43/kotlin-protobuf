package kr.jadekim.protobuf.converter

interface ProtobufConverter<KotlinType> {

    fun serialize(obj: KotlinType): ByteArray

    fun deserialize(bytes: ByteArray): KotlinType

    fun KotlinType.toByteArray(): ByteArray = serialize(this)
}
