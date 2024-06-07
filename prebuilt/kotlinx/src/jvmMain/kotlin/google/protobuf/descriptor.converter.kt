// Transform from google/protobuf/descriptor.proto
@file:GeneratorVersion(version = "0.5.2")

package google.protobuf

import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter

public actual object FileDescriptorSetConverter : FileDescriptorSetJvmConverter(),
    ProtobufConverter<FileDescriptorSet>

public actual object FileDescriptorProtoConverter : FileDescriptorProtoJvmConverter(),
    ProtobufConverter<FileDescriptorProto>

public actual object DescriptorProtoConverter : DescriptorProtoJvmConverter(),
    ProtobufConverter<DescriptorProto> {
  public actual object ExtensionRangeConverter :
      DescriptorProtoJvmConverter.ExtensionRangeJvmConverter(),
      ProtobufConverter<DescriptorProto.ExtensionRange>

  public actual object ReservedRangeConverter :
      DescriptorProtoJvmConverter.ReservedRangeJvmConverter(),
      ProtobufConverter<DescriptorProto.ReservedRange>
}

public actual object ExtensionRangeOptionsConverter : ExtensionRangeOptionsJvmConverter(),
    ProtobufConverter<ExtensionRangeOptions> {
  public actual object DeclarationConverter :
      ExtensionRangeOptionsJvmConverter.DeclarationJvmConverter(),
      ProtobufConverter<ExtensionRangeOptions.Declaration>
}

public actual object FieldDescriptorProtoConverter : FieldDescriptorProtoJvmConverter(),
    ProtobufConverter<FieldDescriptorProto>

public actual object OneofDescriptorProtoConverter : OneofDescriptorProtoJvmConverter(),
    ProtobufConverter<OneofDescriptorProto>

public actual object EnumDescriptorProtoConverter : EnumDescriptorProtoJvmConverter(),
    ProtobufConverter<EnumDescriptorProto> {
  public actual object EnumReservedRangeConverter :
      EnumDescriptorProtoJvmConverter.EnumReservedRangeJvmConverter(),
      ProtobufConverter<EnumDescriptorProto.EnumReservedRange>
}

public actual object EnumValueDescriptorProtoConverter : EnumValueDescriptorProtoJvmConverter(),
    ProtobufConverter<EnumValueDescriptorProto>

public actual object ServiceDescriptorProtoConverter : ServiceDescriptorProtoJvmConverter(),
    ProtobufConverter<ServiceDescriptorProto>

public actual object MethodDescriptorProtoConverter : MethodDescriptorProtoJvmConverter(),
    ProtobufConverter<MethodDescriptorProto>

public actual object FileOptionsConverter : FileOptionsJvmConverter(),
    ProtobufConverter<FileOptions>

public actual object MessageOptionsConverter : MessageOptionsJvmConverter(),
    ProtobufConverter<MessageOptions>

public actual object FieldOptionsConverter : FieldOptionsJvmConverter(),
    ProtobufConverter<FieldOptions> {
  public actual object EditionDefaultConverter :
      FieldOptionsJvmConverter.EditionDefaultJvmConverter(),
      ProtobufConverter<FieldOptions.EditionDefault>

  public actual object FeatureSupportConverter :
      FieldOptionsJvmConverter.FeatureSupportJvmConverter(),
      ProtobufConverter<FieldOptions.FeatureSupport>
}

public actual object OneofOptionsConverter : OneofOptionsJvmConverter(),
    ProtobufConverter<OneofOptions>

public actual object EnumOptionsConverter : EnumOptionsJvmConverter(),
    ProtobufConverter<EnumOptions>

public actual object EnumValueOptionsConverter : EnumValueOptionsJvmConverter(),
    ProtobufConverter<EnumValueOptions>

public actual object ServiceOptionsConverter : ServiceOptionsJvmConverter(),
    ProtobufConverter<ServiceOptions>

public actual object MethodOptionsConverter : MethodOptionsJvmConverter(),
    ProtobufConverter<MethodOptions>

public actual object UninterpretedOptionConverter : UninterpretedOptionJvmConverter(),
    ProtobufConverter<UninterpretedOption> {
  public actual object NamePartConverter : UninterpretedOptionJvmConverter.NamePartJvmConverter(),
      ProtobufConverter<UninterpretedOption.NamePart>
}

public actual object FeatureSetConverter : FeatureSetJvmConverter(), ProtobufConverter<FeatureSet>

public actual object FeatureSetDefaultsConverter : FeatureSetDefaultsJvmConverter(),
    ProtobufConverter<FeatureSetDefaults> {
  public actual object FeatureSetEditionDefaultConverter :
      FeatureSetDefaultsJvmConverter.FeatureSetEditionDefaultJvmConverter(),
      ProtobufConverter<FeatureSetDefaults.FeatureSetEditionDefault>
}

public actual object SourceCodeInfoConverter : SourceCodeInfoJvmConverter(),
    ProtobufConverter<SourceCodeInfo> {
  public actual object LocationConverter : SourceCodeInfoJvmConverter.LocationJvmConverter(),
      ProtobufConverter<SourceCodeInfo.Location>
}

public actual object GeneratedCodeInfoConverter : GeneratedCodeInfoJvmConverter(),
    ProtobufConverter<GeneratedCodeInfo> {
  public actual object AnnotationConverter : GeneratedCodeInfoJvmConverter.AnnotationJvmConverter(),
      ProtobufConverter<GeneratedCodeInfo.Annotation>
}
