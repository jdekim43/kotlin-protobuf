// Transform from google/protobuf/duration.proto
@file:GeneratorVersion(version = "0.4.0")

package google.protobuf

import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
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

@AnnotationProtobufMessage(typeUrl = Duration.TYPE_URL)
@Serializable(with = Duration.KotlinxSerializer::class)
@SerialName(value = Duration.TYPE_URL)
public data class Duration(
  @ProtobufIndex(index = 1)
  public val seconds: Long = 0L,
  @ProtobufIndex(index = 2)
  public val nanos: Int = 0,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Duration"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Duration::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Duration> {
    private val delegator: KSerializer<Duration> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: Duration) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(DurationConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Duration {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(DurationConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}
