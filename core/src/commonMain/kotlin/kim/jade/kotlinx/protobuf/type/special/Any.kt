package kim.jade.kotlinx.protobuf.type.special

import kim.jade.kotlinx.protobuf.annotation.ProtobufMessage

@ProtobufMessage(typeUrl = Any.TYPE_URL)
data class Any(
    val typeUrl: String,
    val value: kotlin.Any,
) : SpecialTypes {
    companion object {
        const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Any"
    }
}

