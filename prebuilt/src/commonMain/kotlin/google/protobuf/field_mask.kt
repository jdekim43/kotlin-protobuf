// Transform from google/protobuf/field_mask.proto
@file:ProtobufSyntax(syntax = "PROTO3")
@file:GeneratorVersion(version = "0.3.2")

package google.protobuf

import kotlin.String
import kotlin.collections.List
import kotlin.collections.emptyList
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.`annotation`.ProtobufSyntax
import kr.jadekim.protobuf.type.ProtobufMessage

public data class FieldMask(
  @ProtobufIndex(index = 1)
  public val paths: List<String> = emptyList(),
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FieldMask"
  }
}
