// Transform from google/protobuf/duration.proto
@file:ProtobufSyntax(syntax = "PROTO3")
@file:GeneratorVersion(version = "0.3.2")

package google.protobuf

import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.Unit
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

@Serializable(with = Duration.KotlinxSerializer::class)
@SerialName(value = Duration.TYPE_URL)
public data class Duration(
  @ProtobufIndex(index = 1)
  public val seconds: Long,
  @ProtobufIndex(index = 2)
  public val nanos: Int,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Duration"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Duration::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Duration> {
    private val delegator: KSerializer<Duration> = ReflectSerializer

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: Duration): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(DurationConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): Duration {
      if (decoder is ProtobufConverterDecoder) {
        return DurationConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}
