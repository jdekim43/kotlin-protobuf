// Transform from google/protobuf/field_mask.proto
@file:GeneratorVersion(version = "0.5.1")

package google.protobuf

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

@AnnotationProtobufMessage(typeUrl = FieldMask.TYPE_URL)
@Serializable(with = FieldMask.KotlinxSerializer::class)
@SerialName(value = FieldMask.TYPE_URL)
public data class FieldMask(
  @ProtobufIndex(index = 1)
  public val paths: List<String> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FieldMask"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = FieldMask::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<FieldMask> {
    private val delegator: KSerializer<FieldMask> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: FieldMask) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(FieldMaskConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): FieldMask {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(FieldMaskConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}
