// Transform from google/protobuf/type.proto
@file:GeneratorVersion(version = "0.5.1")

package google.protobuf

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.emptyList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.kotlinx.ProtobufConverterDecoder
import kr.jadekim.protobuf.kotlinx.ProtobufConverterEncoder
import kr.jadekim.protobuf.`annotation`.ProtobufMessage as AnnotationProtobufMessage
import kr.jadekim.protobuf.type.ProtobufMessage as TypeProtobufMessage

@Serializable
@SerialName(value = "type.googleapis.com/google.protobuf.Syntax")
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
@Serializable(with = Type.KotlinxSerializer::class)
@SerialName(value = Type.TYPE_URL)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Type::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Type> {
    private val delegator: KSerializer<Type> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: Type) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(TypeConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Type {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(TypeConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = Field.TYPE_URL)
@Serializable(with = Field.KotlinxSerializer::class)
@SerialName(value = Field.TYPE_URL)
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

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.Field.Kind")
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

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.Field.Cardinality")
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Field::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Field> {
    private val delegator: KSerializer<Field> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: Field) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(FieldConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Field {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(FieldConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = Enum.TYPE_URL)
@Serializable(with = Enum.KotlinxSerializer::class)
@SerialName(value = Enum.TYPE_URL)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Enum::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Enum> {
    private val delegator: KSerializer<Enum> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: Enum) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(EnumConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Enum {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(EnumConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = EnumValue.TYPE_URL)
@Serializable(with = EnumValue.KotlinxSerializer::class)
@SerialName(value = EnumValue.TYPE_URL)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = EnumValue::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<EnumValue> {
    private val delegator: KSerializer<EnumValue> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: EnumValue) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(EnumValueConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): EnumValue {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(EnumValueConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = Option.TYPE_URL)
@Serializable(with = Option.KotlinxSerializer::class)
@SerialName(value = Option.TYPE_URL)
public data class Option(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val `value`: Any = Any(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Option"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Option::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Option> {
    private val delegator: KSerializer<Option> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: Option) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(OptionConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Option {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(OptionConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}
