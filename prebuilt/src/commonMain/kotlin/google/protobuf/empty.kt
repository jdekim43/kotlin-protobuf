// Transform from google/protobuf/empty.proto
@file:GeneratorVersion(version = "0.5.1")

package google.protobuf

import kotlin.String
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufMessage as AnnotationProtobufMessage
import kr.jadekim.protobuf.type.ProtobufMessage as TypeProtobufMessage

@AnnotationProtobufMessage(typeUrl = Empty.TYPE_URL)
public class Empty() : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Empty"
  }
}
