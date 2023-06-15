// Transform from google/protobuf/wrappers.proto
@file:ProtobufSyntax(syntax = "PROTO3")
@file:GeneratorVersion(version = "0.2.3")

package google.protobuf

import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.UInt
import kotlin.ULong
import kotlin.Unit
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.`annotation`.ProtobufSyntax
import kr.jadekim.protobuf.kotlinx.ProtobufConverterEncoder
import kr.jadekim.protobuf.kotlinx.ProtobufMapperDecoder
import kr.jadekim.protobuf.type.ProtobufMessage

@Serializable(with = DoubleValue.KotlinxSerializer::class)
@SerialName(value = DoubleValue.TYPE_URL)
public data class DoubleValue(
  @ProtobufIndex(index = 1)
  public val `value`: Double,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.DoubleValue"
  }

  public object KotlinxSerializer : KSerializer<DoubleValue> {
    private val delegator: KSerializer<DoubleValue> = DoubleValue.serializer()

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: DoubleValue): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(DoubleValueConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): DoubleValue {
      if (decoder is ProtobufMapperDecoder) {
        return DoubleValueConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}

@Serializable(with = FloatValue.KotlinxSerializer::class)
@SerialName(value = FloatValue.TYPE_URL)
public data class FloatValue(
  @ProtobufIndex(index = 1)
  public val `value`: Float,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FloatValue"
  }

  public object KotlinxSerializer : KSerializer<FloatValue> {
    private val delegator: KSerializer<FloatValue> = FloatValue.serializer()

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: FloatValue): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(FloatValueConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): FloatValue {
      if (decoder is ProtobufMapperDecoder) {
        return FloatValueConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}

@Serializable(with = Int64Value.KotlinxSerializer::class)
@SerialName(value = Int64Value.TYPE_URL)
public data class Int64Value(
  @ProtobufIndex(index = 1)
  public val `value`: Long,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Int64Value"
  }

  public object KotlinxSerializer : KSerializer<Int64Value> {
    private val delegator: KSerializer<Int64Value> = Int64Value.serializer()

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: Int64Value): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(Int64ValueConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): Int64Value {
      if (decoder is ProtobufMapperDecoder) {
        return Int64ValueConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}

@Serializable(with = UInt64Value.KotlinxSerializer::class)
@SerialName(value = UInt64Value.TYPE_URL)
public data class UInt64Value(
  @ProtobufIndex(index = 1)
  public val `value`: ULong,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.UInt64Value"
  }

  public object KotlinxSerializer : KSerializer<UInt64Value> {
    private val delegator: KSerializer<UInt64Value> = UInt64Value.serializer()

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: UInt64Value): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(UInt64ValueConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): UInt64Value {
      if (decoder is ProtobufMapperDecoder) {
        return UInt64ValueConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}

@Serializable(with = Int32Value.KotlinxSerializer::class)
@SerialName(value = Int32Value.TYPE_URL)
public data class Int32Value(
  @ProtobufIndex(index = 1)
  public val `value`: Int,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Int32Value"
  }

  public object KotlinxSerializer : KSerializer<Int32Value> {
    private val delegator: KSerializer<Int32Value> = Int32Value.serializer()

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: Int32Value): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(Int32ValueConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): Int32Value {
      if (decoder is ProtobufMapperDecoder) {
        return Int32ValueConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}

@Serializable(with = UInt32Value.KotlinxSerializer::class)
@SerialName(value = UInt32Value.TYPE_URL)
public data class UInt32Value(
  @ProtobufIndex(index = 1)
  public val `value`: UInt,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.UInt32Value"
  }

  public object KotlinxSerializer : KSerializer<UInt32Value> {
    private val delegator: KSerializer<UInt32Value> = UInt32Value.serializer()

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: UInt32Value): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(UInt32ValueConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): UInt32Value {
      if (decoder is ProtobufMapperDecoder) {
        return UInt32ValueConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}

@Serializable(with = BoolValue.KotlinxSerializer::class)
@SerialName(value = BoolValue.TYPE_URL)
public data class BoolValue(
  @ProtobufIndex(index = 1)
  public val `value`: Boolean,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.BoolValue"
  }

  public object KotlinxSerializer : KSerializer<BoolValue> {
    private val delegator: KSerializer<BoolValue> = BoolValue.serializer()

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: BoolValue): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(BoolValueConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): BoolValue {
      if (decoder is ProtobufMapperDecoder) {
        return BoolValueConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}

@Serializable(with = StringValue.KotlinxSerializer::class)
@SerialName(value = StringValue.TYPE_URL)
public data class StringValue(
  @ProtobufIndex(index = 1)
  public val `value`: String,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.StringValue"
  }

  public object KotlinxSerializer : KSerializer<StringValue> {
    private val delegator: KSerializer<StringValue> = StringValue.serializer()

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: StringValue): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(StringValueConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): StringValue {
      if (decoder is ProtobufMapperDecoder) {
        return StringValueConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}

@Serializable(with = BytesValue.KotlinxSerializer::class)
@SerialName(value = BytesValue.TYPE_URL)
public data class BytesValue(
  @ProtobufIndex(index = 1)
  public val `value`: ByteArray,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.BytesValue"
  }

  public object KotlinxSerializer : KSerializer<BytesValue> {
    private val delegator: KSerializer<BytesValue> = BytesValue.serializer()

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: BytesValue): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(BytesValueConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): BytesValue {
      if (decoder is ProtobufMapperDecoder) {
        return BytesValueConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}
