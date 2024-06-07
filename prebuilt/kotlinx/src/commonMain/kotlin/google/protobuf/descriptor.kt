// Transform from google/protobuf/descriptor.proto
@file:GeneratorVersion(version = "0.5.2")

package google.protobuf

import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Deprecated
import kotlin.DeprecationLevel
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.ReplaceWith
import kotlin.String
import kotlin.ULong
import kotlin.byteArrayOf
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

@Serializable
@SerialName(value = "type.googleapis.com/google.protobuf.Edition")
public enum class Edition(
  public val number: Int,
) {
  @ProtobufIndex(index = 0)
  EDITION_UNKNOWN(0),
  @ProtobufIndex(index = 900)
  EDITION_LEGACY(900),
  @ProtobufIndex(index = 998)
  EDITION_PROTO2(998),
  @ProtobufIndex(index = 999)
  EDITION_PROTO3(999),
  @ProtobufIndex(index = 1_000)
  EDITION_2023(1_000),
  @ProtobufIndex(index = 1_001)
  EDITION_2024(1_001),
  @ProtobufIndex(index = 1)
  EDITION_1_TEST_ONLY(1),
  @ProtobufIndex(index = 2)
  EDITION_2_TEST_ONLY(2),
  @ProtobufIndex(index = 99_997)
  EDITION_99997_TEST_ONLY(99_997),
  @ProtobufIndex(index = 99_998)
  EDITION_99998_TEST_ONLY(99_998),
  @ProtobufIndex(index = 99_999)
  EDITION_99999_TEST_ONLY(99_999),
  @ProtobufIndex(index = 2_147_483_647)
  EDITION_MAX(2_147_483_647),
  ;

  public companion object {
    public fun forNumber(number: Int): Edition = Edition.values()
    	.first { it.number == number }
  }
}

@AnnotationProtobufMessage(typeUrl = FileDescriptorSet.TYPE_URL)
@Serializable(with = FileDescriptorSet.KotlinxSerializer::class)
@SerialName(value = FileDescriptorSet.TYPE_URL)
public data class FileDescriptorSet(
  @ProtobufIndex(index = 1)
  public val `file`: List<FileDescriptorProto> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FileDescriptorSet"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = FileDescriptorSet::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<FileDescriptorSet> {
    private val delegator: KSerializer<FileDescriptorSet> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: FileDescriptorSet) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(FileDescriptorSetConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): FileDescriptorSet {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(FileDescriptorSetConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = FileDescriptorProto.TYPE_URL)
@Serializable(with = FileDescriptorProto.KotlinxSerializer::class)
@SerialName(value = FileDescriptorProto.TYPE_URL)
public data class FileDescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val `package`: String = "",
  @ProtobufIndex(index = 3)
  public val dependency: List<String> = emptyList(),
  @ProtobufIndex(index = 10)
  public val publicDependency: List<Int> = emptyList(),
  @ProtobufIndex(index = 11)
  public val weakDependency: List<Int> = emptyList(),
  @ProtobufIndex(index = 4)
  public val messageType: List<DescriptorProto> = emptyList(),
  @ProtobufIndex(index = 5)
  public val enumType: List<EnumDescriptorProto> = emptyList(),
  @ProtobufIndex(index = 6)
  public val service: List<ServiceDescriptorProto> = emptyList(),
  @ProtobufIndex(index = 7)
  public val extension: List<FieldDescriptorProto> = emptyList(),
  @ProtobufIndex(index = 8)
  public val options: FileOptions = FileOptions(),
  @ProtobufIndex(index = 9)
  public val sourceCodeInfo: SourceCodeInfo = SourceCodeInfo(),
  @ProtobufIndex(index = 12)
  public val syntax: String = "",
  @ProtobufIndex(index = 14)
  public val edition: Edition = Edition.values()[0],
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FileDescriptorProto"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = FileDescriptorProto::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<FileDescriptorProto> {
    private val delegator: KSerializer<FileDescriptorProto> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: FileDescriptorProto) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(FileDescriptorProtoConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): FileDescriptorProto {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(FileDescriptorProtoConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = DescriptorProto.TYPE_URL)
@Serializable(with = DescriptorProto.KotlinxSerializer::class)
@SerialName(value = DescriptorProto.TYPE_URL)
public data class DescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val `field`: List<FieldDescriptorProto> = emptyList(),
  @ProtobufIndex(index = 6)
  public val extension: List<FieldDescriptorProto> = emptyList(),
  @ProtobufIndex(index = 3)
  public val nestedType: List<DescriptorProto> = emptyList(),
  @ProtobufIndex(index = 4)
  public val enumType: List<EnumDescriptorProto> = emptyList(),
  @ProtobufIndex(index = 5)
  public val extensionRange: List<ExtensionRange> = emptyList(),
  @ProtobufIndex(index = 8)
  public val oneofDecl: List<OneofDescriptorProto> = emptyList(),
  @ProtobufIndex(index = 7)
  public val options: MessageOptions = MessageOptions(),
  @ProtobufIndex(index = 9)
  public val reservedRange: List<ReservedRange> = emptyList(),
  @ProtobufIndex(index = 10)
  public val reservedName: List<String> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.DescriptorProto"
  }

  @AnnotationProtobufMessage(typeUrl = ExtensionRange.TYPE_URL)
  @Serializable(with = ExtensionRange.KotlinxSerializer::class)
  @SerialName(value = ExtensionRange.TYPE_URL)
  public data class ExtensionRange(
    @ProtobufIndex(index = 1)
    public val start: Int = 0,
    @ProtobufIndex(index = 2)
    public val end: Int = 0,
    @ProtobufIndex(index = 3)
    public val options: ExtensionRangeOptions = ExtensionRangeOptions(),
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.DescriptorProto.ExtensionRange"
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = ExtensionRange::class)
    private object ReflectSerializer

    public object KotlinxSerializer : KSerializer<ExtensionRange> {
      private val delegator: KSerializer<ExtensionRange> = ReflectSerializer

      override val descriptor: SerialDescriptor = delegator.descriptor

      override fun serialize(encoder: Encoder, `value`: ExtensionRange) {
        if (encoder is ProtobufConverterEncoder) {
          encoder.serialize(DescriptorProtoConverter.ExtensionRangeConverter, value)
          return
        }
        delegator.serialize(encoder, value)
      }

      override fun deserialize(decoder: Decoder): ExtensionRange {
        if (decoder is ProtobufConverterDecoder) {
          return decoder.deserialize(DescriptorProtoConverter.ExtensionRangeConverter)
        }
        return delegator.deserialize(decoder)
      }
    }
  }

  @AnnotationProtobufMessage(typeUrl = ReservedRange.TYPE_URL)
  @Serializable(with = ReservedRange.KotlinxSerializer::class)
  @SerialName(value = ReservedRange.TYPE_URL)
  public data class ReservedRange(
    @ProtobufIndex(index = 1)
    public val start: Int = 0,
    @ProtobufIndex(index = 2)
    public val end: Int = 0,
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.DescriptorProto.ReservedRange"
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = ReservedRange::class)
    private object ReflectSerializer

    public object KotlinxSerializer : KSerializer<ReservedRange> {
      private val delegator: KSerializer<ReservedRange> = ReflectSerializer

      override val descriptor: SerialDescriptor = delegator.descriptor

      override fun serialize(encoder: Encoder, `value`: ReservedRange) {
        if (encoder is ProtobufConverterEncoder) {
          encoder.serialize(DescriptorProtoConverter.ReservedRangeConverter, value)
          return
        }
        delegator.serialize(encoder, value)
      }

      override fun deserialize(decoder: Decoder): ReservedRange {
        if (decoder is ProtobufConverterDecoder) {
          return decoder.deserialize(DescriptorProtoConverter.ReservedRangeConverter)
        }
        return delegator.deserialize(decoder)
      }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = DescriptorProto::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<DescriptorProto> {
    private val delegator: KSerializer<DescriptorProto> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: DescriptorProto) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(DescriptorProtoConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): DescriptorProto {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(DescriptorProtoConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = ExtensionRangeOptions.TYPE_URL)
@Serializable(with = ExtensionRangeOptions.KotlinxSerializer::class)
@SerialName(value = ExtensionRangeOptions.TYPE_URL)
public data class ExtensionRangeOptions(
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
  @ProtobufIndex(index = 2)
  public val declaration: List<Declaration> = emptyList(),
  @ProtobufIndex(index = 50)
  public val features: FeatureSet = FeatureSet(),
  @ProtobufIndex(index = 3)
  public val verification: VerificationState = VerificationState.values()[0],
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.ExtensionRangeOptions"
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.ExtensionRangeOptions.VerificationState")
  public enum class VerificationState(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    DECLARATION(0),
    @ProtobufIndex(index = 1)
    UNVERIFIED(1),
    ;

    public companion object {
      public fun forNumber(number: Int): VerificationState = VerificationState.values()
      	.first { it.number == number }
    }
  }

  @AnnotationProtobufMessage(typeUrl = Declaration.TYPE_URL)
  @Serializable(with = Declaration.KotlinxSerializer::class)
  @SerialName(value = Declaration.TYPE_URL)
  public data class Declaration(
    @ProtobufIndex(index = 1)
    public val number: Int = 0,
    @ProtobufIndex(index = 2)
    public val fullName: String = "",
    @ProtobufIndex(index = 3)
    public val type: String = "",
    @ProtobufIndex(index = 5)
    public val reserved: Boolean = false,
    @ProtobufIndex(index = 6)
    public val repeated: Boolean = false,
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.ExtensionRangeOptions.Declaration"
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = Declaration::class)
    private object ReflectSerializer

    public object KotlinxSerializer : KSerializer<Declaration> {
      private val delegator: KSerializer<Declaration> = ReflectSerializer

      override val descriptor: SerialDescriptor = delegator.descriptor

      override fun serialize(encoder: Encoder, `value`: Declaration) {
        if (encoder is ProtobufConverterEncoder) {
          encoder.serialize(ExtensionRangeOptionsConverter.DeclarationConverter, value)
          return
        }
        delegator.serialize(encoder, value)
      }

      override fun deserialize(decoder: Decoder): Declaration {
        if (decoder is ProtobufConverterDecoder) {
          return decoder.deserialize(ExtensionRangeOptionsConverter.DeclarationConverter)
        }
        return delegator.deserialize(decoder)
      }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = ExtensionRangeOptions::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<ExtensionRangeOptions> {
    private val delegator: KSerializer<ExtensionRangeOptions> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: ExtensionRangeOptions) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(ExtensionRangeOptionsConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): ExtensionRangeOptions {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(ExtensionRangeOptionsConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = FieldDescriptorProto.TYPE_URL)
@Serializable(with = FieldDescriptorProto.KotlinxSerializer::class)
@SerialName(value = FieldDescriptorProto.TYPE_URL)
public data class FieldDescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 3)
  public val number: Int = 0,
  @ProtobufIndex(index = 4)
  public val label: Label = Label.values()[0],
  @ProtobufIndex(index = 5)
  public val type: Type = Type.values()[0],
  @ProtobufIndex(index = 6)
  public val typeName: String = "",
  @ProtobufIndex(index = 2)
  public val extendee: String = "",
  @ProtobufIndex(index = 7)
  public val defaultValue: String = "",
  @ProtobufIndex(index = 9)
  public val oneofIndex: Int = 0,
  @ProtobufIndex(index = 10)
  public val jsonName: String = "",
  @ProtobufIndex(index = 8)
  public val options: FieldOptions = FieldOptions(),
  @ProtobufIndex(index = 17)
  public val proto3Optional: Boolean = false,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FieldDescriptorProto"
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.FieldDescriptorProto.Type")
  public enum class Type(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 1)
    TYPE_DOUBLE(1),
    @ProtobufIndex(index = 2)
    TYPE_FLOAT(2),
    @ProtobufIndex(index = 3)
    TYPE_INT64(3),
    @ProtobufIndex(index = 4)
    TYPE_UINT64(4),
    @ProtobufIndex(index = 5)
    TYPE_INT32(5),
    @ProtobufIndex(index = 6)
    TYPE_FIXED64(6),
    @ProtobufIndex(index = 7)
    TYPE_FIXED32(7),
    @ProtobufIndex(index = 8)
    TYPE_BOOL(8),
    @ProtobufIndex(index = 9)
    TYPE_STRING(9),
    @ProtobufIndex(index = 10)
    TYPE_GROUP(10),
    @ProtobufIndex(index = 11)
    TYPE_MESSAGE(11),
    @ProtobufIndex(index = 12)
    TYPE_BYTES(12),
    @ProtobufIndex(index = 13)
    TYPE_UINT32(13),
    @ProtobufIndex(index = 14)
    TYPE_ENUM(14),
    @ProtobufIndex(index = 15)
    TYPE_SFIXED32(15),
    @ProtobufIndex(index = 16)
    TYPE_SFIXED64(16),
    @ProtobufIndex(index = 17)
    TYPE_SINT32(17),
    @ProtobufIndex(index = 18)
    TYPE_SINT64(18),
    ;

    public companion object {
      public fun forNumber(number: Int): Type = Type.values()
      	.first { it.number == number }
    }
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.FieldDescriptorProto.Label")
  public enum class Label(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 1)
    LABEL_OPTIONAL(1),
    @ProtobufIndex(index = 3)
    LABEL_REPEATED(3),
    @ProtobufIndex(index = 2)
    LABEL_REQUIRED(2),
    ;

    public companion object {
      public fun forNumber(number: Int): Label = Label.values()
      	.first { it.number == number }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = FieldDescriptorProto::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<FieldDescriptorProto> {
    private val delegator: KSerializer<FieldDescriptorProto> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: FieldDescriptorProto) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(FieldDescriptorProtoConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): FieldDescriptorProto {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(FieldDescriptorProtoConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = OneofDescriptorProto.TYPE_URL)
@Serializable(with = OneofDescriptorProto.KotlinxSerializer::class)
@SerialName(value = OneofDescriptorProto.TYPE_URL)
public data class OneofDescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val options: OneofOptions = OneofOptions(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.OneofDescriptorProto"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = OneofDescriptorProto::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<OneofDescriptorProto> {
    private val delegator: KSerializer<OneofDescriptorProto> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: OneofDescriptorProto) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(OneofDescriptorProtoConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): OneofDescriptorProto {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(OneofDescriptorProtoConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = EnumDescriptorProto.TYPE_URL)
@Serializable(with = EnumDescriptorProto.KotlinxSerializer::class)
@SerialName(value = EnumDescriptorProto.TYPE_URL)
public data class EnumDescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val `value`: List<EnumValueDescriptorProto> = emptyList(),
  @ProtobufIndex(index = 3)
  public val options: EnumOptions = EnumOptions(),
  @ProtobufIndex(index = 4)
  public val reservedRange: List<EnumReservedRange> = emptyList(),
  @ProtobufIndex(index = 5)
  public val reservedName: List<String> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.EnumDescriptorProto"
  }

  @AnnotationProtobufMessage(typeUrl = EnumReservedRange.TYPE_URL)
  @Serializable(with = EnumReservedRange.KotlinxSerializer::class)
  @SerialName(value = EnumReservedRange.TYPE_URL)
  public data class EnumReservedRange(
    @ProtobufIndex(index = 1)
    public val start: Int = 0,
    @ProtobufIndex(index = 2)
    public val end: Int = 0,
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.EnumDescriptorProto.EnumReservedRange"
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = EnumReservedRange::class)
    private object ReflectSerializer

    public object KotlinxSerializer : KSerializer<EnumReservedRange> {
      private val delegator: KSerializer<EnumReservedRange> = ReflectSerializer

      override val descriptor: SerialDescriptor = delegator.descriptor

      override fun serialize(encoder: Encoder, `value`: EnumReservedRange) {
        if (encoder is ProtobufConverterEncoder) {
          encoder.serialize(EnumDescriptorProtoConverter.EnumReservedRangeConverter, value)
          return
        }
        delegator.serialize(encoder, value)
      }

      override fun deserialize(decoder: Decoder): EnumReservedRange {
        if (decoder is ProtobufConverterDecoder) {
          return decoder.deserialize(EnumDescriptorProtoConverter.EnumReservedRangeConverter)
        }
        return delegator.deserialize(decoder)
      }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = EnumDescriptorProto::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<EnumDescriptorProto> {
    private val delegator: KSerializer<EnumDescriptorProto> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: EnumDescriptorProto) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(EnumDescriptorProtoConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): EnumDescriptorProto {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(EnumDescriptorProtoConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = EnumValueDescriptorProto.TYPE_URL)
@Serializable(with = EnumValueDescriptorProto.KotlinxSerializer::class)
@SerialName(value = EnumValueDescriptorProto.TYPE_URL)
public data class EnumValueDescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val number: Int = 0,
  @ProtobufIndex(index = 3)
  public val options: EnumValueOptions = EnumValueOptions(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String =
        "type.googleapis.com/google.protobuf.EnumValueDescriptorProto"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = EnumValueDescriptorProto::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<EnumValueDescriptorProto> {
    private val delegator: KSerializer<EnumValueDescriptorProto> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: EnumValueDescriptorProto) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(EnumValueDescriptorProtoConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): EnumValueDescriptorProto {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(EnumValueDescriptorProtoConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = ServiceDescriptorProto.TYPE_URL)
@Serializable(with = ServiceDescriptorProto.KotlinxSerializer::class)
@SerialName(value = ServiceDescriptorProto.TYPE_URL)
public data class ServiceDescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val method: List<MethodDescriptorProto> = emptyList(),
  @ProtobufIndex(index = 3)
  public val options: ServiceOptions = ServiceOptions(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.ServiceDescriptorProto"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = ServiceDescriptorProto::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<ServiceDescriptorProto> {
    private val delegator: KSerializer<ServiceDescriptorProto> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: ServiceDescriptorProto) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(ServiceDescriptorProtoConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): ServiceDescriptorProto {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(ServiceDescriptorProtoConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = MethodDescriptorProto.TYPE_URL)
@Serializable(with = MethodDescriptorProto.KotlinxSerializer::class)
@SerialName(value = MethodDescriptorProto.TYPE_URL)
public data class MethodDescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String = "",
  @ProtobufIndex(index = 2)
  public val inputType: String = "",
  @ProtobufIndex(index = 3)
  public val outputType: String = "",
  @ProtobufIndex(index = 4)
  public val options: MethodOptions = MethodOptions(),
  @ProtobufIndex(index = 5)
  public val clientStreaming: Boolean = false,
  @ProtobufIndex(index = 6)
  public val serverStreaming: Boolean = false,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.MethodDescriptorProto"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = MethodDescriptorProto::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<MethodDescriptorProto> {
    private val delegator: KSerializer<MethodDescriptorProto> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: MethodDescriptorProto) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(MethodDescriptorProtoConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): MethodDescriptorProto {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(MethodDescriptorProtoConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = FileOptions.TYPE_URL)
@Serializable(with = FileOptions.KotlinxSerializer::class)
@SerialName(value = FileOptions.TYPE_URL)
public data class FileOptions(
  @ProtobufIndex(index = 1)
  public val javaPackage: String = "",
  @ProtobufIndex(index = 8)
  public val javaOuterClassname: String = "",
  @ProtobufIndex(index = 10)
  public val javaMultipleFiles: Boolean = false,
  @Deprecated(
    message = "",
    replaceWith = ReplaceWith(""),
    level = DeprecationLevel.WARNING,
  )
  @ProtobufIndex(index = 20)
  public val javaGenerateEqualsAndHash: Boolean = false,
  @ProtobufIndex(index = 27)
  public val javaStringCheckUtf8: Boolean = false,
  @ProtobufIndex(index = 9)
  public val optimizeFor: OptimizeMode = OptimizeMode.values()[0],
  @ProtobufIndex(index = 11)
  public val goPackage: String = "",
  @ProtobufIndex(index = 16)
  public val ccGenericServices: Boolean = false,
  @ProtobufIndex(index = 17)
  public val javaGenericServices: Boolean = false,
  @ProtobufIndex(index = 18)
  public val pyGenericServices: Boolean = false,
  @ProtobufIndex(index = 23)
  public val deprecated: Boolean = false,
  @ProtobufIndex(index = 31)
  public val ccEnableArenas: Boolean = false,
  @ProtobufIndex(index = 36)
  public val objcClassPrefix: String = "",
  @ProtobufIndex(index = 37)
  public val csharpNamespace: String = "",
  @ProtobufIndex(index = 39)
  public val swiftPrefix: String = "",
  @ProtobufIndex(index = 40)
  public val phpClassPrefix: String = "",
  @ProtobufIndex(index = 41)
  public val phpNamespace: String = "",
  @ProtobufIndex(index = 44)
  public val phpMetadataNamespace: String = "",
  @ProtobufIndex(index = 45)
  public val rubyPackage: String = "",
  @ProtobufIndex(index = 50)
  public val features: FeatureSet = FeatureSet(),
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FileOptions"
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.FileOptions.OptimizeMode")
  public enum class OptimizeMode(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 1)
    SPEED(1),
    @ProtobufIndex(index = 2)
    CODE_SIZE(2),
    @ProtobufIndex(index = 3)
    LITE_RUNTIME(3),
    ;

    public companion object {
      public fun forNumber(number: Int): OptimizeMode = OptimizeMode.values()
      	.first { it.number == number }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = FileOptions::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<FileOptions> {
    private val delegator: KSerializer<FileOptions> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: FileOptions) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(FileOptionsConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): FileOptions {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(FileOptionsConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = MessageOptions.TYPE_URL)
@Serializable(with = MessageOptions.KotlinxSerializer::class)
@SerialName(value = MessageOptions.TYPE_URL)
public data class MessageOptions(
  @ProtobufIndex(index = 1)
  public val messageSetWireFormat: Boolean = false,
  @ProtobufIndex(index = 2)
  public val noStandardDescriptorAccessor: Boolean = false,
  @ProtobufIndex(index = 3)
  public val deprecated: Boolean = false,
  @ProtobufIndex(index = 7)
  public val mapEntry: Boolean = false,
  @Deprecated(
    message = "",
    replaceWith = ReplaceWith(""),
    level = DeprecationLevel.WARNING,
  )
  @ProtobufIndex(index = 11)
  public val deprecatedLegacyJsonFieldConflicts: Boolean = false,
  @ProtobufIndex(index = 12)
  public val features: FeatureSet = FeatureSet(),
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.MessageOptions"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = MessageOptions::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<MessageOptions> {
    private val delegator: KSerializer<MessageOptions> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: MessageOptions) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(MessageOptionsConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): MessageOptions {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(MessageOptionsConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = FieldOptions.TYPE_URL)
@Serializable(with = FieldOptions.KotlinxSerializer::class)
@SerialName(value = FieldOptions.TYPE_URL)
public data class FieldOptions(
  @ProtobufIndex(index = 1)
  public val ctype: CType = CType.values()[0],
  @ProtobufIndex(index = 2)
  public val packed: Boolean = false,
  @ProtobufIndex(index = 6)
  public val jstype: JSType = JSType.values()[0],
  @ProtobufIndex(index = 5)
  public val lazy: Boolean = false,
  @ProtobufIndex(index = 15)
  public val unverifiedLazy: Boolean = false,
  @ProtobufIndex(index = 3)
  public val deprecated: Boolean = false,
  @ProtobufIndex(index = 10)
  public val weak: Boolean = false,
  @ProtobufIndex(index = 16)
  public val debugRedact: Boolean = false,
  @ProtobufIndex(index = 17)
  public val retention: OptionRetention = OptionRetention.values()[0],
  @ProtobufIndex(index = 19)
  public val targets: List<OptionTargetType> = emptyList(),
  @ProtobufIndex(index = 20)
  public val editionDefaults: List<EditionDefault> = emptyList(),
  @ProtobufIndex(index = 21)
  public val features: FeatureSet = FeatureSet(),
  @ProtobufIndex(index = 22)
  public val featureSupport: FeatureSupport = FeatureSupport(),
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FieldOptions"
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.FieldOptions.CType")
  public enum class CType(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    STRING(0),
    @ProtobufIndex(index = 1)
    CORD(1),
    @ProtobufIndex(index = 2)
    STRING_PIECE(2),
    ;

    public companion object {
      public fun forNumber(number: Int): CType = CType.values()
      	.first { it.number == number }
    }
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.FieldOptions.JSType")
  public enum class JSType(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    JS_NORMAL(0),
    @ProtobufIndex(index = 1)
    JS_STRING(1),
    @ProtobufIndex(index = 2)
    JS_NUMBER(2),
    ;

    public companion object {
      public fun forNumber(number: Int): JSType = JSType.values()
      	.first { it.number == number }
    }
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.FieldOptions.OptionRetention")
  public enum class OptionRetention(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    RETENTION_UNKNOWN(0),
    @ProtobufIndex(index = 1)
    RETENTION_RUNTIME(1),
    @ProtobufIndex(index = 2)
    RETENTION_SOURCE(2),
    ;

    public companion object {
      public fun forNumber(number: Int): OptionRetention = OptionRetention.values()
      	.first { it.number == number }
    }
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.FieldOptions.OptionTargetType")
  public enum class OptionTargetType(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    TARGET_TYPE_UNKNOWN(0),
    @ProtobufIndex(index = 1)
    TARGET_TYPE_FILE(1),
    @ProtobufIndex(index = 2)
    TARGET_TYPE_EXTENSION_RANGE(2),
    @ProtobufIndex(index = 3)
    TARGET_TYPE_MESSAGE(3),
    @ProtobufIndex(index = 4)
    TARGET_TYPE_FIELD(4),
    @ProtobufIndex(index = 5)
    TARGET_TYPE_ONEOF(5),
    @ProtobufIndex(index = 6)
    TARGET_TYPE_ENUM(6),
    @ProtobufIndex(index = 7)
    TARGET_TYPE_ENUM_ENTRY(7),
    @ProtobufIndex(index = 8)
    TARGET_TYPE_SERVICE(8),
    @ProtobufIndex(index = 9)
    TARGET_TYPE_METHOD(9),
    ;

    public companion object {
      public fun forNumber(number: Int): OptionTargetType = OptionTargetType.values()
      	.first { it.number == number }
    }
  }

  @AnnotationProtobufMessage(typeUrl = EditionDefault.TYPE_URL)
  @Serializable(with = EditionDefault.KotlinxSerializer::class)
  @SerialName(value = EditionDefault.TYPE_URL)
  public data class EditionDefault(
    @ProtobufIndex(index = 3)
    public val edition: Edition = Edition.values()[0],
    @ProtobufIndex(index = 2)
    public val `value`: String = "",
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.FieldOptions.EditionDefault"
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = EditionDefault::class)
    private object ReflectSerializer

    public object KotlinxSerializer : KSerializer<EditionDefault> {
      private val delegator: KSerializer<EditionDefault> = ReflectSerializer

      override val descriptor: SerialDescriptor = delegator.descriptor

      override fun serialize(encoder: Encoder, `value`: EditionDefault) {
        if (encoder is ProtobufConverterEncoder) {
          encoder.serialize(FieldOptionsConverter.EditionDefaultConverter, value)
          return
        }
        delegator.serialize(encoder, value)
      }

      override fun deserialize(decoder: Decoder): EditionDefault {
        if (decoder is ProtobufConverterDecoder) {
          return decoder.deserialize(FieldOptionsConverter.EditionDefaultConverter)
        }
        return delegator.deserialize(decoder)
      }
    }
  }

  @AnnotationProtobufMessage(typeUrl = FeatureSupport.TYPE_URL)
  @Serializable(with = FeatureSupport.KotlinxSerializer::class)
  @SerialName(value = FeatureSupport.TYPE_URL)
  public data class FeatureSupport(
    @ProtobufIndex(index = 1)
    public val editionIntroduced: Edition = Edition.values()[0],
    @ProtobufIndex(index = 2)
    public val editionDeprecated: Edition = Edition.values()[0],
    @ProtobufIndex(index = 3)
    public val deprecationWarning: String = "",
    @ProtobufIndex(index = 4)
    public val editionRemoved: Edition = Edition.values()[0],
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.FieldOptions.FeatureSupport"
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = FeatureSupport::class)
    private object ReflectSerializer

    public object KotlinxSerializer : KSerializer<FeatureSupport> {
      private val delegator: KSerializer<FeatureSupport> = ReflectSerializer

      override val descriptor: SerialDescriptor = delegator.descriptor

      override fun serialize(encoder: Encoder, `value`: FeatureSupport) {
        if (encoder is ProtobufConverterEncoder) {
          encoder.serialize(FieldOptionsConverter.FeatureSupportConverter, value)
          return
        }
        delegator.serialize(encoder, value)
      }

      override fun deserialize(decoder: Decoder): FeatureSupport {
        if (decoder is ProtobufConverterDecoder) {
          return decoder.deserialize(FieldOptionsConverter.FeatureSupportConverter)
        }
        return delegator.deserialize(decoder)
      }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = FieldOptions::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<FieldOptions> {
    private val delegator: KSerializer<FieldOptions> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: FieldOptions) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(FieldOptionsConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): FieldOptions {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(FieldOptionsConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = OneofOptions.TYPE_URL)
@Serializable(with = OneofOptions.KotlinxSerializer::class)
@SerialName(value = OneofOptions.TYPE_URL)
public data class OneofOptions(
  @ProtobufIndex(index = 1)
  public val features: FeatureSet = FeatureSet(),
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.OneofOptions"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = OneofOptions::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<OneofOptions> {
    private val delegator: KSerializer<OneofOptions> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: OneofOptions) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(OneofOptionsConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): OneofOptions {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(OneofOptionsConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = EnumOptions.TYPE_URL)
@Serializable(with = EnumOptions.KotlinxSerializer::class)
@SerialName(value = EnumOptions.TYPE_URL)
public data class EnumOptions(
  @ProtobufIndex(index = 2)
  public val allowAlias: Boolean = false,
  @ProtobufIndex(index = 3)
  public val deprecated: Boolean = false,
  @Deprecated(
    message = "",
    replaceWith = ReplaceWith(""),
    level = DeprecationLevel.WARNING,
  )
  @ProtobufIndex(index = 6)
  public val deprecatedLegacyJsonFieldConflicts: Boolean = false,
  @ProtobufIndex(index = 7)
  public val features: FeatureSet = FeatureSet(),
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.EnumOptions"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = EnumOptions::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<EnumOptions> {
    private val delegator: KSerializer<EnumOptions> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: EnumOptions) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(EnumOptionsConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): EnumOptions {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(EnumOptionsConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = EnumValueOptions.TYPE_URL)
@Serializable(with = EnumValueOptions.KotlinxSerializer::class)
@SerialName(value = EnumValueOptions.TYPE_URL)
public data class EnumValueOptions(
  @ProtobufIndex(index = 1)
  public val deprecated: Boolean = false,
  @ProtobufIndex(index = 2)
  public val features: FeatureSet = FeatureSet(),
  @ProtobufIndex(index = 3)
  public val debugRedact: Boolean = false,
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.EnumValueOptions"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = EnumValueOptions::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<EnumValueOptions> {
    private val delegator: KSerializer<EnumValueOptions> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: EnumValueOptions) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(EnumValueOptionsConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): EnumValueOptions {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(EnumValueOptionsConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = ServiceOptions.TYPE_URL)
@Serializable(with = ServiceOptions.KotlinxSerializer::class)
@SerialName(value = ServiceOptions.TYPE_URL)
public data class ServiceOptions(
  @ProtobufIndex(index = 34)
  public val features: FeatureSet = FeatureSet(),
  @ProtobufIndex(index = 33)
  public val deprecated: Boolean = false,
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.ServiceOptions"
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = ServiceOptions::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<ServiceOptions> {
    private val delegator: KSerializer<ServiceOptions> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: ServiceOptions) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(ServiceOptionsConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): ServiceOptions {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(ServiceOptionsConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = MethodOptions.TYPE_URL)
@Serializable(with = MethodOptions.KotlinxSerializer::class)
@SerialName(value = MethodOptions.TYPE_URL)
public data class MethodOptions(
  @ProtobufIndex(index = 33)
  public val deprecated: Boolean = false,
  @ProtobufIndex(index = 34)
  public val idempotencyLevel: IdempotencyLevel = IdempotencyLevel.values()[0],
  @ProtobufIndex(index = 35)
  public val features: FeatureSet = FeatureSet(),
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.MethodOptions"
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.MethodOptions.IdempotencyLevel")
  public enum class IdempotencyLevel(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    IDEMPOTENCY_UNKNOWN(0),
    @ProtobufIndex(index = 1)
    NO_SIDE_EFFECTS(1),
    @ProtobufIndex(index = 2)
    IDEMPOTENT(2),
    ;

    public companion object {
      public fun forNumber(number: Int): IdempotencyLevel = IdempotencyLevel.values()
      	.first { it.number == number }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = MethodOptions::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<MethodOptions> {
    private val delegator: KSerializer<MethodOptions> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: MethodOptions) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(MethodOptionsConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): MethodOptions {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(MethodOptionsConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = UninterpretedOption.TYPE_URL)
@Serializable(with = UninterpretedOption.KotlinxSerializer::class)
@SerialName(value = UninterpretedOption.TYPE_URL)
public data class UninterpretedOption(
  @ProtobufIndex(index = 2)
  public val name: List<NamePart> = emptyList(),
  @ProtobufIndex(index = 3)
  public val identifierValue: String = "",
  @ProtobufIndex(index = 4)
  public val positiveIntValue: ULong = 0uL,
  @ProtobufIndex(index = 5)
  public val negativeIntValue: Long = 0L,
  @ProtobufIndex(index = 6)
  public val doubleValue: Double = 0.0,
  @ProtobufIndex(index = 7)
  public val stringValue: ByteArray = byteArrayOf(),
  @ProtobufIndex(index = 8)
  public val aggregateValue: String = "",
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.UninterpretedOption"
  }

  @AnnotationProtobufMessage(typeUrl = NamePart.TYPE_URL)
  @Serializable(with = NamePart.KotlinxSerializer::class)
  @SerialName(value = NamePart.TYPE_URL)
  public data class NamePart(
    @ProtobufIndex(index = 1)
    public val namePart: String = "",
    @ProtobufIndex(index = 2)
    public val isExtension: Boolean = false,
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.UninterpretedOption.NamePart"
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = NamePart::class)
    private object ReflectSerializer

    public object KotlinxSerializer : KSerializer<NamePart> {
      private val delegator: KSerializer<NamePart> = ReflectSerializer

      override val descriptor: SerialDescriptor = delegator.descriptor

      override fun serialize(encoder: Encoder, `value`: NamePart) {
        if (encoder is ProtobufConverterEncoder) {
          encoder.serialize(UninterpretedOptionConverter.NamePartConverter, value)
          return
        }
        delegator.serialize(encoder, value)
      }

      override fun deserialize(decoder: Decoder): NamePart {
        if (decoder is ProtobufConverterDecoder) {
          return decoder.deserialize(UninterpretedOptionConverter.NamePartConverter)
        }
        return delegator.deserialize(decoder)
      }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = UninterpretedOption::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<UninterpretedOption> {
    private val delegator: KSerializer<UninterpretedOption> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: UninterpretedOption) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(UninterpretedOptionConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): UninterpretedOption {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(UninterpretedOptionConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = FeatureSet.TYPE_URL)
@Serializable(with = FeatureSet.KotlinxSerializer::class)
@SerialName(value = FeatureSet.TYPE_URL)
public data class FeatureSet(
  @ProtobufIndex(index = 1)
  public val fieldPresence: FieldPresence = FieldPresence.values()[0],
  @ProtobufIndex(index = 2)
  public val enumType: EnumType = EnumType.values()[0],
  @ProtobufIndex(index = 3)
  public val repeatedFieldEncoding: RepeatedFieldEncoding = RepeatedFieldEncoding.values()[0],
  @ProtobufIndex(index = 4)
  public val utf8Validation: Utf8Validation = Utf8Validation.values()[0],
  @ProtobufIndex(index = 5)
  public val messageEncoding: MessageEncoding = MessageEncoding.values()[0],
  @ProtobufIndex(index = 6)
  public val jsonFormat: JsonFormat = JsonFormat.values()[0],
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FeatureSet"
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.FeatureSet.FieldPresence")
  public enum class FieldPresence(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    FIELD_PRESENCE_UNKNOWN(0),
    @ProtobufIndex(index = 1)
    EXPLICIT(1),
    @ProtobufIndex(index = 2)
    IMPLICIT(2),
    @ProtobufIndex(index = 3)
    LEGACY_REQUIRED(3),
    ;

    public companion object {
      public fun forNumber(number: Int): FieldPresence = FieldPresence.values()
      	.first { it.number == number }
    }
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.FeatureSet.EnumType")
  public enum class EnumType(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    ENUM_TYPE_UNKNOWN(0),
    @ProtobufIndex(index = 1)
    OPEN(1),
    @ProtobufIndex(index = 2)
    CLOSED(2),
    ;

    public companion object {
      public fun forNumber(number: Int): EnumType = EnumType.values()
      	.first { it.number == number }
    }
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.FeatureSet.RepeatedFieldEncoding")
  public enum class RepeatedFieldEncoding(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    REPEATED_FIELD_ENCODING_UNKNOWN(0),
    @ProtobufIndex(index = 1)
    PACKED(1),
    @ProtobufIndex(index = 2)
    EXPANDED(2),
    ;

    public companion object {
      public fun forNumber(number: Int): RepeatedFieldEncoding = RepeatedFieldEncoding.values()
      	.first { it.number == number }
    }
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.FeatureSet.Utf8Validation")
  public enum class Utf8Validation(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    UTF8_VALIDATION_UNKNOWN(0),
    @ProtobufIndex(index = 2)
    VERIFY(2),
    @ProtobufIndex(index = 3)
    NONE(3),
    ;

    public companion object {
      public fun forNumber(number: Int): Utf8Validation = Utf8Validation.values()
      	.first { it.number == number }
    }
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.FeatureSet.MessageEncoding")
  public enum class MessageEncoding(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    MESSAGE_ENCODING_UNKNOWN(0),
    @ProtobufIndex(index = 1)
    LENGTH_PREFIXED(1),
    @ProtobufIndex(index = 2)
    DELIMITED(2),
    ;

    public companion object {
      public fun forNumber(number: Int): MessageEncoding = MessageEncoding.values()
      	.first { it.number == number }
    }
  }

  @Serializable
  @SerialName(value = "type.googleapis.com/google.protobuf.FeatureSet.JsonFormat")
  public enum class JsonFormat(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    JSON_FORMAT_UNKNOWN(0),
    @ProtobufIndex(index = 1)
    ALLOW(1),
    @ProtobufIndex(index = 2)
    LEGACY_BEST_EFFORT(2),
    ;

    public companion object {
      public fun forNumber(number: Int): JsonFormat = JsonFormat.values()
      	.first { it.number == number }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = FeatureSet::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<FeatureSet> {
    private val delegator: KSerializer<FeatureSet> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: FeatureSet) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(FeatureSetConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): FeatureSet {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(FeatureSetConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = FeatureSetDefaults.TYPE_URL)
@Serializable(with = FeatureSetDefaults.KotlinxSerializer::class)
@SerialName(value = FeatureSetDefaults.TYPE_URL)
public data class FeatureSetDefaults(
  @ProtobufIndex(index = 1)
  public val defaults: List<FeatureSetEditionDefault> = emptyList(),
  @ProtobufIndex(index = 4)
  public val minimumEdition: Edition = Edition.values()[0],
  @ProtobufIndex(index = 5)
  public val maximumEdition: Edition = Edition.values()[0],
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FeatureSetDefaults"
  }

  @AnnotationProtobufMessage(typeUrl = FeatureSetEditionDefault.TYPE_URL)
  @Serializable(with = FeatureSetEditionDefault.KotlinxSerializer::class)
  @SerialName(value = FeatureSetEditionDefault.TYPE_URL)
  public data class FeatureSetEditionDefault(
    @ProtobufIndex(index = 3)
    public val edition: Edition = Edition.values()[0],
    @ProtobufIndex(index = 4)
    public val overridableFeatures: FeatureSet = FeatureSet(),
    @ProtobufIndex(index = 5)
    public val fixedFeatures: FeatureSet = FeatureSet(),
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.FeatureSetDefaults.FeatureSetEditionDefault"
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = FeatureSetEditionDefault::class)
    private object ReflectSerializer

    public object KotlinxSerializer : KSerializer<FeatureSetEditionDefault> {
      private val delegator: KSerializer<FeatureSetEditionDefault> = ReflectSerializer

      override val descriptor: SerialDescriptor = delegator.descriptor

      override fun serialize(encoder: Encoder, `value`: FeatureSetEditionDefault) {
        if (encoder is ProtobufConverterEncoder) {
          encoder.serialize(FeatureSetDefaultsConverter.FeatureSetEditionDefaultConverter, value)
          return
        }
        delegator.serialize(encoder, value)
      }

      override fun deserialize(decoder: Decoder): FeatureSetEditionDefault {
        if (decoder is ProtobufConverterDecoder) {
          return decoder.deserialize(FeatureSetDefaultsConverter.FeatureSetEditionDefaultConverter)
        }
        return delegator.deserialize(decoder)
      }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = FeatureSetDefaults::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<FeatureSetDefaults> {
    private val delegator: KSerializer<FeatureSetDefaults> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: FeatureSetDefaults) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(FeatureSetDefaultsConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): FeatureSetDefaults {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(FeatureSetDefaultsConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = SourceCodeInfo.TYPE_URL)
@Serializable(with = SourceCodeInfo.KotlinxSerializer::class)
@SerialName(value = SourceCodeInfo.TYPE_URL)
public data class SourceCodeInfo(
  @ProtobufIndex(index = 1)
  public val location: List<Location> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.SourceCodeInfo"
  }

  @AnnotationProtobufMessage(typeUrl = Location.TYPE_URL)
  @Serializable(with = Location.KotlinxSerializer::class)
  @SerialName(value = Location.TYPE_URL)
  public data class Location(
    @ProtobufIndex(index = 1)
    public val path: List<Int> = emptyList(),
    @ProtobufIndex(index = 2)
    public val span: List<Int> = emptyList(),
    @ProtobufIndex(index = 3)
    public val leadingComments: String = "",
    @ProtobufIndex(index = 4)
    public val trailingComments: String = "",
    @ProtobufIndex(index = 6)
    public val leadingDetachedComments: List<String> = emptyList(),
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.SourceCodeInfo.Location"
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = Location::class)
    private object ReflectSerializer

    public object KotlinxSerializer : KSerializer<Location> {
      private val delegator: KSerializer<Location> = ReflectSerializer

      override val descriptor: SerialDescriptor = delegator.descriptor

      override fun serialize(encoder: Encoder, `value`: Location) {
        if (encoder is ProtobufConverterEncoder) {
          encoder.serialize(SourceCodeInfoConverter.LocationConverter, value)
          return
        }
        delegator.serialize(encoder, value)
      }

      override fun deserialize(decoder: Decoder): Location {
        if (decoder is ProtobufConverterDecoder) {
          return decoder.deserialize(SourceCodeInfoConverter.LocationConverter)
        }
        return delegator.deserialize(decoder)
      }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = SourceCodeInfo::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<SourceCodeInfo> {
    private val delegator: KSerializer<SourceCodeInfo> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: SourceCodeInfo) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(SourceCodeInfoConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): SourceCodeInfo {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(SourceCodeInfoConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}

@AnnotationProtobufMessage(typeUrl = GeneratedCodeInfo.TYPE_URL)
@Serializable(with = GeneratedCodeInfo.KotlinxSerializer::class)
@SerialName(value = GeneratedCodeInfo.TYPE_URL)
public data class GeneratedCodeInfo(
  @ProtobufIndex(index = 1)
  public val `annotation`: List<Annotation> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.GeneratedCodeInfo"
  }

  @AnnotationProtobufMessage(typeUrl = Annotation.TYPE_URL)
  @Serializable(with = Annotation.KotlinxSerializer::class)
  @SerialName(value = Annotation.TYPE_URL)
  public data class Annotation(
    @ProtobufIndex(index = 1)
    public val path: List<Int> = emptyList(),
    @ProtobufIndex(index = 2)
    public val sourceFile: String = "",
    @ProtobufIndex(index = 3)
    public val begin: Int = 0,
    @ProtobufIndex(index = 4)
    public val end: Int = 0,
    @ProtobufIndex(index = 5)
    public val semantic: Semantic = Semantic.values()[0],
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.GeneratedCodeInfo.Annotation"
    }

    @Serializable
    @SerialName(value = "type.googleapis.com/google.protobuf.GeneratedCodeInfo.Annotation.Semantic")
    public enum class Semantic(
      public val number: Int,
    ) {
      @ProtobufIndex(index = 0)
      NONE(0),
      @ProtobufIndex(index = 1)
      SET(1),
      @ProtobufIndex(index = 2)
      ALIAS(2),
      ;

      public companion object {
        public fun forNumber(number: Int): Semantic = Semantic.values()
        	.first { it.number == number }
      }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = Annotation::class)
    private object ReflectSerializer

    public object KotlinxSerializer : KSerializer<Annotation> {
      private val delegator: KSerializer<Annotation> = ReflectSerializer

      override val descriptor: SerialDescriptor = delegator.descriptor

      override fun serialize(encoder: Encoder, `value`: Annotation) {
        if (encoder is ProtobufConverterEncoder) {
          encoder.serialize(GeneratedCodeInfoConverter.AnnotationConverter, value)
          return
        }
        delegator.serialize(encoder, value)
      }

      override fun deserialize(decoder: Decoder): Annotation {
        if (decoder is ProtobufConverterDecoder) {
          return decoder.deserialize(GeneratedCodeInfoConverter.AnnotationConverter)
        }
        return delegator.deserialize(decoder)
      }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializer(forClass = GeneratedCodeInfo::class)
  private object ReflectSerializer

  public object KotlinxSerializer : KSerializer<GeneratedCodeInfo> {
    private val delegator: KSerializer<GeneratedCodeInfo> = ReflectSerializer

    override val descriptor: SerialDescriptor = delegator.descriptor

    override fun serialize(encoder: Encoder, `value`: GeneratedCodeInfo) {
      if (encoder is ProtobufConverterEncoder) {
        encoder.serialize(GeneratedCodeInfoConverter, value)
        return
      }
      delegator.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): GeneratedCodeInfo {
      if (decoder is ProtobufConverterDecoder) {
        return decoder.deserialize(GeneratedCodeInfoConverter)
      }
      return delegator.deserialize(decoder)
    }
  }
}
