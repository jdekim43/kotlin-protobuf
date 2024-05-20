// Transform from google/protobuf/descriptor.proto
@file:GeneratorVersion(version = "0.4.1")

package google.protobuf

import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Deprecated
import kotlin.DeprecationLevel
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.ReplaceWith
import kotlin.String
import kotlin.ULong
import kotlin.collections.List
import kotlin.collections.emptyList
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.`annotation`.ProtobufMessage as AnnotationProtobufMessage
import kr.jadekim.protobuf.type.ProtobufMessage as TypeProtobufMessage

public enum class Edition(
  public val number: Int,
) {
  @ProtobufIndex(index = 0)
  EDITION_UNKNOWN(0),
  @ProtobufIndex(index = 998)
  EDITION_PROTO2(998),
  @ProtobufIndex(index = 999)
  EDITION_PROTO3(999),
  @ProtobufIndex(index = 1_000)
  EDITION_2023(1_000),
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
  ;

  public companion object {
    public fun forNumber(number: Int): Edition = Edition.values()
    	.first { it.number == number }
  }
}

@AnnotationProtobufMessage(typeUrl = FileDescriptorSet.TYPE_URL)
public data class FileDescriptorSet(
  @ProtobufIndex(index = 1)
  public val `file`: List<FileDescriptorProto> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FileDescriptorSet"
  }
}

@AnnotationProtobufMessage(typeUrl = FileDescriptorProto.TYPE_URL)
public data class FileDescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String? = null,
  @ProtobufIndex(index = 2)
  public val `package`: String? = null,
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
  public val options: FileOptions? = null,
  @ProtobufIndex(index = 9)
  public val sourceCodeInfo: SourceCodeInfo? = null,
  @ProtobufIndex(index = 12)
  public val syntax: String? = null,
  @ProtobufIndex(index = 14)
  public val edition: Edition? = null,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FileDescriptorProto"
  }
}

@AnnotationProtobufMessage(typeUrl = DescriptorProto.TYPE_URL)
public data class DescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String? = null,
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
  public val options: MessageOptions? = null,
  @ProtobufIndex(index = 9)
  public val reservedRange: List<ReservedRange> = emptyList(),
  @ProtobufIndex(index = 10)
  public val reservedName: List<String> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.DescriptorProto"
  }

  @AnnotationProtobufMessage(typeUrl = ExtensionRange.TYPE_URL)
  public data class ExtensionRange(
    @ProtobufIndex(index = 1)
    public val start: Int? = null,
    @ProtobufIndex(index = 2)
    public val end: Int? = null,
    @ProtobufIndex(index = 3)
    public val options: ExtensionRangeOptions? = null,
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.DescriptorProto.ExtensionRange"
    }
  }

  @AnnotationProtobufMessage(typeUrl = ReservedRange.TYPE_URL)
  public data class ReservedRange(
    @ProtobufIndex(index = 1)
    public val start: Int? = null,
    @ProtobufIndex(index = 2)
    public val end: Int? = null,
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.DescriptorProto.ReservedRange"
    }
  }
}

@AnnotationProtobufMessage(typeUrl = ExtensionRangeOptions.TYPE_URL)
public data class ExtensionRangeOptions(
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
  @ProtobufIndex(index = 2)
  public val declaration: List<Declaration> = emptyList(),
  @ProtobufIndex(index = 50)
  public val features: FeatureSet? = null,
  @ProtobufIndex(index = 3)
  public val verification: VerificationState? = null,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.ExtensionRangeOptions"
  }

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
  public data class Declaration(
    @ProtobufIndex(index = 1)
    public val number: Int? = null,
    @ProtobufIndex(index = 2)
    public val fullName: String? = null,
    @ProtobufIndex(index = 3)
    public val type: String? = null,
    @ProtobufIndex(index = 5)
    public val reserved: Boolean? = null,
    @ProtobufIndex(index = 6)
    public val repeated: Boolean? = null,
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.ExtensionRangeOptions.Declaration"
    }
  }
}

@AnnotationProtobufMessage(typeUrl = FieldDescriptorProto.TYPE_URL)
public data class FieldDescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String? = null,
  @ProtobufIndex(index = 3)
  public val number: Int? = null,
  @ProtobufIndex(index = 4)
  public val label: Label? = null,
  @ProtobufIndex(index = 5)
  public val type: Type? = null,
  @ProtobufIndex(index = 6)
  public val typeName: String? = null,
  @ProtobufIndex(index = 2)
  public val extendee: String? = null,
  @ProtobufIndex(index = 7)
  public val defaultValue: String? = null,
  @ProtobufIndex(index = 9)
  public val oneofIndex: Int? = null,
  @ProtobufIndex(index = 10)
  public val jsonName: String? = null,
  @ProtobufIndex(index = 8)
  public val options: FieldOptions? = null,
  @ProtobufIndex(index = 17)
  public val proto3Optional: Boolean? = null,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FieldDescriptorProto"
  }

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
}

@AnnotationProtobufMessage(typeUrl = OneofDescriptorProto.TYPE_URL)
public data class OneofDescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String? = null,
  @ProtobufIndex(index = 2)
  public val options: OneofOptions? = null,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.OneofDescriptorProto"
  }
}

@AnnotationProtobufMessage(typeUrl = EnumDescriptorProto.TYPE_URL)
public data class EnumDescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String? = null,
  @ProtobufIndex(index = 2)
  public val `value`: List<EnumValueDescriptorProto> = emptyList(),
  @ProtobufIndex(index = 3)
  public val options: EnumOptions? = null,
  @ProtobufIndex(index = 4)
  public val reservedRange: List<EnumReservedRange> = emptyList(),
  @ProtobufIndex(index = 5)
  public val reservedName: List<String> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.EnumDescriptorProto"
  }

  @AnnotationProtobufMessage(typeUrl = EnumReservedRange.TYPE_URL)
  public data class EnumReservedRange(
    @ProtobufIndex(index = 1)
    public val start: Int? = null,
    @ProtobufIndex(index = 2)
    public val end: Int? = null,
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.EnumDescriptorProto.EnumReservedRange"
    }
  }
}

@AnnotationProtobufMessage(typeUrl = EnumValueDescriptorProto.TYPE_URL)
public data class EnumValueDescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String? = null,
  @ProtobufIndex(index = 2)
  public val number: Int? = null,
  @ProtobufIndex(index = 3)
  public val options: EnumValueOptions? = null,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String =
        "type.googleapis.com/google.protobuf.EnumValueDescriptorProto"
  }
}

@AnnotationProtobufMessage(typeUrl = ServiceDescriptorProto.TYPE_URL)
public data class ServiceDescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String? = null,
  @ProtobufIndex(index = 2)
  public val method: List<MethodDescriptorProto> = emptyList(),
  @ProtobufIndex(index = 3)
  public val options: ServiceOptions? = null,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.ServiceDescriptorProto"
  }
}

@AnnotationProtobufMessage(typeUrl = MethodDescriptorProto.TYPE_URL)
public data class MethodDescriptorProto(
  @ProtobufIndex(index = 1)
  public val name: String? = null,
  @ProtobufIndex(index = 2)
  public val inputType: String? = null,
  @ProtobufIndex(index = 3)
  public val outputType: String? = null,
  @ProtobufIndex(index = 4)
  public val options: MethodOptions? = null,
  @ProtobufIndex(index = 5)
  public val clientStreaming: Boolean? = null,
  @ProtobufIndex(index = 6)
  public val serverStreaming: Boolean? = null,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.MethodDescriptorProto"
  }
}

@AnnotationProtobufMessage(typeUrl = FileOptions.TYPE_URL)
public data class FileOptions(
  @ProtobufIndex(index = 1)
  public val javaPackage: String? = null,
  @ProtobufIndex(index = 8)
  public val javaOuterClassname: String? = null,
  @ProtobufIndex(index = 10)
  public val javaMultipleFiles: Boolean? = null,
  @Deprecated(
    message = "",
    replaceWith = ReplaceWith(""),
    level = DeprecationLevel.WARNING,
  )
  @ProtobufIndex(index = 20)
  public val javaGenerateEqualsAndHash: Boolean? = null,
  @ProtobufIndex(index = 27)
  public val javaStringCheckUtf8: Boolean? = null,
  @ProtobufIndex(index = 9)
  public val optimizeFor: OptimizeMode? = null,
  @ProtobufIndex(index = 11)
  public val goPackage: String? = null,
  @ProtobufIndex(index = 16)
  public val ccGenericServices: Boolean? = null,
  @ProtobufIndex(index = 17)
  public val javaGenericServices: Boolean? = null,
  @ProtobufIndex(index = 18)
  public val pyGenericServices: Boolean? = null,
  @ProtobufIndex(index = 42)
  public val phpGenericServices: Boolean? = null,
  @ProtobufIndex(index = 23)
  public val deprecated: Boolean? = null,
  @ProtobufIndex(index = 31)
  public val ccEnableArenas: Boolean? = null,
  @ProtobufIndex(index = 36)
  public val objcClassPrefix: String? = null,
  @ProtobufIndex(index = 37)
  public val csharpNamespace: String? = null,
  @ProtobufIndex(index = 39)
  public val swiftPrefix: String? = null,
  @ProtobufIndex(index = 40)
  public val phpClassPrefix: String? = null,
  @ProtobufIndex(index = 41)
  public val phpNamespace: String? = null,
  @ProtobufIndex(index = 44)
  public val phpMetadataNamespace: String? = null,
  @ProtobufIndex(index = 45)
  public val rubyPackage: String? = null,
  @ProtobufIndex(index = 50)
  public val features: FeatureSet? = null,
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FileOptions"
  }

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
}

@AnnotationProtobufMessage(typeUrl = MessageOptions.TYPE_URL)
public data class MessageOptions(
  @ProtobufIndex(index = 1)
  public val messageSetWireFormat: Boolean? = null,
  @ProtobufIndex(index = 2)
  public val noStandardDescriptorAccessor: Boolean? = null,
  @ProtobufIndex(index = 3)
  public val deprecated: Boolean? = null,
  @ProtobufIndex(index = 7)
  public val mapEntry: Boolean? = null,
  @Deprecated(
    message = "",
    replaceWith = ReplaceWith(""),
    level = DeprecationLevel.WARNING,
  )
  @ProtobufIndex(index = 11)
  public val deprecatedLegacyJsonFieldConflicts: Boolean? = null,
  @ProtobufIndex(index = 12)
  public val features: FeatureSet? = null,
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.MessageOptions"
  }
}

@AnnotationProtobufMessage(typeUrl = FieldOptions.TYPE_URL)
public data class FieldOptions(
  @ProtobufIndex(index = 1)
  public val ctype: CType? = null,
  @ProtobufIndex(index = 2)
  public val packed: Boolean? = null,
  @ProtobufIndex(index = 6)
  public val jstype: JSType? = null,
  @ProtobufIndex(index = 5)
  public val lazy: Boolean? = null,
  @ProtobufIndex(index = 15)
  public val unverifiedLazy: Boolean? = null,
  @ProtobufIndex(index = 3)
  public val deprecated: Boolean? = null,
  @ProtobufIndex(index = 10)
  public val weak: Boolean? = null,
  @ProtobufIndex(index = 16)
  public val debugRedact: Boolean? = null,
  @ProtobufIndex(index = 17)
  public val retention: OptionRetention? = null,
  @ProtobufIndex(index = 19)
  public val targets: List<OptionTargetType> = emptyList(),
  @ProtobufIndex(index = 20)
  public val editionDefaults: List<EditionDefault> = emptyList(),
  @ProtobufIndex(index = 21)
  public val features: FeatureSet? = null,
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FieldOptions"
  }

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
  public data class EditionDefault(
    @ProtobufIndex(index = 3)
    public val edition: Edition? = null,
    @ProtobufIndex(index = 2)
    public val `value`: String? = null,
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.FieldOptions.EditionDefault"
    }
  }
}

@AnnotationProtobufMessage(typeUrl = OneofOptions.TYPE_URL)
public data class OneofOptions(
  @ProtobufIndex(index = 1)
  public val features: FeatureSet? = null,
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.OneofOptions"
  }
}

@AnnotationProtobufMessage(typeUrl = EnumOptions.TYPE_URL)
public data class EnumOptions(
  @ProtobufIndex(index = 2)
  public val allowAlias: Boolean? = null,
  @ProtobufIndex(index = 3)
  public val deprecated: Boolean? = null,
  @Deprecated(
    message = "",
    replaceWith = ReplaceWith(""),
    level = DeprecationLevel.WARNING,
  )
  @ProtobufIndex(index = 6)
  public val deprecatedLegacyJsonFieldConflicts: Boolean? = null,
  @ProtobufIndex(index = 7)
  public val features: FeatureSet? = null,
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.EnumOptions"
  }
}

@AnnotationProtobufMessage(typeUrl = EnumValueOptions.TYPE_URL)
public data class EnumValueOptions(
  @ProtobufIndex(index = 1)
  public val deprecated: Boolean? = null,
  @ProtobufIndex(index = 2)
  public val features: FeatureSet? = null,
  @ProtobufIndex(index = 3)
  public val debugRedact: Boolean? = null,
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.EnumValueOptions"
  }
}

@AnnotationProtobufMessage(typeUrl = ServiceOptions.TYPE_URL)
public data class ServiceOptions(
  @ProtobufIndex(index = 34)
  public val features: FeatureSet? = null,
  @ProtobufIndex(index = 33)
  public val deprecated: Boolean? = null,
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.ServiceOptions"
  }
}

@AnnotationProtobufMessage(typeUrl = MethodOptions.TYPE_URL)
public data class MethodOptions(
  @ProtobufIndex(index = 33)
  public val deprecated: Boolean? = null,
  @ProtobufIndex(index = 34)
  public val idempotencyLevel: IdempotencyLevel? = null,
  @ProtobufIndex(index = 35)
  public val features: FeatureSet? = null,
  @ProtobufIndex(index = 999)
  public val uninterpretedOption: List<UninterpretedOption> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.MethodOptions"
  }

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
}

@AnnotationProtobufMessage(typeUrl = UninterpretedOption.TYPE_URL)
public data class UninterpretedOption(
  @ProtobufIndex(index = 2)
  public val name: List<NamePart> = emptyList(),
  @ProtobufIndex(index = 3)
  public val identifierValue: String? = null,
  @ProtobufIndex(index = 4)
  public val positiveIntValue: ULong? = null,
  @ProtobufIndex(index = 5)
  public val negativeIntValue: Long? = null,
  @ProtobufIndex(index = 6)
  public val doubleValue: Double? = null,
  @ProtobufIndex(index = 7)
  public val stringValue: ByteArray? = null,
  @ProtobufIndex(index = 8)
  public val aggregateValue: String? = null,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.UninterpretedOption"
  }

  @AnnotationProtobufMessage(typeUrl = NamePart.TYPE_URL)
  public data class NamePart(
    @ProtobufIndex(index = 1)
    public val namePart: String? = null,
    @ProtobufIndex(index = 2)
    public val isExtension: Boolean? = null,
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.UninterpretedOption.NamePart"
    }
  }
}

@AnnotationProtobufMessage(typeUrl = FeatureSet.TYPE_URL)
public data class FeatureSet(
  @ProtobufIndex(index = 1)
  public val fieldPresence: FieldPresence? = null,
  @ProtobufIndex(index = 2)
  public val enumType: EnumType? = null,
  @ProtobufIndex(index = 3)
  public val repeatedFieldEncoding: RepeatedFieldEncoding? = null,
  @ProtobufIndex(index = 4)
  public val utf8Validation: Utf8Validation? = null,
  @ProtobufIndex(index = 5)
  public val messageEncoding: MessageEncoding? = null,
  @ProtobufIndex(index = 6)
  public val jsonFormat: JsonFormat? = null,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FeatureSet"
  }

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

  public enum class Utf8Validation(
    public val number: Int,
  ) {
    @ProtobufIndex(index = 0)
    UTF8_VALIDATION_UNKNOWN(0),
    @ProtobufIndex(index = 1)
    NONE(1),
    @ProtobufIndex(index = 2)
    VERIFY(2),
    ;

    public companion object {
      public fun forNumber(number: Int): Utf8Validation = Utf8Validation.values()
      	.first { it.number == number }
    }
  }

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
}

@AnnotationProtobufMessage(typeUrl = FeatureSetDefaults.TYPE_URL)
public data class FeatureSetDefaults(
  @ProtobufIndex(index = 1)
  public val defaults: List<FeatureSetEditionDefault> = emptyList(),
  @ProtobufIndex(index = 4)
  public val minimumEdition: Edition? = null,
  @ProtobufIndex(index = 5)
  public val maximumEdition: Edition? = null,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FeatureSetDefaults"
  }

  @AnnotationProtobufMessage(typeUrl = FeatureSetEditionDefault.TYPE_URL)
  public data class FeatureSetEditionDefault(
    @ProtobufIndex(index = 3)
    public val edition: Edition? = null,
    @ProtobufIndex(index = 2)
    public val features: FeatureSet? = null,
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.FeatureSetDefaults.FeatureSetEditionDefault"
    }
  }
}

@AnnotationProtobufMessage(typeUrl = SourceCodeInfo.TYPE_URL)
public data class SourceCodeInfo(
  @ProtobufIndex(index = 1)
  public val location: List<Location> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.SourceCodeInfo"
  }

  @AnnotationProtobufMessage(typeUrl = Location.TYPE_URL)
  public data class Location(
    @ProtobufIndex(index = 1)
    public val path: List<Int> = emptyList(),
    @ProtobufIndex(index = 2)
    public val span: List<Int> = emptyList(),
    @ProtobufIndex(index = 3)
    public val leadingComments: String? = null,
    @ProtobufIndex(index = 4)
    public val trailingComments: String? = null,
    @ProtobufIndex(index = 6)
    public val leadingDetachedComments: List<String> = emptyList(),
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.SourceCodeInfo.Location"
    }
  }
}

@AnnotationProtobufMessage(typeUrl = GeneratedCodeInfo.TYPE_URL)
public data class GeneratedCodeInfo(
  @ProtobufIndex(index = 1)
  public val `annotation`: List<Annotation> = emptyList(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.GeneratedCodeInfo"
  }

  @AnnotationProtobufMessage(typeUrl = Annotation.TYPE_URL)
  public data class Annotation(
    @ProtobufIndex(index = 1)
    public val path: List<Int> = emptyList(),
    @ProtobufIndex(index = 2)
    public val sourceFile: String? = null,
    @ProtobufIndex(index = 3)
    public val begin: Int? = null,
    @ProtobufIndex(index = 4)
    public val end: Int? = null,
    @ProtobufIndex(index = 5)
    public val semantic: Semantic? = null,
  ) : TypeProtobufMessage {
    public companion object {
      public const val TYPE_URL: String =
          "type.googleapis.com/google.protobuf.GeneratedCodeInfo.Annotation"
    }

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
  }
}
