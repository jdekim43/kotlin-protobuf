// Transform from google/protobuf/timestamp.proto
@file:ProtobufSyntax(syntax = "PROTO3")
@file:GeneratorVersion(version = "0.3.2")

package google.protobuf

import kotlin.Int
import kotlin.Long
import kotlin.String
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.`annotation`.ProtobufSyntax
import kr.jadekim.protobuf.type.ProtobufMessage

public data class Timestamp(
  @ProtobufIndex(index = 1)
  public val seconds: Long,
  @ProtobufIndex(index = 2)
  public val nanos: Int,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Timestamp"
  }
}
