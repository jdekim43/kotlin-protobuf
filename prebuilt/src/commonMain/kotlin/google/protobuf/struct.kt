// Transform from google/protobuf/struct.proto
@file:ProtobufSyntax(syntax = "PROTO3")
@file:GeneratorVersion(version = "0.3.2")

package google.protobuf

import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.emptyList
import kotlin.collections.emptyMap
import kotlin.jvm.JvmInline
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.`annotation`.ProtobufSyntax
import kr.jadekim.protobuf.type.ProtobufMessage

public enum class NullValue(
  public val number: Int,
) {
  @ProtobufIndex(index = 0)
  NULL_VALUE(0),
  ;

  public companion object {
    public fun forNumber(number: Int): NullValue = NullValue.values()
    	.first { it.number == number }
  }
}

public data class Struct(
  @ProtobufIndex(index = 1)
  public val fields: Map<String, Value> = emptyMap(),
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Struct"
  }
}

public data class Value(
  public val kind: KindOneOf,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Value"
  }

  public sealed interface KindOneOf {
    @JvmInline
    public value class NullValue(
      @ProtobufIndex(index = 1)
      public val `value`: google.protobuf.NullValue = google.protobuf.NullValue.values()[0],
    ) : KindOneOf

    @JvmInline
    public value class NumberValue(
      @ProtobufIndex(index = 2)
      public val `value`: Double = 0.0,
    ) : KindOneOf

    @JvmInline
    public value class StringValue(
      @ProtobufIndex(index = 3)
      public val `value`: String = "",
    ) : KindOneOf

    @JvmInline
    public value class BoolValue(
      @ProtobufIndex(index = 4)
      public val `value`: Boolean = false,
    ) : KindOneOf

    @JvmInline
    public value class StructValue(
      @ProtobufIndex(index = 5)
      public val `value`: Struct = Struct(),
    ) : KindOneOf

    @JvmInline
    public value class ListValue(
      @ProtobufIndex(index = 6)
      public val `value`: google.protobuf.ListValue = google.protobuf.ListValue(),
    ) : KindOneOf
  }
}

public data class ListValue(
  @ProtobufIndex(index = 1)
  public val values: List<Value> = emptyList(),
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.ListValue"
  }
}
