package kr.jadekim.protobuf.type.special

import kr.jadekim.protobuf.annotation.ProtobufMessage
import kotlin.reflect.KClass

@ProtobufMessage(typeUrl = Any.TYPE_URL)
data class Any(
    val typeUrl: String,
    val value: kotlin.Any,
) : SpecialTypes {
    public companion object {
        public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Any"
    }
}

