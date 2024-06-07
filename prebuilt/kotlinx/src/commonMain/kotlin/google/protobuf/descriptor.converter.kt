// Transform from google/protobuf/descriptor.proto
@file:GeneratorVersion(version = "0.5.2")

package google.protobuf

import java.lang.IllegalStateException
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.converter.parseProtobuf

public expect object FileDescriptorSetConverter : ProtobufConverter<FileDescriptorSet>

public fun FileDescriptorSet.toAny(): Any = Any(FileDescriptorSet.TYPE_URL,
    with(FileDescriptorSetConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<FileDescriptorSet> = FileDescriptorSetConverter):
    FileDescriptorSet {
  if (typeUrl != FileDescriptorSet.TYPE_URL) throw
      IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val FileDescriptorSet.Companion.converter: FileDescriptorSetConverter
  get() = FileDescriptorSetConverter

public expect object FileDescriptorProtoConverter : ProtobufConverter<FileDescriptorProto>

public fun FileDescriptorProto.toAny(): Any = Any(FileDescriptorProto.TYPE_URL,
    with(FileDescriptorProtoConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<FileDescriptorProto> =
    FileDescriptorProtoConverter): FileDescriptorProto {
  if (typeUrl != FileDescriptorProto.TYPE_URL) throw
      IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val FileDescriptorProto.Companion.converter: FileDescriptorProtoConverter
  get() = FileDescriptorProtoConverter

public expect object DescriptorProtoConverter : ProtobufConverter<DescriptorProto> {
  public object ExtensionRangeConverter : ProtobufConverter<DescriptorProto.ExtensionRange>

  public object ReservedRangeConverter : ProtobufConverter<DescriptorProto.ReservedRange>
}

public fun DescriptorProto.toAny(): Any = Any(DescriptorProto.TYPE_URL,
    with(DescriptorProtoConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<DescriptorProto> = DescriptorProtoConverter):
    DescriptorProto {
  if (typeUrl != DescriptorProto.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val DescriptorProto.Companion.converter: DescriptorProtoConverter
  get() = DescriptorProtoConverter

public expect object ExtensionRangeOptionsConverter : ProtobufConverter<ExtensionRangeOptions> {
  public object DeclarationConverter : ProtobufConverter<ExtensionRangeOptions.Declaration>
}

public fun ExtensionRangeOptions.toAny(): Any = Any(ExtensionRangeOptions.TYPE_URL,
    with(ExtensionRangeOptionsConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<ExtensionRangeOptions> =
    ExtensionRangeOptionsConverter): ExtensionRangeOptions {
  if (typeUrl != ExtensionRangeOptions.TYPE_URL) throw
      IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val ExtensionRangeOptions.Companion.converter: ExtensionRangeOptionsConverter
  get() = ExtensionRangeOptionsConverter

public expect object FieldDescriptorProtoConverter : ProtobufConverter<FieldDescriptorProto>

public fun FieldDescriptorProto.toAny(): Any = Any(FieldDescriptorProto.TYPE_URL,
    with(FieldDescriptorProtoConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<FieldDescriptorProto> =
    FieldDescriptorProtoConverter): FieldDescriptorProto {
  if (typeUrl != FieldDescriptorProto.TYPE_URL) throw
      IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val FieldDescriptorProto.Companion.converter: FieldDescriptorProtoConverter
  get() = FieldDescriptorProtoConverter

public expect object OneofDescriptorProtoConverter : ProtobufConverter<OneofDescriptorProto>

public fun OneofDescriptorProto.toAny(): Any = Any(OneofDescriptorProto.TYPE_URL,
    with(OneofDescriptorProtoConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<OneofDescriptorProto> =
    OneofDescriptorProtoConverter): OneofDescriptorProto {
  if (typeUrl != OneofDescriptorProto.TYPE_URL) throw
      IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val OneofDescriptorProto.Companion.converter: OneofDescriptorProtoConverter
  get() = OneofDescriptorProtoConverter

public expect object EnumDescriptorProtoConverter : ProtobufConverter<EnumDescriptorProto> {
  public object EnumReservedRangeConverter :
      ProtobufConverter<EnumDescriptorProto.EnumReservedRange>
}

public fun EnumDescriptorProto.toAny(): Any = Any(EnumDescriptorProto.TYPE_URL,
    with(EnumDescriptorProtoConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<EnumDescriptorProto> =
    EnumDescriptorProtoConverter): EnumDescriptorProto {
  if (typeUrl != EnumDescriptorProto.TYPE_URL) throw
      IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val EnumDescriptorProto.Companion.converter: EnumDescriptorProtoConverter
  get() = EnumDescriptorProtoConverter

public expect object EnumValueDescriptorProtoConverter : ProtobufConverter<EnumValueDescriptorProto>

public fun EnumValueDescriptorProto.toAny(): Any = Any(EnumValueDescriptorProto.TYPE_URL,
    with(EnumValueDescriptorProtoConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<EnumValueDescriptorProto> =
    EnumValueDescriptorProtoConverter): EnumValueDescriptorProto {
  if (typeUrl != EnumValueDescriptorProto.TYPE_URL) throw
      IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val EnumValueDescriptorProto.Companion.converter: EnumValueDescriptorProtoConverter
  get() = EnumValueDescriptorProtoConverter

public expect object ServiceDescriptorProtoConverter : ProtobufConverter<ServiceDescriptorProto>

public fun ServiceDescriptorProto.toAny(): Any = Any(ServiceDescriptorProto.TYPE_URL,
    with(ServiceDescriptorProtoConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<ServiceDescriptorProto> =
    ServiceDescriptorProtoConverter): ServiceDescriptorProto {
  if (typeUrl != ServiceDescriptorProto.TYPE_URL) throw
      IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val ServiceDescriptorProto.Companion.converter: ServiceDescriptorProtoConverter
  get() = ServiceDescriptorProtoConverter

public expect object MethodDescriptorProtoConverter : ProtobufConverter<MethodDescriptorProto>

public fun MethodDescriptorProto.toAny(): Any = Any(MethodDescriptorProto.TYPE_URL,
    with(MethodDescriptorProtoConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<MethodDescriptorProto> =
    MethodDescriptorProtoConverter): MethodDescriptorProto {
  if (typeUrl != MethodDescriptorProto.TYPE_URL) throw
      IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val MethodDescriptorProto.Companion.converter: MethodDescriptorProtoConverter
  get() = MethodDescriptorProtoConverter

public expect object FileOptionsConverter : ProtobufConverter<FileOptions>

public fun FileOptions.toAny(): Any = Any(FileOptions.TYPE_URL, with(FileOptionsConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<FileOptions> = FileOptionsConverter):
    FileOptions {
  if (typeUrl != FileOptions.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val FileOptions.Companion.converter: FileOptionsConverter
  get() = FileOptionsConverter

public expect object MessageOptionsConverter : ProtobufConverter<MessageOptions>

public fun MessageOptions.toAny(): Any = Any(MessageOptions.TYPE_URL, with(MessageOptionsConverter)
    { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<MessageOptions> = MessageOptionsConverter):
    MessageOptions {
  if (typeUrl != MessageOptions.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val MessageOptions.Companion.converter: MessageOptionsConverter
  get() = MessageOptionsConverter

public expect object FieldOptionsConverter : ProtobufConverter<FieldOptions> {
  public object EditionDefaultConverter : ProtobufConverter<FieldOptions.EditionDefault>

  public object FeatureSupportConverter : ProtobufConverter<FieldOptions.FeatureSupport>
}

public fun FieldOptions.toAny(): Any = Any(FieldOptions.TYPE_URL, with(FieldOptionsConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<FieldOptions> = FieldOptionsConverter):
    FieldOptions {
  if (typeUrl != FieldOptions.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val FieldOptions.Companion.converter: FieldOptionsConverter
  get() = FieldOptionsConverter

public expect object OneofOptionsConverter : ProtobufConverter<OneofOptions>

public fun OneofOptions.toAny(): Any = Any(OneofOptions.TYPE_URL, with(OneofOptionsConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<OneofOptions> = OneofOptionsConverter):
    OneofOptions {
  if (typeUrl != OneofOptions.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val OneofOptions.Companion.converter: OneofOptionsConverter
  get() = OneofOptionsConverter

public expect object EnumOptionsConverter : ProtobufConverter<EnumOptions>

public fun EnumOptions.toAny(): Any = Any(EnumOptions.TYPE_URL, with(EnumOptionsConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<EnumOptions> = EnumOptionsConverter):
    EnumOptions {
  if (typeUrl != EnumOptions.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val EnumOptions.Companion.converter: EnumOptionsConverter
  get() = EnumOptionsConverter

public expect object EnumValueOptionsConverter : ProtobufConverter<EnumValueOptions>

public fun EnumValueOptions.toAny(): Any = Any(EnumValueOptions.TYPE_URL,
    with(EnumValueOptionsConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<EnumValueOptions> = EnumValueOptionsConverter):
    EnumValueOptions {
  if (typeUrl != EnumValueOptions.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val EnumValueOptions.Companion.converter: EnumValueOptionsConverter
  get() = EnumValueOptionsConverter

public expect object ServiceOptionsConverter : ProtobufConverter<ServiceOptions>

public fun ServiceOptions.toAny(): Any = Any(ServiceOptions.TYPE_URL, with(ServiceOptionsConverter)
    { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<ServiceOptions> = ServiceOptionsConverter):
    ServiceOptions {
  if (typeUrl != ServiceOptions.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val ServiceOptions.Companion.converter: ServiceOptionsConverter
  get() = ServiceOptionsConverter

public expect object MethodOptionsConverter : ProtobufConverter<MethodOptions>

public fun MethodOptions.toAny(): Any = Any(MethodOptions.TYPE_URL, with(MethodOptionsConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<MethodOptions> = MethodOptionsConverter):
    MethodOptions {
  if (typeUrl != MethodOptions.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val MethodOptions.Companion.converter: MethodOptionsConverter
  get() = MethodOptionsConverter

public expect object UninterpretedOptionConverter : ProtobufConverter<UninterpretedOption> {
  public object NamePartConverter : ProtobufConverter<UninterpretedOption.NamePart>
}

public fun UninterpretedOption.toAny(): Any = Any(UninterpretedOption.TYPE_URL,
    with(UninterpretedOptionConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<UninterpretedOption> =
    UninterpretedOptionConverter): UninterpretedOption {
  if (typeUrl != UninterpretedOption.TYPE_URL) throw
      IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val UninterpretedOption.Companion.converter: UninterpretedOptionConverter
  get() = UninterpretedOptionConverter

public expect object FeatureSetConverter : ProtobufConverter<FeatureSet>

public fun FeatureSet.toAny(): Any = Any(FeatureSet.TYPE_URL, with(FeatureSetConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<FeatureSet> = FeatureSetConverter): FeatureSet {
  if (typeUrl != FeatureSet.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val FeatureSet.Companion.converter: FeatureSetConverter
  get() = FeatureSetConverter

public expect object FeatureSetDefaultsConverter : ProtobufConverter<FeatureSetDefaults> {
  public object FeatureSetEditionDefaultConverter :
      ProtobufConverter<FeatureSetDefaults.FeatureSetEditionDefault>
}

public fun FeatureSetDefaults.toAny(): Any = Any(FeatureSetDefaults.TYPE_URL,
    with(FeatureSetDefaultsConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<FeatureSetDefaults> =
    FeatureSetDefaultsConverter): FeatureSetDefaults {
  if (typeUrl != FeatureSetDefaults.TYPE_URL) throw
      IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val FeatureSetDefaults.Companion.converter: FeatureSetDefaultsConverter
  get() = FeatureSetDefaultsConverter

public expect object SourceCodeInfoConverter : ProtobufConverter<SourceCodeInfo> {
  public object LocationConverter : ProtobufConverter<SourceCodeInfo.Location>
}

public fun SourceCodeInfo.toAny(): Any = Any(SourceCodeInfo.TYPE_URL, with(SourceCodeInfoConverter)
    { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<SourceCodeInfo> = SourceCodeInfoConverter):
    SourceCodeInfo {
  if (typeUrl != SourceCodeInfo.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val SourceCodeInfo.Companion.converter: SourceCodeInfoConverter
  get() = SourceCodeInfoConverter

public expect object GeneratedCodeInfoConverter : ProtobufConverter<GeneratedCodeInfo> {
  public object AnnotationConverter : ProtobufConverter<GeneratedCodeInfo.Annotation>
}

public fun GeneratedCodeInfo.toAny(): Any = Any(GeneratedCodeInfo.TYPE_URL,
    with(GeneratedCodeInfoConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<GeneratedCodeInfo> = GeneratedCodeInfoConverter):
    GeneratedCodeInfo {
  if (typeUrl != GeneratedCodeInfo.TYPE_URL) throw
      IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val GeneratedCodeInfo.Companion.converter: GeneratedCodeInfoConverter
  get() = GeneratedCodeInfoConverter
