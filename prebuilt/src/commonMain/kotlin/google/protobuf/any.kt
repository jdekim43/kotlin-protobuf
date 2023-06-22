// Transform from google/protobuf/any.proto
@file:ProtobufSyntax(syntax = "PROTO3")
@file:GeneratorVersion(version = "0.3.2")

package google.protobuf

import kotlin.ByteArray
import kotlin.String
import kotlin.byteArrayOf
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.`annotation`.ProtobufSyntax
import kr.jadekim.protobuf.type.ProtobufMessage

public data class Any(
  @ProtobufIndex(index = 1)
  public val typeUrl: String = "",
  @ProtobufIndex(index = 2)
  public val `value`: ByteArray = byteArrayOf(),
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Any"
  }
}
