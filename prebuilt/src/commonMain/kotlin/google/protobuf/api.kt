// Transform from google/protobuf/api.proto
@file:ProtobufSyntax(syntax = "PROTO3")
@file:GeneratorVersion(version = "0.2.3")

package google.protobuf

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.`annotation`.ProtobufSyntax
import kr.jadekim.protobuf.type.ProtobufMessage

public data class Api(
  @ProtobufIndex(index = 1)
  public val name: String,
  @ProtobufIndex(index = 2)
  public val methods: List<Method>,
  @ProtobufIndex(index = 3)
  public val options: List<Option>,
  @ProtobufIndex(index = 4)
  public val version: String,
  @ProtobufIndex(index = 5)
  public val sourceContext: SourceContext,
  @ProtobufIndex(index = 6)
  public val mixins: List<Mixin>,
  @ProtobufIndex(index = 7)
  public val syntax: Syntax,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Api"
  }
}

public data class Method(
  @ProtobufIndex(index = 1)
  public val name: String,
  @ProtobufIndex(index = 2)
  public val requestTypeUrl: String,
  @ProtobufIndex(index = 3)
  public val requestStreaming: Boolean,
  @ProtobufIndex(index = 4)
  public val responseTypeUrl: String,
  @ProtobufIndex(index = 5)
  public val responseStreaming: Boolean,
  @ProtobufIndex(index = 6)
  public val options: List<Option>,
  @ProtobufIndex(index = 7)
  public val syntax: Syntax,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Method"
  }
}

public data class Mixin(
  @ProtobufIndex(index = 1)
  public val name: String,
  @ProtobufIndex(index = 2)
  public val root: String,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Mixin"
  }
}
