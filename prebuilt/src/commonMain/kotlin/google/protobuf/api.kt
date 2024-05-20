// Transform from google/protobuf/api.proto
@file:GeneratorVersion(version = "0.4.1")

package google.protobuf

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlin.collections.emptyList
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.`annotation`.ProtobufMessage as AnnotationProtobufMessage
import kr.jadekim.protobuf.type.ProtobufMessage as TypeProtobufMessage

@AnnotationProtobufMessage(typeUrl = Api.TYPE_URL)
public data class Api(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val methods: List<Method> = emptyList(),
  @ProtobufIndex(index = 3)
  public val options: List<Option> = emptyList(),
  @ProtobufIndex(index = 4)
  public val version: String = "",
  @ProtobufIndex(index = 5)
  public val sourceContext: SourceContext? = null,
  @ProtobufIndex(index = 6)
  public val mixins: List<Mixin> = emptyList(),
  @ProtobufIndex(index = 7)
  public val syntax: Syntax = Syntax.values()[0],
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Api"
  }
}

@AnnotationProtobufMessage(typeUrl = Method.TYPE_URL)
public data class Method(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val requestTypeUrl: String = "",
  @ProtobufIndex(index = 3)
  public val requestStreaming: Boolean = false,
  @ProtobufIndex(index = 4)
  public val responseTypeUrl: String = "",
  @ProtobufIndex(index = 5)
  public val responseStreaming: Boolean = false,
  @ProtobufIndex(index = 6)
  public val options: List<Option> = emptyList(),
  @ProtobufIndex(index = 7)
  public val syntax: Syntax = Syntax.values()[0],
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Method"
  }
}

@AnnotationProtobufMessage(typeUrl = Mixin.TYPE_URL)
public data class Mixin(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val root: String = "",
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Mixin"
  }
}
