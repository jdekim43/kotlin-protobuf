// Transform from google/protobuf/wrappers.proto
@file:GeneratorVersion(version = "0.5.1")

package google.protobuf

import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.UInt
import kotlin.ULong
import kotlin.byteArrayOf
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

@AnnotationProtobufMessage(typeUrl = DoubleValue.TYPE_URL)
@Serializable(with = DoubleValue.KotlinxSerializer::class)
@SerialName(value = DoubleValue.TYPE_URL)
public data class DoubleValue(
  @ProtobufIndex(index = 1)
  public val `value`: Double = 0.0,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.DoubleValue"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = DoubleValue::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<DoubleValue> {
    private val delegator: KSerializer<DoubleValue> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: DoubleValue) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(DoubleValueConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): DoubleValue {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(DoubleValueConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = FloatValue.TYPE_URL)
@Serializable(with = FloatValue.KotlinxSerializer::class)
@SerialName(value = FloatValue.TYPE_URL)
public data class FloatValue(
  @ProtobufIndex(index = 1)
  public val `value`: Float = 0.0f,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FloatValue"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = FloatValue::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<FloatValue> {
    private val delegator: KSerializer<FloatValue> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: FloatValue) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(FloatValueConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): FloatValue {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(FloatValueConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = Int64Value.TYPE_URL)
@Serializable(with = Int64Value.KotlinxSerializer::class)
@SerialName(value = Int64Value.TYPE_URL)
public data class Int64Value(
  @ProtobufIndex(index = 1)
  public val `value`: Long = 0L,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Int64Value"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Int64Value::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Int64Value> {
    private val delegator: KSerializer<Int64Value> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: Int64Value) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(Int64ValueConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Int64Value {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(Int64ValueConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = UInt64Value.TYPE_URL)
@Serializable(with = UInt64Value.KotlinxSerializer::class)
@SerialName(value = UInt64Value.TYPE_URL)
public data class UInt64Value(
  @ProtobufIndex(index = 1)
  public val `value`: ULong = 0uL,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.UInt64Value"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = UInt64Value::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<UInt64Value> {
    private val delegator: KSerializer<UInt64Value> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: UInt64Value) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(UInt64ValueConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): UInt64Value {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(UInt64ValueConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = Int32Value.TYPE_URL)
@Serializable(with = Int32Value.KotlinxSerializer::class)
@SerialName(value = Int32Value.TYPE_URL)
public data class Int32Value(
  @ProtobufIndex(index = 1)
  public val `value`: Int = 0,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Int32Value"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Int32Value::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Int32Value> {
    private val delegator: KSerializer<Int32Value> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: Int32Value) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(Int32ValueConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Int32Value {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(Int32ValueConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = UInt32Value.TYPE_URL)
@Serializable(with = UInt32Value.KotlinxSerializer::class)
@SerialName(value = UInt32Value.TYPE_URL)
public data class UInt32Value(
  @ProtobufIndex(index = 1)
  public val `value`: UInt = 0u,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.UInt32Value"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = UInt32Value::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<UInt32Value> {
    private val delegator: KSerializer<UInt32Value> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: UInt32Value) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(UInt32ValueConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): UInt32Value {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(UInt32ValueConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = BoolValue.TYPE_URL)
@Serializable(with = BoolValue.KotlinxSerializer::class)
@SerialName(value = BoolValue.TYPE_URL)
public data class BoolValue(
  @ProtobufIndex(index = 1)
  public val `value`: Boolean = false,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.BoolValue"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = BoolValue::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<BoolValue> {
    private val delegator: KSerializer<BoolValue> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: BoolValue) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(BoolValueConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): BoolValue {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(BoolValueConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = StringValue.TYPE_URL)
@Serializable(with = StringValue.KotlinxSerializer::class)
@SerialName(value = StringValue.TYPE_URL)
public data class StringValue(
  @ProtobufIndex(index = 1)
  public val `value`: String = "",
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.StringValue"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = StringValue::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<StringValue> {
    private val delegator: KSerializer<StringValue> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: StringValue) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(StringValueConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): StringValue {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(StringValueConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = BytesValue.TYPE_URL)
@Serializable(with = BytesValue.KotlinxSerializer::class)
@SerialName(value = BytesValue.TYPE_URL)
public data class BytesValue(
  @ProtobufIndex(index = 1)
  public val `value`: ByteArray = byteArrayOf(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.BytesValue"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = BytesValue::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<BytesValue> {
    private val delegator: KSerializer<BytesValue> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: BytesValue) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(BytesValueConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): BytesValue {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(BytesValueConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}
