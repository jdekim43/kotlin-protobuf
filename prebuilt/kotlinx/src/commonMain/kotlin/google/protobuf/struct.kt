// Transform from google/protobuf/struct.proto
@file:ProtobufSyntax(syntax = "PROTO3")
@file:GeneratorVersion(version = "0.3.2")

package google.protobuf

import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.jvm.JvmInline
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
import kr.jadekim.protobuf.`annotation`.ProtobufSyntax
import kr.jadekim.protobuf.kotlinx.ProtobufConverterDecoder
import kr.jadekim.protobuf.kotlinx.ProtobufConverterEncoder
import kr.jadekim.protobuf.type.ProtobufMessage

@Serializable
@SerialName(value = "type.googleapis.com/google.protobuf.NullValue")
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

@Serializable(with = Struct.KotlinxSerializer::class)
@SerialName(value = Struct.TYPE_URL)
public data class Struct(
  @ProtobufIndex(index = 1)
  public val fields: Map<String, Value>,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Struct"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Struct::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Struct> {
    private val delegator: KSerializer<Struct> = ReflectSerializer

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: Struct): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(StructConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): Struct {
      if (decoder is ProtobufConverterDecoder) {
        return StructConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}

@Serializable(with = Value.KotlinxSerializer::class)
@SerialName(value = Value.TYPE_URL)
public data class Value(
  public val kind: KindOneOf,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Value"
  }

  @Serializable
  public sealed interface KindOneOf {
    @JvmInline
    public value class NullValue(
      @ProtobufIndex(index = 1)
      public val `value`: google.protobuf.NullValue,
    ) : KindOneOf

    @JvmInline
    public value class NumberValue(
      @ProtobufIndex(index = 2)
      public val `value`: Double,
    ) : KindOneOf

    @JvmInline
    public value class StringValue(
      @ProtobufIndex(index = 3)
      public val `value`: String,
    ) : KindOneOf

    @JvmInline
    public value class BoolValue(
      @ProtobufIndex(index = 4)
      public val `value`: Boolean,
    ) : KindOneOf

    @JvmInline
    public value class StructValue(
      @ProtobufIndex(index = 5)
      public val `value`: Struct,
    ) : KindOneOf

    @JvmInline
    public value class ListValue(
      @ProtobufIndex(index = 6)
      public val `value`: google.protobuf.ListValue,
    ) : KindOneOf
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Value::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Value> {
    private val delegator: KSerializer<Value> = ReflectSerializer

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: Value): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(ValueConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): Value {
      if (decoder is ProtobufConverterDecoder) {
        return ValueConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}

@Serializable(with = ListValue.KotlinxSerializer::class)
@SerialName(value = ListValue.TYPE_URL)
public data class ListValue(
  @ProtobufIndex(index = 1)
  public val values: List<Value>,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.ListValue"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = ListValue::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<ListValue> {
    private val delegator: KSerializer<ListValue> = ReflectSerializer

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: ListValue): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(ListValueConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): ListValue {
      if (decoder is ProtobufConverterDecoder) {
        return ListValueConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}
