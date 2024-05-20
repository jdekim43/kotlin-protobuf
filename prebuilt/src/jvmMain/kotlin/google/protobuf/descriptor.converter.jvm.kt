// Transform from google/protobuf/descriptor.proto
@file:GeneratorVersion(version = "0.4.1")

package google.protobuf

import com.google.protobuf.ByteString
import com.google.protobuf.DescriptorProtos
import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper
import kr.jadekim.protobuf.util.asJavaType
import kr.jadekim.protobuf.util.asKotlinType

public open class FileDescriptorSetJvmConverter :
    ProtobufTypeMapper<FileDescriptorSet, DescriptorProtos.FileDescriptorSet> {
  override val descriptor: Descriptors.Descriptor =
      DescriptorProtos.FileDescriptorSet.getDescriptor()

  override val parser: Parser<DescriptorProtos.FileDescriptorSet> =
      DescriptorProtos.FileDescriptorSet.parser()

  override val default: DescriptorProtos.FileDescriptorSet =
      DescriptorProtos.FileDescriptorSet.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.FileDescriptorSet): FileDescriptorSet =
      FileDescriptorSet(
  	`file` = obj.getFileList().map { FileDescriptorProtoConverter.convert(it) },
  )

  override fun convert(obj: FileDescriptorSet): DescriptorProtos.FileDescriptorSet {
    val builder = DescriptorProtos.FileDescriptorSet.newBuilder()
    builder.addAllFile(obj.`file`.map { FileDescriptorProtoConverter.convert(it) })
    return builder.build()
  }
}

public open class FileDescriptorProtoJvmConverter :
    ProtobufTypeMapper<FileDescriptorProto, DescriptorProtos.FileDescriptorProto> {
  override val descriptor: Descriptors.Descriptor =
      DescriptorProtos.FileDescriptorProto.getDescriptor()

  override val parser: Parser<DescriptorProtos.FileDescriptorProto> =
      DescriptorProtos.FileDescriptorProto.parser()

  override val default: DescriptorProtos.FileDescriptorProto =
      DescriptorProtos.FileDescriptorProto.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.FileDescriptorProto): FileDescriptorProto =
      FileDescriptorProto(
  	name = obj.getName(),
  	`package` = obj.getPackage(),
  	dependency = obj.getDependencyList().map { it },
  	publicDependency = obj.getPublicDependencyList().map { it },
  	weakDependency = obj.getWeakDependencyList().map { it },
  	messageType = obj.getMessageTypeList().map { DescriptorProtoConverter.convert(it) },
  	enumType = obj.getEnumTypeList().map { EnumDescriptorProtoConverter.convert(it) },
  	service = obj.getServiceList().map { ServiceDescriptorProtoConverter.convert(it) },
  	extension = obj.getExtensionList().map { FieldDescriptorProtoConverter.convert(it) },
  	options = FileOptionsConverter.convert(obj.getOptions()),
  	sourceCodeInfo = SourceCodeInfoConverter.convert(obj.getSourceCodeInfo()),
  	syntax = obj.getSyntax(),
  	edition = Edition.forNumber(obj.getEdition().number),
  )

  override fun convert(obj: FileDescriptorProto): DescriptorProtos.FileDescriptorProto {
    val builder = DescriptorProtos.FileDescriptorProto.newBuilder()
    val value0 = obj.name
    if (value0 != null) {
      builder.setName(value0)
    }
    val value1 = obj.`package`
    if (value1 != null) {
      builder.setPackage(value1)
    }
    builder.addAllDependency(obj.dependency.map { it })
    builder.addAllPublicDependency(obj.publicDependency.map { it })
    builder.addAllWeakDependency(obj.weakDependency.map { it })
    builder.addAllMessageType(obj.messageType.map { DescriptorProtoConverter.convert(it) })
    builder.addAllEnumType(obj.enumType.map { EnumDescriptorProtoConverter.convert(it) })
    builder.addAllService(obj.service.map { ServiceDescriptorProtoConverter.convert(it) })
    builder.addAllExtension(obj.extension.map { FieldDescriptorProtoConverter.convert(it) })
    val value9 = obj.options
    if (value9 != null) {
      builder.setOptions(FileOptionsConverter.convert(value9))
    }
    val value10 = obj.sourceCodeInfo
    if (value10 != null) {
      builder.setSourceCodeInfo(SourceCodeInfoConverter.convert(value10))
    }
    val value11 = obj.syntax
    if (value11 != null) {
      builder.setSyntax(value11)
    }
    val value12 = obj.edition
    if (value12 != null) {
      builder.setEdition(DescriptorProtos.Edition.forNumber(value12.number))
    }
    return builder.build()
  }
}

public open class DescriptorProtoJvmConverter :
    ProtobufTypeMapper<DescriptorProto, DescriptorProtos.DescriptorProto> {
  override val descriptor: Descriptors.Descriptor = DescriptorProtos.DescriptorProto.getDescriptor()

  override val parser: Parser<DescriptorProtos.DescriptorProto> =
      DescriptorProtos.DescriptorProto.parser()

  override val default: DescriptorProtos.DescriptorProto =
      DescriptorProtos.DescriptorProto.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.DescriptorProto): DescriptorProto = DescriptorProto(
  	name = obj.getName(),
  	`field` = obj.getFieldList().map { FieldDescriptorProtoConverter.convert(it) },
  	extension = obj.getExtensionList().map { FieldDescriptorProtoConverter.convert(it) },
  	nestedType = obj.getNestedTypeList().map { DescriptorProtoConverter.convert(it) },
  	enumType = obj.getEnumTypeList().map { EnumDescriptorProtoConverter.convert(it) },
  	extensionRange = obj.getExtensionRangeList().map {
      DescriptorProtoConverter.ExtensionRangeConverter.convert(it) },
  	oneofDecl = obj.getOneofDeclList().map { OneofDescriptorProtoConverter.convert(it) },
  	options = MessageOptionsConverter.convert(obj.getOptions()),
  	reservedRange = obj.getReservedRangeList().map {
      DescriptorProtoConverter.ReservedRangeConverter.convert(it) },
  	reservedName = obj.getReservedNameList().map { it },
  )

  override fun convert(obj: DescriptorProto): DescriptorProtos.DescriptorProto {
    val builder = DescriptorProtos.DescriptorProto.newBuilder()
    val value0 = obj.name
    if (value0 != null) {
      builder.setName(value0)
    }
    builder.addAllField(obj.`field`.map { FieldDescriptorProtoConverter.convert(it) })
    builder.addAllExtension(obj.extension.map { FieldDescriptorProtoConverter.convert(it) })
    builder.addAllNestedType(obj.nestedType.map { DescriptorProtoConverter.convert(it) })
    builder.addAllEnumType(obj.enumType.map { EnumDescriptorProtoConverter.convert(it) })
    builder.addAllExtensionRange(obj.extensionRange.map {
        DescriptorProtoConverter.ExtensionRangeConverter.convert(it) })
    builder.addAllOneofDecl(obj.oneofDecl.map { OneofDescriptorProtoConverter.convert(it) })
    val value7 = obj.options
    if (value7 != null) {
      builder.setOptions(MessageOptionsConverter.convert(value7))
    }
    builder.addAllReservedRange(obj.reservedRange.map {
        DescriptorProtoConverter.ReservedRangeConverter.convert(it) })
    builder.addAllReservedName(obj.reservedName.map { it })
    return builder.build()
  }

  public open class ExtensionRangeJvmConverter :
      ProtobufTypeMapper<DescriptorProto.ExtensionRange, DescriptorProtos.DescriptorProto.ExtensionRange>
      {
    override val descriptor: Descriptors.Descriptor =
        DescriptorProtos.DescriptorProto.ExtensionRange.getDescriptor()

    override val parser: Parser<DescriptorProtos.DescriptorProto.ExtensionRange> =
        DescriptorProtos.DescriptorProto.ExtensionRange.parser()

    override val default: DescriptorProtos.DescriptorProto.ExtensionRange =
        DescriptorProtos.DescriptorProto.ExtensionRange.getDefaultInstance()

    override fun convert(obj: DescriptorProtos.DescriptorProto.ExtensionRange):
        DescriptorProto.ExtensionRange = DescriptorProto.ExtensionRange(
    	start = obj.getStart(),
    	end = obj.getEnd(),
    	options = ExtensionRangeOptionsConverter.convert(obj.getOptions()),
    )

    override fun convert(obj: DescriptorProto.ExtensionRange):
        DescriptorProtos.DescriptorProto.ExtensionRange {
      val builder = DescriptorProtos.DescriptorProto.ExtensionRange.newBuilder()
      val value0 = obj.start
      if (value0 != null) {
        builder.setStart(value0)
      }
      val value1 = obj.end
      if (value1 != null) {
        builder.setEnd(value1)
      }
      val value2 = obj.options
      if (value2 != null) {
        builder.setOptions(ExtensionRangeOptionsConverter.convert(value2))
      }
      return builder.build()
    }
  }

  public open class ReservedRangeJvmConverter :
      ProtobufTypeMapper<DescriptorProto.ReservedRange, DescriptorProtos.DescriptorProto.ReservedRange>
      {
    override val descriptor: Descriptors.Descriptor =
        DescriptorProtos.DescriptorProto.ReservedRange.getDescriptor()

    override val parser: Parser<DescriptorProtos.DescriptorProto.ReservedRange> =
        DescriptorProtos.DescriptorProto.ReservedRange.parser()

    override val default: DescriptorProtos.DescriptorProto.ReservedRange =
        DescriptorProtos.DescriptorProto.ReservedRange.getDefaultInstance()

    override fun convert(obj: DescriptorProtos.DescriptorProto.ReservedRange):
        DescriptorProto.ReservedRange = DescriptorProto.ReservedRange(
    	start = obj.getStart(),
    	end = obj.getEnd(),
    )

    override fun convert(obj: DescriptorProto.ReservedRange):
        DescriptorProtos.DescriptorProto.ReservedRange {
      val builder = DescriptorProtos.DescriptorProto.ReservedRange.newBuilder()
      val value0 = obj.start
      if (value0 != null) {
        builder.setStart(value0)
      }
      val value1 = obj.end
      if (value1 != null) {
        builder.setEnd(value1)
      }
      return builder.build()
    }
  }
}

public open class ExtensionRangeOptionsJvmConverter :
    ProtobufTypeMapper<ExtensionRangeOptions, DescriptorProtos.ExtensionRangeOptions> {
  override val descriptor: Descriptors.Descriptor =
      DescriptorProtos.ExtensionRangeOptions.getDescriptor()

  override val parser: Parser<DescriptorProtos.ExtensionRangeOptions> =
      DescriptorProtos.ExtensionRangeOptions.parser()

  override val default: DescriptorProtos.ExtensionRangeOptions =
      DescriptorProtos.ExtensionRangeOptions.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.ExtensionRangeOptions): ExtensionRangeOptions =
      ExtensionRangeOptions(
  	uninterpretedOption = obj.getUninterpretedOptionList().map {
      UninterpretedOptionConverter.convert(it) },
  	declaration = obj.getDeclarationList().map {
      ExtensionRangeOptionsConverter.DeclarationConverter.convert(it) },
  	features = FeatureSetConverter.convert(obj.getFeatures()),
  	verification = ExtensionRangeOptions.VerificationState.forNumber(obj.getVerification().number),
  )

  override fun convert(obj: ExtensionRangeOptions): DescriptorProtos.ExtensionRangeOptions {
    val builder = DescriptorProtos.ExtensionRangeOptions.newBuilder()
    builder.addAllUninterpretedOption(obj.uninterpretedOption.map {
        UninterpretedOptionConverter.convert(it) })
    builder.addAllDeclaration(obj.declaration.map {
        ExtensionRangeOptionsConverter.DeclarationConverter.convert(it) })
    val value2 = obj.features
    if (value2 != null) {
      builder.setFeatures(FeatureSetConverter.convert(value2))
    }
    val value3 = obj.verification
    if (value3 != null) {
      builder.setVerification(DescriptorProtos.ExtensionRangeOptions.VerificationState.forNumber(value3.number))
    }
    return builder.build()
  }

  public open class DeclarationJvmConverter :
      ProtobufTypeMapper<ExtensionRangeOptions.Declaration, DescriptorProtos.ExtensionRangeOptions.Declaration>
      {
    override val descriptor: Descriptors.Descriptor =
        DescriptorProtos.ExtensionRangeOptions.Declaration.getDescriptor()

    override val parser: Parser<DescriptorProtos.ExtensionRangeOptions.Declaration> =
        DescriptorProtos.ExtensionRangeOptions.Declaration.parser()

    override val default: DescriptorProtos.ExtensionRangeOptions.Declaration =
        DescriptorProtos.ExtensionRangeOptions.Declaration.getDefaultInstance()

    override fun convert(obj: DescriptorProtos.ExtensionRangeOptions.Declaration):
        ExtensionRangeOptions.Declaration = ExtensionRangeOptions.Declaration(
    	number = obj.getNumber(),
    	fullName = obj.getFullName(),
    	type = obj.getType(),
    	reserved = obj.getReserved(),
    	repeated = obj.getRepeated(),
    )

    override fun convert(obj: ExtensionRangeOptions.Declaration):
        DescriptorProtos.ExtensionRangeOptions.Declaration {
      val builder = DescriptorProtos.ExtensionRangeOptions.Declaration.newBuilder()
      val value0 = obj.number
      if (value0 != null) {
        builder.setNumber(value0)
      }
      val value1 = obj.fullName
      if (value1 != null) {
        builder.setFullName(value1)
      }
      val value2 = obj.type
      if (value2 != null) {
        builder.setType(value2)
      }
      val value3 = obj.reserved
      if (value3 != null) {
        builder.setReserved(value3)
      }
      val value4 = obj.repeated
      if (value4 != null) {
        builder.setRepeated(value4)
      }
      return builder.build()
    }
  }
}

public open class FieldDescriptorProtoJvmConverter :
    ProtobufTypeMapper<FieldDescriptorProto, DescriptorProtos.FieldDescriptorProto> {
  override val descriptor: Descriptors.Descriptor =
      DescriptorProtos.FieldDescriptorProto.getDescriptor()

  override val parser: Parser<DescriptorProtos.FieldDescriptorProto> =
      DescriptorProtos.FieldDescriptorProto.parser()

  override val default: DescriptorProtos.FieldDescriptorProto =
      DescriptorProtos.FieldDescriptorProto.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.FieldDescriptorProto): FieldDescriptorProto =
      FieldDescriptorProto(
  	name = obj.getName(),
  	number = obj.getNumber(),
  	label = FieldDescriptorProto.Label.forNumber(obj.getLabel().number),
  	type = FieldDescriptorProto.Type.forNumber(obj.getType().number),
  	typeName = obj.getTypeName(),
  	extendee = obj.getExtendee(),
  	defaultValue = obj.getDefaultValue(),
  	oneofIndex = obj.getOneofIndex(),
  	jsonName = obj.getJsonName(),
  	options = FieldOptionsConverter.convert(obj.getOptions()),
  	proto3Optional = obj.getProto3Optional(),
  )

  override fun convert(obj: FieldDescriptorProto): DescriptorProtos.FieldDescriptorProto {
    val builder = DescriptorProtos.FieldDescriptorProto.newBuilder()
    val value0 = obj.name
    if (value0 != null) {
      builder.setName(value0)
    }
    val value1 = obj.number
    if (value1 != null) {
      builder.setNumber(value1)
    }
    val value2 = obj.label
    if (value2 != null) {
      builder.setLabel(DescriptorProtos.FieldDescriptorProto.Label.forNumber(value2.number))
    }
    val value3 = obj.type
    if (value3 != null) {
      builder.setType(DescriptorProtos.FieldDescriptorProto.Type.forNumber(value3.number))
    }
    val value4 = obj.typeName
    if (value4 != null) {
      builder.setTypeName(value4)
    }
    val value5 = obj.extendee
    if (value5 != null) {
      builder.setExtendee(value5)
    }
    val value6 = obj.defaultValue
    if (value6 != null) {
      builder.setDefaultValue(value6)
    }
    val value7 = obj.oneofIndex
    if (value7 != null) {
      builder.setOneofIndex(value7)
    }
    val value8 = obj.jsonName
    if (value8 != null) {
      builder.setJsonName(value8)
    }
    val value9 = obj.options
    if (value9 != null) {
      builder.setOptions(FieldOptionsConverter.convert(value9))
    }
    val value10 = obj.proto3Optional
    if (value10 != null) {
      builder.setProto3Optional(value10)
    }
    return builder.build()
  }
}

public open class OneofDescriptorProtoJvmConverter :
    ProtobufTypeMapper<OneofDescriptorProto, DescriptorProtos.OneofDescriptorProto> {
  override val descriptor: Descriptors.Descriptor =
      DescriptorProtos.OneofDescriptorProto.getDescriptor()

  override val parser: Parser<DescriptorProtos.OneofDescriptorProto> =
      DescriptorProtos.OneofDescriptorProto.parser()

  override val default: DescriptorProtos.OneofDescriptorProto =
      DescriptorProtos.OneofDescriptorProto.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.OneofDescriptorProto): OneofDescriptorProto =
      OneofDescriptorProto(
  	name = obj.getName(),
  	options = OneofOptionsConverter.convert(obj.getOptions()),
  )

  override fun convert(obj: OneofDescriptorProto): DescriptorProtos.OneofDescriptorProto {
    val builder = DescriptorProtos.OneofDescriptorProto.newBuilder()
    val value0 = obj.name
    if (value0 != null) {
      builder.setName(value0)
    }
    val value1 = obj.options
    if (value1 != null) {
      builder.setOptions(OneofOptionsConverter.convert(value1))
    }
    return builder.build()
  }
}

public open class EnumDescriptorProtoJvmConverter :
    ProtobufTypeMapper<EnumDescriptorProto, DescriptorProtos.EnumDescriptorProto> {
  override val descriptor: Descriptors.Descriptor =
      DescriptorProtos.EnumDescriptorProto.getDescriptor()

  override val parser: Parser<DescriptorProtos.EnumDescriptorProto> =
      DescriptorProtos.EnumDescriptorProto.parser()

  override val default: DescriptorProtos.EnumDescriptorProto =
      DescriptorProtos.EnumDescriptorProto.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.EnumDescriptorProto): EnumDescriptorProto =
      EnumDescriptorProto(
  	name = obj.getName(),
  	`value` = obj.getValueList().map { EnumValueDescriptorProtoConverter.convert(it) },
  	options = EnumOptionsConverter.convert(obj.getOptions()),
  	reservedRange = obj.getReservedRangeList().map {
      EnumDescriptorProtoConverter.EnumReservedRangeConverter.convert(it) },
  	reservedName = obj.getReservedNameList().map { it },
  )

  override fun convert(obj: EnumDescriptorProto): DescriptorProtos.EnumDescriptorProto {
    val builder = DescriptorProtos.EnumDescriptorProto.newBuilder()
    val value0 = obj.name
    if (value0 != null) {
      builder.setName(value0)
    }
    builder.addAllValue(obj.`value`.map { EnumValueDescriptorProtoConverter.convert(it) })
    val value2 = obj.options
    if (value2 != null) {
      builder.setOptions(EnumOptionsConverter.convert(value2))
    }
    builder.addAllReservedRange(obj.reservedRange.map {
        EnumDescriptorProtoConverter.EnumReservedRangeConverter.convert(it) })
    builder.addAllReservedName(obj.reservedName.map { it })
    return builder.build()
  }

  public open class EnumReservedRangeJvmConverter :
      ProtobufTypeMapper<EnumDescriptorProto.EnumReservedRange, DescriptorProtos.EnumDescriptorProto.EnumReservedRange>
      {
    override val descriptor: Descriptors.Descriptor =
        DescriptorProtos.EnumDescriptorProto.EnumReservedRange.getDescriptor()

    override val parser: Parser<DescriptorProtos.EnumDescriptorProto.EnumReservedRange> =
        DescriptorProtos.EnumDescriptorProto.EnumReservedRange.parser()

    override val default: DescriptorProtos.EnumDescriptorProto.EnumReservedRange =
        DescriptorProtos.EnumDescriptorProto.EnumReservedRange.getDefaultInstance()

    override fun convert(obj: DescriptorProtos.EnumDescriptorProto.EnumReservedRange):
        EnumDescriptorProto.EnumReservedRange = EnumDescriptorProto.EnumReservedRange(
    	start = obj.getStart(),
    	end = obj.getEnd(),
    )

    override fun convert(obj: EnumDescriptorProto.EnumReservedRange):
        DescriptorProtos.EnumDescriptorProto.EnumReservedRange {
      val builder = DescriptorProtos.EnumDescriptorProto.EnumReservedRange.newBuilder()
      val value0 = obj.start
      if (value0 != null) {
        builder.setStart(value0)
      }
      val value1 = obj.end
      if (value1 != null) {
        builder.setEnd(value1)
      }
      return builder.build()
    }
  }
}

public open class EnumValueDescriptorProtoJvmConverter :
    ProtobufTypeMapper<EnumValueDescriptorProto, DescriptorProtos.EnumValueDescriptorProto> {
  override val descriptor: Descriptors.Descriptor =
      DescriptorProtos.EnumValueDescriptorProto.getDescriptor()

  override val parser: Parser<DescriptorProtos.EnumValueDescriptorProto> =
      DescriptorProtos.EnumValueDescriptorProto.parser()

  override val default: DescriptorProtos.EnumValueDescriptorProto =
      DescriptorProtos.EnumValueDescriptorProto.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.EnumValueDescriptorProto): EnumValueDescriptorProto =
      EnumValueDescriptorProto(
  	name = obj.getName(),
  	number = obj.getNumber(),
  	options = EnumValueOptionsConverter.convert(obj.getOptions()),
  )

  override fun convert(obj: EnumValueDescriptorProto): DescriptorProtos.EnumValueDescriptorProto {
    val builder = DescriptorProtos.EnumValueDescriptorProto.newBuilder()
    val value0 = obj.name
    if (value0 != null) {
      builder.setName(value0)
    }
    val value1 = obj.number
    if (value1 != null) {
      builder.setNumber(value1)
    }
    val value2 = obj.options
    if (value2 != null) {
      builder.setOptions(EnumValueOptionsConverter.convert(value2))
    }
    return builder.build()
  }
}

public open class ServiceDescriptorProtoJvmConverter :
    ProtobufTypeMapper<ServiceDescriptorProto, DescriptorProtos.ServiceDescriptorProto> {
  override val descriptor: Descriptors.Descriptor =
      DescriptorProtos.ServiceDescriptorProto.getDescriptor()

  override val parser: Parser<DescriptorProtos.ServiceDescriptorProto> =
      DescriptorProtos.ServiceDescriptorProto.parser()

  override val default: DescriptorProtos.ServiceDescriptorProto =
      DescriptorProtos.ServiceDescriptorProto.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.ServiceDescriptorProto): ServiceDescriptorProto =
      ServiceDescriptorProto(
  	name = obj.getName(),
  	method = obj.getMethodList().map { MethodDescriptorProtoConverter.convert(it) },
  	options = ServiceOptionsConverter.convert(obj.getOptions()),
  )

  override fun convert(obj: ServiceDescriptorProto): DescriptorProtos.ServiceDescriptorProto {
    val builder = DescriptorProtos.ServiceDescriptorProto.newBuilder()
    val value0 = obj.name
    if (value0 != null) {
      builder.setName(value0)
    }
    builder.addAllMethod(obj.method.map { MethodDescriptorProtoConverter.convert(it) })
    val value2 = obj.options
    if (value2 != null) {
      builder.setOptions(ServiceOptionsConverter.convert(value2))
    }
    return builder.build()
  }
}

public open class MethodDescriptorProtoJvmConverter :
    ProtobufTypeMapper<MethodDescriptorProto, DescriptorProtos.MethodDescriptorProto> {
  override val descriptor: Descriptors.Descriptor =
      DescriptorProtos.MethodDescriptorProto.getDescriptor()

  override val parser: Parser<DescriptorProtos.MethodDescriptorProto> =
      DescriptorProtos.MethodDescriptorProto.parser()

  override val default: DescriptorProtos.MethodDescriptorProto =
      DescriptorProtos.MethodDescriptorProto.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.MethodDescriptorProto): MethodDescriptorProto =
      MethodDescriptorProto(
  	name = obj.getName(),
  	inputType = obj.getInputType(),
  	outputType = obj.getOutputType(),
  	options = MethodOptionsConverter.convert(obj.getOptions()),
  	clientStreaming = obj.getClientStreaming(),
  	serverStreaming = obj.getServerStreaming(),
  )

  override fun convert(obj: MethodDescriptorProto): DescriptorProtos.MethodDescriptorProto {
    val builder = DescriptorProtos.MethodDescriptorProto.newBuilder()
    val value0 = obj.name
    if (value0 != null) {
      builder.setName(value0)
    }
    val value1 = obj.inputType
    if (value1 != null) {
      builder.setInputType(value1)
    }
    val value2 = obj.outputType
    if (value2 != null) {
      builder.setOutputType(value2)
    }
    val value3 = obj.options
    if (value3 != null) {
      builder.setOptions(MethodOptionsConverter.convert(value3))
    }
    val value4 = obj.clientStreaming
    if (value4 != null) {
      builder.setClientStreaming(value4)
    }
    val value5 = obj.serverStreaming
    if (value5 != null) {
      builder.setServerStreaming(value5)
    }
    return builder.build()
  }
}

public open class FileOptionsJvmConverter :
    ProtobufTypeMapper<FileOptions, DescriptorProtos.FileOptions> {
  override val descriptor: Descriptors.Descriptor = DescriptorProtos.FileOptions.getDescriptor()

  override val parser: Parser<DescriptorProtos.FileOptions> = DescriptorProtos.FileOptions.parser()

  override val default: DescriptorProtos.FileOptions =
      DescriptorProtos.FileOptions.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.FileOptions): FileOptions = FileOptions(
  	javaPackage = obj.getJavaPackage(),
  	javaOuterClassname = obj.getJavaOuterClassname(),
  	javaMultipleFiles = obj.getJavaMultipleFiles(),
  	javaGenerateEqualsAndHash = obj.getJavaGenerateEqualsAndHash(),
  	javaStringCheckUtf8 = obj.getJavaStringCheckUtf8(),
  	optimizeFor = FileOptions.OptimizeMode.forNumber(obj.getOptimizeFor().number),
  	goPackage = obj.getGoPackage(),
  	ccGenericServices = obj.getCcGenericServices(),
  	javaGenericServices = obj.getJavaGenericServices(),
  	pyGenericServices = obj.getPyGenericServices(),
  	phpGenericServices = obj.getPhpGenericServices(),
  	deprecated = obj.getDeprecated(),
  	ccEnableArenas = obj.getCcEnableArenas(),
  	objcClassPrefix = obj.getObjcClassPrefix(),
  	csharpNamespace = obj.getCsharpNamespace(),
  	swiftPrefix = obj.getSwiftPrefix(),
  	phpClassPrefix = obj.getPhpClassPrefix(),
  	phpNamespace = obj.getPhpNamespace(),
  	phpMetadataNamespace = obj.getPhpMetadataNamespace(),
  	rubyPackage = obj.getRubyPackage(),
  	features = FeatureSetConverter.convert(obj.getFeatures()),
  	uninterpretedOption = obj.getUninterpretedOptionList().map {
      UninterpretedOptionConverter.convert(it) },
  )

  override fun convert(obj: FileOptions): DescriptorProtos.FileOptions {
    val builder = DescriptorProtos.FileOptions.newBuilder()
    val value0 = obj.javaPackage
    if (value0 != null) {
      builder.setJavaPackage(value0)
    }
    val value1 = obj.javaOuterClassname
    if (value1 != null) {
      builder.setJavaOuterClassname(value1)
    }
    val value2 = obj.javaMultipleFiles
    if (value2 != null) {
      builder.setJavaMultipleFiles(value2)
    }
    val value3 = obj.javaGenerateEqualsAndHash
    if (value3 != null) {
      builder.setJavaGenerateEqualsAndHash(value3)
    }
    val value4 = obj.javaStringCheckUtf8
    if (value4 != null) {
      builder.setJavaStringCheckUtf8(value4)
    }
    val value5 = obj.optimizeFor
    if (value5 != null) {
      builder.setOptimizeFor(DescriptorProtos.FileOptions.OptimizeMode.forNumber(value5.number))
    }
    val value6 = obj.goPackage
    if (value6 != null) {
      builder.setGoPackage(value6)
    }
    val value7 = obj.ccGenericServices
    if (value7 != null) {
      builder.setCcGenericServices(value7)
    }
    val value8 = obj.javaGenericServices
    if (value8 != null) {
      builder.setJavaGenericServices(value8)
    }
    val value9 = obj.pyGenericServices
    if (value9 != null) {
      builder.setPyGenericServices(value9)
    }
    val value10 = obj.phpGenericServices
    if (value10 != null) {
      builder.setPhpGenericServices(value10)
    }
    val value11 = obj.deprecated
    if (value11 != null) {
      builder.setDeprecated(value11)
    }
    val value12 = obj.ccEnableArenas
    if (value12 != null) {
      builder.setCcEnableArenas(value12)
    }
    val value13 = obj.objcClassPrefix
    if (value13 != null) {
      builder.setObjcClassPrefix(value13)
    }
    val value14 = obj.csharpNamespace
    if (value14 != null) {
      builder.setCsharpNamespace(value14)
    }
    val value15 = obj.swiftPrefix
    if (value15 != null) {
      builder.setSwiftPrefix(value15)
    }
    val value16 = obj.phpClassPrefix
    if (value16 != null) {
      builder.setPhpClassPrefix(value16)
    }
    val value17 = obj.phpNamespace
    if (value17 != null) {
      builder.setPhpNamespace(value17)
    }
    val value18 = obj.phpMetadataNamespace
    if (value18 != null) {
      builder.setPhpMetadataNamespace(value18)
    }
    val value19 = obj.rubyPackage
    if (value19 != null) {
      builder.setRubyPackage(value19)
    }
    val value20 = obj.features
    if (value20 != null) {
      builder.setFeatures(FeatureSetConverter.convert(value20))
    }
    builder.addAllUninterpretedOption(obj.uninterpretedOption.map {
        UninterpretedOptionConverter.convert(it) })
    return builder.build()
  }
}

public open class MessageOptionsJvmConverter :
    ProtobufTypeMapper<MessageOptions, DescriptorProtos.MessageOptions> {
  override val descriptor: Descriptors.Descriptor = DescriptorProtos.MessageOptions.getDescriptor()

  override val parser: Parser<DescriptorProtos.MessageOptions> =
      DescriptorProtos.MessageOptions.parser()

  override val default: DescriptorProtos.MessageOptions =
      DescriptorProtos.MessageOptions.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.MessageOptions): MessageOptions = MessageOptions(
  	messageSetWireFormat = obj.getMessageSetWireFormat(),
  	noStandardDescriptorAccessor = obj.getNoStandardDescriptorAccessor(),
  	deprecated = obj.getDeprecated(),
  	mapEntry = obj.getMapEntry(),
  	deprecatedLegacyJsonFieldConflicts = obj.getDeprecatedLegacyJsonFieldConflicts(),
  	features = FeatureSetConverter.convert(obj.getFeatures()),
  	uninterpretedOption = obj.getUninterpretedOptionList().map {
      UninterpretedOptionConverter.convert(it) },
  )

  override fun convert(obj: MessageOptions): DescriptorProtos.MessageOptions {
    val builder = DescriptorProtos.MessageOptions.newBuilder()
    val value0 = obj.messageSetWireFormat
    if (value0 != null) {
      builder.setMessageSetWireFormat(value0)
    }
    val value1 = obj.noStandardDescriptorAccessor
    if (value1 != null) {
      builder.setNoStandardDescriptorAccessor(value1)
    }
    val value2 = obj.deprecated
    if (value2 != null) {
      builder.setDeprecated(value2)
    }
    val value3 = obj.mapEntry
    if (value3 != null) {
      builder.setMapEntry(value3)
    }
    val value4 = obj.deprecatedLegacyJsonFieldConflicts
    if (value4 != null) {
      builder.setDeprecatedLegacyJsonFieldConflicts(value4)
    }
    val value5 = obj.features
    if (value5 != null) {
      builder.setFeatures(FeatureSetConverter.convert(value5))
    }
    builder.addAllUninterpretedOption(obj.uninterpretedOption.map {
        UninterpretedOptionConverter.convert(it) })
    return builder.build()
  }
}

public open class FieldOptionsJvmConverter :
    ProtobufTypeMapper<FieldOptions, DescriptorProtos.FieldOptions> {
  override val descriptor: Descriptors.Descriptor = DescriptorProtos.FieldOptions.getDescriptor()

  override val parser: Parser<DescriptorProtos.FieldOptions> =
      DescriptorProtos.FieldOptions.parser()

  override val default: DescriptorProtos.FieldOptions =
      DescriptorProtos.FieldOptions.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.FieldOptions): FieldOptions = FieldOptions(
  	ctype = FieldOptions.CType.forNumber(obj.getCtype().number),
  	packed = obj.getPacked(),
  	jstype = FieldOptions.JSType.forNumber(obj.getJstype().number),
  	lazy = obj.getLazy(),
  	unverifiedLazy = obj.getUnverifiedLazy(),
  	deprecated = obj.getDeprecated(),
  	weak = obj.getWeak(),
  	debugRedact = obj.getDebugRedact(),
  	retention = FieldOptions.OptionRetention.forNumber(obj.getRetention().number),
  	targets = obj.getTargetsList().map { FieldOptions.OptionTargetType.forNumber(it.number) },
  	editionDefaults = obj.getEditionDefaultsList().map {
      FieldOptionsConverter.EditionDefaultConverter.convert(it) },
  	features = FeatureSetConverter.convert(obj.getFeatures()),
  	uninterpretedOption = obj.getUninterpretedOptionList().map {
      UninterpretedOptionConverter.convert(it) },
  )

  override fun convert(obj: FieldOptions): DescriptorProtos.FieldOptions {
    val builder = DescriptorProtos.FieldOptions.newBuilder()
    val value0 = obj.ctype
    if (value0 != null) {
      builder.setCtype(DescriptorProtos.FieldOptions.CType.forNumber(value0.number))
    }
    val value1 = obj.packed
    if (value1 != null) {
      builder.setPacked(value1)
    }
    val value2 = obj.jstype
    if (value2 != null) {
      builder.setJstype(DescriptorProtos.FieldOptions.JSType.forNumber(value2.number))
    }
    val value3 = obj.lazy
    if (value3 != null) {
      builder.setLazy(value3)
    }
    val value4 = obj.unverifiedLazy
    if (value4 != null) {
      builder.setUnverifiedLazy(value4)
    }
    val value5 = obj.deprecated
    if (value5 != null) {
      builder.setDeprecated(value5)
    }
    val value6 = obj.weak
    if (value6 != null) {
      builder.setWeak(value6)
    }
    val value7 = obj.debugRedact
    if (value7 != null) {
      builder.setDebugRedact(value7)
    }
    val value8 = obj.retention
    if (value8 != null) {
      builder.setRetention(DescriptorProtos.FieldOptions.OptionRetention.forNumber(value8.number))
    }
    builder.addAllTargets(obj.targets.map {
        DescriptorProtos.FieldOptions.OptionTargetType.forNumber(it.number) })
    builder.addAllEditionDefaults(obj.editionDefaults.map {
        FieldOptionsConverter.EditionDefaultConverter.convert(it) })
    val value11 = obj.features
    if (value11 != null) {
      builder.setFeatures(FeatureSetConverter.convert(value11))
    }
    builder.addAllUninterpretedOption(obj.uninterpretedOption.map {
        UninterpretedOptionConverter.convert(it) })
    return builder.build()
  }

  public open class EditionDefaultJvmConverter :
      ProtobufTypeMapper<FieldOptions.EditionDefault, DescriptorProtos.FieldOptions.EditionDefault>
      {
    override val descriptor: Descriptors.Descriptor =
        DescriptorProtos.FieldOptions.EditionDefault.getDescriptor()

    override val parser: Parser<DescriptorProtos.FieldOptions.EditionDefault> =
        DescriptorProtos.FieldOptions.EditionDefault.parser()

    override val default: DescriptorProtos.FieldOptions.EditionDefault =
        DescriptorProtos.FieldOptions.EditionDefault.getDefaultInstance()

    override fun convert(obj: DescriptorProtos.FieldOptions.EditionDefault):
        FieldOptions.EditionDefault = FieldOptions.EditionDefault(
    	edition = Edition.forNumber(obj.getEdition().number),
    	`value` = obj.getValue(),
    )

    override fun convert(obj: FieldOptions.EditionDefault):
        DescriptorProtos.FieldOptions.EditionDefault {
      val builder = DescriptorProtos.FieldOptions.EditionDefault.newBuilder()
      val value0 = obj.edition
      if (value0 != null) {
        builder.setEdition(DescriptorProtos.Edition.forNumber(value0.number))
      }
      val value1 = obj.`value`
      if (value1 != null) {
        builder.setValue(value1)
      }
      return builder.build()
    }
  }
}

public open class OneofOptionsJvmConverter :
    ProtobufTypeMapper<OneofOptions, DescriptorProtos.OneofOptions> {
  override val descriptor: Descriptors.Descriptor = DescriptorProtos.OneofOptions.getDescriptor()

  override val parser: Parser<DescriptorProtos.OneofOptions> =
      DescriptorProtos.OneofOptions.parser()

  override val default: DescriptorProtos.OneofOptions =
      DescriptorProtos.OneofOptions.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.OneofOptions): OneofOptions = OneofOptions(
  	features = FeatureSetConverter.convert(obj.getFeatures()),
  	uninterpretedOption = obj.getUninterpretedOptionList().map {
      UninterpretedOptionConverter.convert(it) },
  )

  override fun convert(obj: OneofOptions): DescriptorProtos.OneofOptions {
    val builder = DescriptorProtos.OneofOptions.newBuilder()
    val value0 = obj.features
    if (value0 != null) {
      builder.setFeatures(FeatureSetConverter.convert(value0))
    }
    builder.addAllUninterpretedOption(obj.uninterpretedOption.map {
        UninterpretedOptionConverter.convert(it) })
    return builder.build()
  }
}

public open class EnumOptionsJvmConverter :
    ProtobufTypeMapper<EnumOptions, DescriptorProtos.EnumOptions> {
  override val descriptor: Descriptors.Descriptor = DescriptorProtos.EnumOptions.getDescriptor()

  override val parser: Parser<DescriptorProtos.EnumOptions> = DescriptorProtos.EnumOptions.parser()

  override val default: DescriptorProtos.EnumOptions =
      DescriptorProtos.EnumOptions.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.EnumOptions): EnumOptions = EnumOptions(
  	allowAlias = obj.getAllowAlias(),
  	deprecated = obj.getDeprecated(),
  	deprecatedLegacyJsonFieldConflicts = obj.getDeprecatedLegacyJsonFieldConflicts(),
  	features = FeatureSetConverter.convert(obj.getFeatures()),
  	uninterpretedOption = obj.getUninterpretedOptionList().map {
      UninterpretedOptionConverter.convert(it) },
  )

  override fun convert(obj: EnumOptions): DescriptorProtos.EnumOptions {
    val builder = DescriptorProtos.EnumOptions.newBuilder()
    val value0 = obj.allowAlias
    if (value0 != null) {
      builder.setAllowAlias(value0)
    }
    val value1 = obj.deprecated
    if (value1 != null) {
      builder.setDeprecated(value1)
    }
    val value2 = obj.deprecatedLegacyJsonFieldConflicts
    if (value2 != null) {
      builder.setDeprecatedLegacyJsonFieldConflicts(value2)
    }
    val value3 = obj.features
    if (value3 != null) {
      builder.setFeatures(FeatureSetConverter.convert(value3))
    }
    builder.addAllUninterpretedOption(obj.uninterpretedOption.map {
        UninterpretedOptionConverter.convert(it) })
    return builder.build()
  }
}

public open class EnumValueOptionsJvmConverter :
    ProtobufTypeMapper<EnumValueOptions, DescriptorProtos.EnumValueOptions> {
  override val descriptor: Descriptors.Descriptor =
      DescriptorProtos.EnumValueOptions.getDescriptor()

  override val parser: Parser<DescriptorProtos.EnumValueOptions> =
      DescriptorProtos.EnumValueOptions.parser()

  override val default: DescriptorProtos.EnumValueOptions =
      DescriptorProtos.EnumValueOptions.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.EnumValueOptions): EnumValueOptions = EnumValueOptions(
  	deprecated = obj.getDeprecated(),
  	features = FeatureSetConverter.convert(obj.getFeatures()),
  	debugRedact = obj.getDebugRedact(),
  	uninterpretedOption = obj.getUninterpretedOptionList().map {
      UninterpretedOptionConverter.convert(it) },
  )

  override fun convert(obj: EnumValueOptions): DescriptorProtos.EnumValueOptions {
    val builder = DescriptorProtos.EnumValueOptions.newBuilder()
    val value0 = obj.deprecated
    if (value0 != null) {
      builder.setDeprecated(value0)
    }
    val value1 = obj.features
    if (value1 != null) {
      builder.setFeatures(FeatureSetConverter.convert(value1))
    }
    val value2 = obj.debugRedact
    if (value2 != null) {
      builder.setDebugRedact(value2)
    }
    builder.addAllUninterpretedOption(obj.uninterpretedOption.map {
        UninterpretedOptionConverter.convert(it) })
    return builder.build()
  }
}

public open class ServiceOptionsJvmConverter :
    ProtobufTypeMapper<ServiceOptions, DescriptorProtos.ServiceOptions> {
  override val descriptor: Descriptors.Descriptor = DescriptorProtos.ServiceOptions.getDescriptor()

  override val parser: Parser<DescriptorProtos.ServiceOptions> =
      DescriptorProtos.ServiceOptions.parser()

  override val default: DescriptorProtos.ServiceOptions =
      DescriptorProtos.ServiceOptions.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.ServiceOptions): ServiceOptions = ServiceOptions(
  	features = FeatureSetConverter.convert(obj.getFeatures()),
  	deprecated = obj.getDeprecated(),
  	uninterpretedOption = obj.getUninterpretedOptionList().map {
      UninterpretedOptionConverter.convert(it) },
  )

  override fun convert(obj: ServiceOptions): DescriptorProtos.ServiceOptions {
    val builder = DescriptorProtos.ServiceOptions.newBuilder()
    val value0 = obj.features
    if (value0 != null) {
      builder.setFeatures(FeatureSetConverter.convert(value0))
    }
    val value1 = obj.deprecated
    if (value1 != null) {
      builder.setDeprecated(value1)
    }
    builder.addAllUninterpretedOption(obj.uninterpretedOption.map {
        UninterpretedOptionConverter.convert(it) })
    return builder.build()
  }
}

public open class MethodOptionsJvmConverter :
    ProtobufTypeMapper<MethodOptions, DescriptorProtos.MethodOptions> {
  override val descriptor: Descriptors.Descriptor = DescriptorProtos.MethodOptions.getDescriptor()

  override val parser: Parser<DescriptorProtos.MethodOptions> =
      DescriptorProtos.MethodOptions.parser()

  override val default: DescriptorProtos.MethodOptions =
      DescriptorProtos.MethodOptions.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.MethodOptions): MethodOptions = MethodOptions(
  	deprecated = obj.getDeprecated(),
  	idempotencyLevel = MethodOptions.IdempotencyLevel.forNumber(obj.getIdempotencyLevel().number),
  	features = FeatureSetConverter.convert(obj.getFeatures()),
  	uninterpretedOption = obj.getUninterpretedOptionList().map {
      UninterpretedOptionConverter.convert(it) },
  )

  override fun convert(obj: MethodOptions): DescriptorProtos.MethodOptions {
    val builder = DescriptorProtos.MethodOptions.newBuilder()
    val value0 = obj.deprecated
    if (value0 != null) {
      builder.setDeprecated(value0)
    }
    val value1 = obj.idempotencyLevel
    if (value1 != null) {
      builder.setIdempotencyLevel(DescriptorProtos.MethodOptions.IdempotencyLevel.forNumber(value1.number))
    }
    val value2 = obj.features
    if (value2 != null) {
      builder.setFeatures(FeatureSetConverter.convert(value2))
    }
    builder.addAllUninterpretedOption(obj.uninterpretedOption.map {
        UninterpretedOptionConverter.convert(it) })
    return builder.build()
  }
}

public open class UninterpretedOptionJvmConverter :
    ProtobufTypeMapper<UninterpretedOption, DescriptorProtos.UninterpretedOption> {
  override val descriptor: Descriptors.Descriptor =
      DescriptorProtos.UninterpretedOption.getDescriptor()

  override val parser: Parser<DescriptorProtos.UninterpretedOption> =
      DescriptorProtos.UninterpretedOption.parser()

  override val default: DescriptorProtos.UninterpretedOption =
      DescriptorProtos.UninterpretedOption.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.UninterpretedOption): UninterpretedOption =
      UninterpretedOption(
  	name = obj.getNameList().map { UninterpretedOptionConverter.NamePartConverter.convert(it) },
  	identifierValue = obj.getIdentifierValue(),
  	positiveIntValue = obj.getPositiveIntValue().asKotlinType,
  	negativeIntValue = obj.getNegativeIntValue(),
  	doubleValue = obj.getDoubleValue(),
  	stringValue = obj.getStringValue().toByteArray(),
  	aggregateValue = obj.getAggregateValue(),
  )

  override fun convert(obj: UninterpretedOption): DescriptorProtos.UninterpretedOption {
    val builder = DescriptorProtos.UninterpretedOption.newBuilder()
    builder.addAllName(obj.name.map { UninterpretedOptionConverter.NamePartConverter.convert(it) })
    val value1 = obj.identifierValue
    if (value1 != null) {
      builder.setIdentifierValue(value1)
    }
    val value2 = obj.positiveIntValue
    if (value2 != null) {
      builder.setPositiveIntValue(value2.asJavaType)
    }
    val value3 = obj.negativeIntValue
    if (value3 != null) {
      builder.setNegativeIntValue(value3)
    }
    val value4 = obj.doubleValue
    if (value4 != null) {
      builder.setDoubleValue(value4)
    }
    val value5 = obj.stringValue
    if (value5 != null) {
      builder.setStringValue(ByteString.copyFrom(value5))
    }
    val value6 = obj.aggregateValue
    if (value6 != null) {
      builder.setAggregateValue(value6)
    }
    return builder.build()
  }

  public open class NamePartJvmConverter :
      ProtobufTypeMapper<UninterpretedOption.NamePart, DescriptorProtos.UninterpretedOption.NamePart>
      {
    override val descriptor: Descriptors.Descriptor =
        DescriptorProtos.UninterpretedOption.NamePart.getDescriptor()

    override val parser: Parser<DescriptorProtos.UninterpretedOption.NamePart> =
        DescriptorProtos.UninterpretedOption.NamePart.parser()

    override val default: DescriptorProtos.UninterpretedOption.NamePart =
        DescriptorProtos.UninterpretedOption.NamePart.getDefaultInstance()

    override fun convert(obj: DescriptorProtos.UninterpretedOption.NamePart):
        UninterpretedOption.NamePart = UninterpretedOption.NamePart(
    	namePart = obj.getNamePart(),
    	isExtension = obj.getIsExtension(),
    )

    override fun convert(obj: UninterpretedOption.NamePart):
        DescriptorProtos.UninterpretedOption.NamePart {
      val builder = DescriptorProtos.UninterpretedOption.NamePart.newBuilder()
      val value0 = obj.namePart
      if (value0 != null) {
        builder.setNamePart(value0)
      }
      val value1 = obj.isExtension
      if (value1 != null) {
        builder.setIsExtension(value1)
      }
      return builder.build()
    }
  }
}

public open class FeatureSetJvmConverter :
    ProtobufTypeMapper<FeatureSet, DescriptorProtos.FeatureSet> {
  override val descriptor: Descriptors.Descriptor = DescriptorProtos.FeatureSet.getDescriptor()

  override val parser: Parser<DescriptorProtos.FeatureSet> = DescriptorProtos.FeatureSet.parser()

  override val default: DescriptorProtos.FeatureSet =
      DescriptorProtos.FeatureSet.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.FeatureSet): FeatureSet = FeatureSet(
  	fieldPresence = FeatureSet.FieldPresence.forNumber(obj.getFieldPresence().number),
  	enumType = FeatureSet.EnumType.forNumber(obj.getEnumType().number),
  	repeatedFieldEncoding =
      FeatureSet.RepeatedFieldEncoding.forNumber(obj.getRepeatedFieldEncoding().number),
  	utf8Validation = FeatureSet.Utf8Validation.forNumber(obj.getUtf8Validation().number),
  	messageEncoding = FeatureSet.MessageEncoding.forNumber(obj.getMessageEncoding().number),
  	jsonFormat = FeatureSet.JsonFormat.forNumber(obj.getJsonFormat().number),
  )

  override fun convert(obj: FeatureSet): DescriptorProtos.FeatureSet {
    val builder = DescriptorProtos.FeatureSet.newBuilder()
    val value0 = obj.fieldPresence
    if (value0 != null) {
      builder.setFieldPresence(DescriptorProtos.FeatureSet.FieldPresence.forNumber(value0.number))
    }
    val value1 = obj.enumType
    if (value1 != null) {
      builder.setEnumType(DescriptorProtos.FeatureSet.EnumType.forNumber(value1.number))
    }
    val value2 = obj.repeatedFieldEncoding
    if (value2 != null) {
      builder.setRepeatedFieldEncoding(DescriptorProtos.FeatureSet.RepeatedFieldEncoding.forNumber(value2.number))
    }
    val value3 = obj.utf8Validation
    if (value3 != null) {
      builder.setUtf8Validation(DescriptorProtos.FeatureSet.Utf8Validation.forNumber(value3.number))
    }
    val value4 = obj.messageEncoding
    if (value4 != null) {
      builder.setMessageEncoding(DescriptorProtos.FeatureSet.MessageEncoding.forNumber(value4.number))
    }
    val value5 = obj.jsonFormat
    if (value5 != null) {
      builder.setJsonFormat(DescriptorProtos.FeatureSet.JsonFormat.forNumber(value5.number))
    }
    return builder.build()
  }
}

public open class FeatureSetDefaultsJvmConverter :
    ProtobufTypeMapper<FeatureSetDefaults, DescriptorProtos.FeatureSetDefaults> {
  override val descriptor: Descriptors.Descriptor =
      DescriptorProtos.FeatureSetDefaults.getDescriptor()

  override val parser: Parser<DescriptorProtos.FeatureSetDefaults> =
      DescriptorProtos.FeatureSetDefaults.parser()

  override val default: DescriptorProtos.FeatureSetDefaults =
      DescriptorProtos.FeatureSetDefaults.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.FeatureSetDefaults): FeatureSetDefaults =
      FeatureSetDefaults(
  	defaults = obj.getDefaultsList().map {
      FeatureSetDefaultsConverter.FeatureSetEditionDefaultConverter.convert(it) },
  	minimumEdition = Edition.forNumber(obj.getMinimumEdition().number),
  	maximumEdition = Edition.forNumber(obj.getMaximumEdition().number),
  )

  override fun convert(obj: FeatureSetDefaults): DescriptorProtos.FeatureSetDefaults {
    val builder = DescriptorProtos.FeatureSetDefaults.newBuilder()
    builder.addAllDefaults(obj.defaults.map {
        FeatureSetDefaultsConverter.FeatureSetEditionDefaultConverter.convert(it) })
    val value1 = obj.minimumEdition
    if (value1 != null) {
      builder.setMinimumEdition(DescriptorProtos.Edition.forNumber(value1.number))
    }
    val value2 = obj.maximumEdition
    if (value2 != null) {
      builder.setMaximumEdition(DescriptorProtos.Edition.forNumber(value2.number))
    }
    return builder.build()
  }

  public open class FeatureSetEditionDefaultJvmConverter :
      ProtobufTypeMapper<FeatureSetDefaults.FeatureSetEditionDefault, DescriptorProtos.FeatureSetDefaults.FeatureSetEditionDefault>
      {
    override val descriptor: Descriptors.Descriptor =
        DescriptorProtos.FeatureSetDefaults.FeatureSetEditionDefault.getDescriptor()

    override val parser: Parser<DescriptorProtos.FeatureSetDefaults.FeatureSetEditionDefault> =
        DescriptorProtos.FeatureSetDefaults.FeatureSetEditionDefault.parser()

    override val default: DescriptorProtos.FeatureSetDefaults.FeatureSetEditionDefault =
        DescriptorProtos.FeatureSetDefaults.FeatureSetEditionDefault.getDefaultInstance()

    override fun convert(obj: DescriptorProtos.FeatureSetDefaults.FeatureSetEditionDefault):
        FeatureSetDefaults.FeatureSetEditionDefault = FeatureSetDefaults.FeatureSetEditionDefault(
    	edition = Edition.forNumber(obj.getEdition().number),
    	features = FeatureSetConverter.convert(obj.getFeatures()),
    )

    override fun convert(obj: FeatureSetDefaults.FeatureSetEditionDefault):
        DescriptorProtos.FeatureSetDefaults.FeatureSetEditionDefault {
      val builder = DescriptorProtos.FeatureSetDefaults.FeatureSetEditionDefault.newBuilder()
      val value0 = obj.edition
      if (value0 != null) {
        builder.setEdition(DescriptorProtos.Edition.forNumber(value0.number))
      }
      val value1 = obj.features
      if (value1 != null) {
        builder.setFeatures(FeatureSetConverter.convert(value1))
      }
      return builder.build()
    }
  }
}

public open class SourceCodeInfoJvmConverter :
    ProtobufTypeMapper<SourceCodeInfo, DescriptorProtos.SourceCodeInfo> {
  override val descriptor: Descriptors.Descriptor = DescriptorProtos.SourceCodeInfo.getDescriptor()

  override val parser: Parser<DescriptorProtos.SourceCodeInfo> =
      DescriptorProtos.SourceCodeInfo.parser()

  override val default: DescriptorProtos.SourceCodeInfo =
      DescriptorProtos.SourceCodeInfo.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.SourceCodeInfo): SourceCodeInfo = SourceCodeInfo(
  	location = obj.getLocationList().map { SourceCodeInfoConverter.LocationConverter.convert(it) },
  )

  override fun convert(obj: SourceCodeInfo): DescriptorProtos.SourceCodeInfo {
    val builder = DescriptorProtos.SourceCodeInfo.newBuilder()
    builder.addAllLocation(obj.location.map { SourceCodeInfoConverter.LocationConverter.convert(it)
        })
    return builder.build()
  }

  public open class LocationJvmConverter :
      ProtobufTypeMapper<SourceCodeInfo.Location, DescriptorProtos.SourceCodeInfo.Location> {
    override val descriptor: Descriptors.Descriptor =
        DescriptorProtos.SourceCodeInfo.Location.getDescriptor()

    override val parser: Parser<DescriptorProtos.SourceCodeInfo.Location> =
        DescriptorProtos.SourceCodeInfo.Location.parser()

    override val default: DescriptorProtos.SourceCodeInfo.Location =
        DescriptorProtos.SourceCodeInfo.Location.getDefaultInstance()

    override fun convert(obj: DescriptorProtos.SourceCodeInfo.Location): SourceCodeInfo.Location =
        SourceCodeInfo.Location(
    	path = obj.getPathList().map { it },
    	span = obj.getSpanList().map { it },
    	leadingComments = obj.getLeadingComments(),
    	trailingComments = obj.getTrailingComments(),
    	leadingDetachedComments = obj.getLeadingDetachedCommentsList().map { it },
    )

    override fun convert(obj: SourceCodeInfo.Location): DescriptorProtos.SourceCodeInfo.Location {
      val builder = DescriptorProtos.SourceCodeInfo.Location.newBuilder()
      builder.addAllPath(obj.path.map { it })
      builder.addAllSpan(obj.span.map { it })
      val value2 = obj.leadingComments
      if (value2 != null) {
        builder.setLeadingComments(value2)
      }
      val value3 = obj.trailingComments
      if (value3 != null) {
        builder.setTrailingComments(value3)
      }
      builder.addAllLeadingDetachedComments(obj.leadingDetachedComments.map { it })
      return builder.build()
    }
  }
}

public open class GeneratedCodeInfoJvmConverter :
    ProtobufTypeMapper<GeneratedCodeInfo, DescriptorProtos.GeneratedCodeInfo> {
  override val descriptor: Descriptors.Descriptor =
      DescriptorProtos.GeneratedCodeInfo.getDescriptor()

  override val parser: Parser<DescriptorProtos.GeneratedCodeInfo> =
      DescriptorProtos.GeneratedCodeInfo.parser()

  override val default: DescriptorProtos.GeneratedCodeInfo =
      DescriptorProtos.GeneratedCodeInfo.getDefaultInstance()

  override fun convert(obj: DescriptorProtos.GeneratedCodeInfo): GeneratedCodeInfo =
      GeneratedCodeInfo(
  	`annotation` = obj.getAnnotationList().map {
      GeneratedCodeInfoConverter.AnnotationConverter.convert(it) },
  )

  override fun convert(obj: GeneratedCodeInfo): DescriptorProtos.GeneratedCodeInfo {
    val builder = DescriptorProtos.GeneratedCodeInfo.newBuilder()
    builder.addAllAnnotation(obj.`annotation`.map {
        GeneratedCodeInfoConverter.AnnotationConverter.convert(it) })
    return builder.build()
  }

  public open class AnnotationJvmConverter :
      ProtobufTypeMapper<GeneratedCodeInfo.Annotation, DescriptorProtos.GeneratedCodeInfo.Annotation>
      {
    override val descriptor: Descriptors.Descriptor =
        DescriptorProtos.GeneratedCodeInfo.Annotation.getDescriptor()

    override val parser: Parser<DescriptorProtos.GeneratedCodeInfo.Annotation> =
        DescriptorProtos.GeneratedCodeInfo.Annotation.parser()

    override val default: DescriptorProtos.GeneratedCodeInfo.Annotation =
        DescriptorProtos.GeneratedCodeInfo.Annotation.getDefaultInstance()

    override fun convert(obj: DescriptorProtos.GeneratedCodeInfo.Annotation):
        GeneratedCodeInfo.Annotation = GeneratedCodeInfo.Annotation(
    	path = obj.getPathList().map { it },
    	sourceFile = obj.getSourceFile(),
    	begin = obj.getBegin(),
    	end = obj.getEnd(),
    	semantic = GeneratedCodeInfo.Annotation.Semantic.forNumber(obj.getSemantic().number),
    )

    override fun convert(obj: GeneratedCodeInfo.Annotation):
        DescriptorProtos.GeneratedCodeInfo.Annotation {
      val builder = DescriptorProtos.GeneratedCodeInfo.Annotation.newBuilder()
      builder.addAllPath(obj.path.map { it })
      val value1 = obj.sourceFile
      if (value1 != null) {
        builder.setSourceFile(value1)
      }
      val value2 = obj.begin
      if (value2 != null) {
        builder.setBegin(value2)
      }
      val value3 = obj.end
      if (value3 != null) {
        builder.setEnd(value3)
      }
      val value4 = obj.semantic
      if (value4 != null) {
        builder.setSemantic(DescriptorProtos.GeneratedCodeInfo.Annotation.Semantic.forNumber(value4.number))
      }
      return builder.build()
    }
  }
}
