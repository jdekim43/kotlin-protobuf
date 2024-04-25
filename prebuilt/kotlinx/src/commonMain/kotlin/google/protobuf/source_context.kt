// Transform from google/protobuf/source_context.proto
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
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.kotlinx.ProtobufConverterDecoder
import kr.jadekim.protobuf.kotlinx.ProtobufConverterEncoder
import kr.jadekim.protobuf.`annotation`.ProtobufMessage as AnnotationProtobufMessage
import kr.jadekim.protobuf.type.ProtobufMessage as TypeProtobufMessage

@AnnotationProtobufMessage(typeUrl = SourceContext.TYPE_URL)
@Serializable(with = SourceContext.KotlinxSerializer::class)
@SerialName(value = SourceContext.TYPE_URL)
public data class SourceContext(
  @ProtobufIndex(index = 1)
  public val fileName: String = "",
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.SourceContext"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = SourceContext::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<SourceContext> {
    private val delegator: KSerializer<SourceContext> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: SourceContext) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(SourceContextConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): SourceContext {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(SourceContextConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}
