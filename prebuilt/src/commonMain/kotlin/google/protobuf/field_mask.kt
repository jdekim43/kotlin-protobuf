// Transform from google/protobuf/field_mask.proto
@file:GeneratorVersion(version = "0.5.2")

package google.protobuf

import kotlin.String
import kotlin.collections.List
import kotlin.collections.emptyList
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.`annotation`.ProtobufMessage as AnnotationProtobufMessage
import kr.jadekim.protobuf.type.ProtobufMessage as TypeProtobufMessage

@AnnotationProtobufMessage(typeUrl = FieldMask.TYPE_URL)
public data class FieldMask(
  @ProtobufIndex(index = 1)
  public val paths: List<String> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FieldMask"
  }
}
