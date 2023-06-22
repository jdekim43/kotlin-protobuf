// Transform from google/protobuf/source_context.proto
@file:ProtobufSyntax(syntax = "PROTO3")
@file:GeneratorVersion(version = "0.2.3")

package google.protobuf

import kotlin.String
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
import kr.jadekim.protobuf.kotlinx.ProtobufConverterDecoder
import kr.jadekim.protobuf.type.ProtobufMessage

@Serializable(with = SourceContext.KotlinxSerializer::class)
@SerialName(value = SourceContext.TYPE_URL)
public data class SourceContext(
  @ProtobufIndex(index = 1)
  public val fileName: String,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.SourceContext"
  }

  public object KotlinxSerializer : KSerializer<SourceContext> {
    private val delegator: KSerializer<SourceContext> = SourceContext.serializer()

    public override val descriptor: SerialDescriptor = delegator.descriptor

    public override fun serialize(encoder: Encoder, `value`: SourceContext): Unit {
      if (encoder is ProtobufConverterEncoder) {
        encoder.encodeValue(SourceContextConverter.serialize(value))
        return
      }
      delegator.serialize(encoder, value)
    }

    public override fun deserialize(decoder: Decoder): SourceContext {
      if (decoder is ProtobufConverterDecoder) {
        return SourceContextConverter.deserialize(decoder.decodeByteArray())
      }
      return delegator.deserialize(decoder)
    }
  }
}
