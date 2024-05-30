// Transform from google/protobuf/any.proto
@file:GeneratorVersion(version = "0.5.1")

package google.protobuf

import kotlin.ByteArray
import kotlin.String
import kotlin.byteArrayOf
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.`annotation`.ProtobufMessage as AnnotationProtobufMessage
import kr.jadekim.protobuf.type.ProtobufMessage as TypeProtobufMessage

@AnnotationProtobufMessage(typeUrl = Any.TYPE_URL)
public data class Any(
  @ProtobufIndex(index = 1)
  public val typeUrl: String = "",
  @ProtobufIndex(index = 2)
  public val `value`: ByteArray = byteArrayOf(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Any"
  }
}
