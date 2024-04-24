// Transform from google/protobuf/api.proto
@file:GeneratorVersion(version = "0.4.0")

package google.protobuf

import kotlin.Boolean
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

@AnnotationProtobufMessage(typeUrl = Api.TYPE_URL)
@Serializable(with = Api.KotlinxSerializer::class)
@SerialName(value = Api.TYPE_URL)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Api::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Api> {
    private val delegator: KSerializer<Api> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: Api) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(ApiConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Api {
      if (decoder is ProtobufConverterDecoder) {
        return ApiConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = Method.TYPE_URL)
@Serializable(with = Method.KotlinxSerializer::class)
@SerialName(value = Method.TYPE_URL)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Method::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Method> {
    private val delegator: KSerializer<Method> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: Method) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(MethodConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Method {
      if (decoder is ProtobufConverterDecoder) {
        return MethodConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = Mixin.TYPE_URL)
@Serializable(with = Mixin.KotlinxSerializer::class)
@SerialName(value = Mixin.TYPE_URL)
public data class Mixin(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val root: String = "",
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Mixin"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Mixin::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Mixin> {
    private val delegator: KSerializer<Mixin> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: Mixin) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(MixinConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Mixin {
      if (decoder is ProtobufConverterDecoder) {
        return MixinConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}
