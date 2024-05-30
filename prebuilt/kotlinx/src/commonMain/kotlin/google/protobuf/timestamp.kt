// Transform from google/protobuf/timestamp.proto
@file:GeneratorVersion(version = "0.5.1")

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

@AnnotationProtobufMessage(typeUrl = Timestamp.TYPE_URL)
@Serializable(with = Timestamp.KotlinxSerializer::class)
@SerialName(value = Timestamp.TYPE_URL)
public data class Timestamp(
  @ProtobufIndex(index = 1)
  public val seconds: Long = 0L,
  @ProtobufIndex(index = 2)
  public val nanos: Int = 0,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Timestamp"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Timestamp::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Timestamp> {
    private val delegator: KSerializer<Timestamp> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: Timestamp) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(TimestampConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Timestamp {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(TimestampConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}
