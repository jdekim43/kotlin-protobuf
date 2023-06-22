// Transform from google/protobuf/api.proto
@file:ProtobufSyntax(syntax = "PROTO3")
@file:GeneratorVersion(version = "0.3.2")

package google.protobuf

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.Unit
import kotlin.collections.List
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

@Serializable(with = Api.KotlinxSerializer::class)
@SerialName(value = Api.TYPE_URL)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Api::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Api> {
    private val delegator: KSerializer<Api> = ReflectSerializer

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: Api): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(ApiConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): Api {
      if (decoder is ProtobufConverterDecoder) {
        return ApiConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}

@Serializable(with = Method.KotlinxSerializer::class)
@SerialName(value = Method.TYPE_URL)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Method::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Method> {
    private val delegator: KSerializer<Method> = ReflectSerializer

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: Method): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(MethodConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): Method {
      if (decoder is ProtobufConverterDecoder) {
        return MethodConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}

@Serializable(with = Mixin.KotlinxSerializer::class)
@SerialName(value = Mixin.TYPE_URL)
public data class Mixin(
  @ProtobufIndex(index = 1)
  public val name: String,
  @ProtobufIndex(index = 2)
  public val root: String,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Mixin"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Mixin::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Mixin> {
    private val delegator: KSerializer<Mixin> = ReflectSerializer

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: Mixin): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(MixinConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): Mixin {
      if (decoder is ProtobufConverterDecoder) {
        return MixinConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}
