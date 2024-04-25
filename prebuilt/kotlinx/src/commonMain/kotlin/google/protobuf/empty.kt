// Transform from google/protobuf/empty.proto
@file:GeneratorVersion(version = "0.4.0")

package google.protobuf

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
import kr.jadekim.protobuf.kotlinx.ProtobufConverterDecoder
import kr.jadekim.protobuf.kotlinx.ProtobufConverterEncoder
import kr.jadekim.protobuf.`annotation`.ProtobufMessage as AnnotationProtobufMessage
import kr.jadekim.protobuf.type.ProtobufMessage as TypeProtobufMessage

@AnnotationProtobufMessage(typeUrl = Empty.TYPE_URL)
@Serializable(with = Empty.KotlinxSerializer::class)
@SerialName(value = Empty.TYPE_URL)
public class Empty() : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Empty"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = Empty::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<Empty> {
    private val delegator: KSerializer<Empty> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: Empty) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(EmptyConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Empty {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(EmptyConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}
