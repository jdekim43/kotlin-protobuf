// Transform from google/protobuf/any.proto
@file:GeneratorVersion(version = "0.5.1")

package google.protobuf

import kotlin.ByteArray
import kotlin.OptIn
import kotlin.String
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

@AnnotationProtobufMessage(typeUrl = Any.TYPE_URL)
@Serializable(with = Any.KotlinxSerializer::class)
@SerialName(value = Any.TYPE_URL)
public data class Any(
  @ProtobufIndex(index = 1)
  public val typeUrl: String = "",
  @ProtobufIndex(index = 2)
  public val `value`: ByteArray = byteArrayOf(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Any"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Any::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Any> {
    private val delegator: KSerializer<Any> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: Any) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(AnyConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Any {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(AnyConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}
