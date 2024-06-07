// Transform from google/protobuf/type.proto
@file:GeneratorVersion(version = "0.5.2")

package google.protobuf

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlin.collections.emptyList
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.`annotation`.ProtobufMessage as AnnotationProtobufMessage
import kr.jadekim.protobuf.type.ProtobufMessage as TypeProtobufMessage

public enum class Syntax(
  public val number: Int,
) {
  @ProtobufIndex(index = 0)
  SYNTAX_PROTO2(0),
  @ProtobufIndex(index = 1)
  SYNTAX_PROTO3(1),
  @ProtobufIndex(index = 2)
  SYNTAX_EDITIONS(2),
  ;

  public companion object {
    public fun forNumber(number: Int): Syntax = Syntax.values()
    	.first { it.number == number }
  }
}

@AnnotationProtobufMessage(typeUrl = Type.TYPE_URL)
public data class Type(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val fields: List<Field> = emptyList(),
  @ProtobufIndex(index = 3)
  public val oneofs: List<String> = emptyList(),
  @ProtobufIndex(index = 4)
  public val options: List<Option> = emptyList(),
  @ProtobufIndex(index = 5)
  public val sourceContext: SourceContext = SourceContext(),
  @ProtobufIndex(index = 6)
  public val syntax: Syntax = Syntax.values()[0],
  @ProtobufIndex(index = 7)
  public val edition: String = "",
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Type"
  }
}

@AnnotationProtobufMessage(typeUrl = Field.TYPE_URL)
public data class Field(
  @ProtobufIndex(index = 1)
  public val kind: Kind = Kind.values()[0],
  @ProtobufIndex(index = 2)
  public val cardinality: Cardinality = Cardinality.values()[0],
  @ProtobufIndex(index = 3)
  public val number: Int = 0,
  @ProtobufIndex(index = 4)
  public val name: String = "",
  @ProtobufIndex(index = 6)
  public val typeUrl: String = "",
  @ProtobufIndex(index = 7)
  public val oneofIndex: Int = 0,
  @ProtobufIndex(index = 8)
  public val packed: Boolean = false,
  @ProtobufIndex(index = 9)
  public val options: List<Option> = emptyList(),
  @ProtobufIndex(index = 10)
  public val jsonName: String = "",
  @ProtobufIndex(index = 11)
  public val defaultValue: String = "",
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Field"
  }

  public enum class Kind(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    TYPE_UNKNOWN(0),
    @ProtobufIndex(index = 1)
    TYPE_DOUBLE(1),
    @ProtobufIndex(index = 2)
    TYPE_FLOAT(2),
    @ProtobufIndex(index = 3)
    TYPE_INT64(3),
    @ProtobufIndex(index = 4)
    TYPE_UINT64(4),
    @ProtobufIndex(index = 5)
    TYPE_INT32(5),
    @ProtobufIndex(index = 6)
    TYPE_FIXED64(6),
    @ProtobufIndex(index = 7)
    TYPE_FIXED32(7),
    @ProtobufIndex(index = 8)
    TYPE_BOOL(8),
    @ProtobufIndex(index = 9)
    TYPE_STRING(9),
    @ProtobufIndex(index = 10)
    TYPE_GROUP(10),
    @ProtobufIndex(index = 11)
    TYPE_MESSAGE(11),
    @ProtobufIndex(index = 12)
    TYPE_BYTES(12),
    @ProtobufIndex(index = 13)
    TYPE_UINT32(13),
    @ProtobufIndex(index = 14)
    TYPE_ENUM(14),
    @ProtobufIndex(index = 15)
    TYPE_SFIXED32(15),
    @ProtobufIndex(index = 16)
    TYPE_SFIXED64(16),
    @ProtobufIndex(index = 17)
    TYPE_SINT32(17),
    @ProtobufIndex(index = 18)
    TYPE_SINT64(18),
    ;

    public companion object {
      public fun forNumber(number: Int): Kind = Kind.values()
      	.first { it.number == number }
    }
  }

  public enum class Cardinality(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    CARDINALITY_UNKNOWN(0),
    @ProtobufIndex(index = 1)
    CARDINALITY_OPTIONAL(1),
    @ProtobufIndex(index = 2)
    CARDINALITY_REQUIRED(2),
    @ProtobufIndex(index = 3)
    CARDINALITY_REPEATED(3),
    ;

    public companion object {
      public fun forNumber(number: Int): Cardinality = Cardinality.values()
      	.first { it.number == number }
    }
  }
}

@AnnotationProtobufMessage(typeUrl = Enum.TYPE_URL)
public data class Enum(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val enumvalue: List<EnumValue> = emptyList(),
  @ProtobufIndex(index = 3)
  public val options: List<Option> = emptyList(),
  @ProtobufIndex(index = 4)
  public val sourceContext: SourceContext = SourceContext(),
  @ProtobufIndex(index = 5)
  public val syntax: Syntax = Syntax.values()[0],
  @ProtobufIndex(index = 6)
  public val edition: String = "",
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Enum"
  }
}

@AnnotationProtobufMessage(typeUrl = EnumValue.TYPE_URL)
public data class EnumValue(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val number: Int = 0,
  @ProtobufIndex(index = 3)
  public val options: List<Option> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.EnumValue"
  }
}

@AnnotationProtobufMessage(typeUrl = Option.TYPE_URL)
public data class Option(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val `value`: Any = Any(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Option"
  }
}
