// Transform from google/protobuf/field_mask.proto
@file:ProtobufSyntax(syntax = "PROTO3")
@file:GeneratorVersion(version = "0.3.2")

package google.protobuf

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

@Serializable(with = FieldMask.KotlinxSerializer::class)
@SerialName(value = FieldMask.TYPE_URL)
public data class FieldMask(
  @ProtobufIndex(index = 1)
  public val paths: List<String>,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FieldMask"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = FieldMask::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<FieldMask> {
    private val delegator: KSerializer<FieldMask> = ReflectSerializer

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: FieldMask): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(FieldMaskConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): FieldMask {
      if (decoder is ProtobufConverterDecoder) {
        return FieldMaskConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}
