// Transform from google/protobuf/source_context.proto
@file:GeneratorVersion(version = "0.4.0")

package google.protobuf

import kotlin.String
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.`annotation`.ProtobufMessage as AnnotationProtobufMessage
import kr.jadekim.protobuf.type.ProtobufMessage as TypeProtobufMessage

@AnnotationProtobufMessage(typeUrl = SourceContext.TYPE_URL)
public data class SourceContext(
  @ProtobufIndex(index = 1)
  public val fileName: String = "",
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.SourceContext"
  }
}
