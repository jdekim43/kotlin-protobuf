// Transform from google/protobuf/descriptor.proto
@file:GeneratorVersion(version = "0.5.1")

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
    builder.setName(obj.name)
    builder.setPackage(obj.`package`)
    builder.addAllDependency(obj.dependency.map { it })
    builder.addAllPublicDependency(obj.publicDependency.map { it })
    builder.addAllWeakDependency(obj.weakDependency.map { it })
    builder.addAllMessageType(obj.messageType.map { DescriptorProtoConverter.convert(it) })
    builder.addAllEnumType(obj.enumType.map { EnumDescriptorProtoConverter.convert(it) })
    builder.addAllService(obj.service.map { ServiceDescriptorProtoConverter.convert(it) })
    builder.addAllExtension(obj.extension.map { FieldDescriptorProtoConverter.convert(it) })
    builder.setOptions(FileOptionsConverter.convert(obj.options))
    builder.setSourceCodeInfo(SourceCodeInfoConverter.convert(obj.sourceCodeInfo))
    builder.setSyntax(obj.syntax)
    builder.setEdition(DescriptorProtos.Edition.forNumber(obj.edition.number))
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
    builder.setName(obj.name)
    builder.addAllField(obj.`field`.map { FieldDescriptorProtoConverter.convert(it) })
    builder.addAllExtension(obj.extension.map { FieldDescriptorProtoConverter.convert(it) })
    builder.addAllNestedType(obj.nestedType.map { DescriptorProtoConverter.convert(it) })
    builder.addAllEnumType(obj.enumType.map { EnumDescriptorProtoConverter.convert(it) })
    builder.addAllExtensionRange(obj.extensionRange.map {
        DescriptorProtoConverter.ExtensionRangeConverter.convert(it) })
    builder.addAllOneofDecl(obj.oneofDecl.map { OneofDescriptorProtoConverter.convert(it) })
    builder.setOptions(MessageOptionsConverter.convert(obj.options))
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
      builder.setStart(obj.start)
      builder.setEnd(obj.end)
      builder.setOptions(ExtensionRangeOptionsConverter.convert(obj.options))
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
      builder.setStart(obj.start)
      builder.setEnd(obj.end)
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
    builder.setFeatures(FeatureSetConverter.convert(obj.features))
    builder.setVerification(DescriptorProtos.ExtensionRangeOptions.VerificationState.forNumber(obj.verification.number))
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
      builder.setNumber(obj.number)
      builder.setFullName(obj.fullName)
      builder.setType(obj.type)
      builder.setReserved(obj.reserved)
      builder.setRepeated(obj.repeated)
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
    builder.setName(obj.name)
    builder.setNumber(obj.number)
    builder.setLabel(DescriptorProtos.FieldDescriptorProto.Label.forNumber(obj.label.number))
    builder.setType(DescriptorProtos.FieldDescriptorProto.Type.forNumber(obj.type.number))
    builder.setTypeName(obj.typeName)
    builder.setExtendee(obj.extendee)
    builder.setDefaultValue(obj.defaultValue)
    builder.setOneofIndex(obj.oneofIndex)
    builder.setJsonName(obj.jsonName)
    builder.setOptions(FieldOptionsConverter.convert(obj.options))
    builder.setProto3Optional(obj.proto3Optional)
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
    builder.setName(obj.name)
    builder.setOptions(OneofOptionsConverter.convert(obj.options))
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
    builder.setName(obj.name)
    builder.addAllValue(obj.`value`.map { EnumValueDescriptorProtoConverter.convert(it) })
    builder.setOptions(EnumOptionsConverter.convert(obj.options))
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
      builder.setStart(obj.start)
      builder.setEnd(obj.end)
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
    builder.setName(obj.name)
    builder.setNumber(obj.number)
    builder.setOptions(EnumValueOptionsConverter.convert(obj.options))
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
    builder.setName(obj.name)
    builder.addAllMethod(obj.method.map { MethodDescriptorProtoConverter.convert(it) })
    builder.setOptions(ServiceOptionsConverter.convert(obj.options))
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
    builder.setName(obj.name)
    builder.setInputType(obj.inputType)
    builder.setOutputType(obj.outputType)
    builder.setOptions(MethodOptionsConverter.convert(obj.options))
    builder.setClientStreaming(obj.clientStreaming)
    builder.setServerStreaming(obj.serverStreaming)
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
    builder.setJavaPackage(obj.javaPackage)
    builder.setJavaOuterClassname(obj.javaOuterClassname)
    builder.setJavaMultipleFiles(obj.javaMultipleFiles)
    builder.setJavaGenerateEqualsAndHash(obj.javaGenerateEqualsAndHash)
    builder.setJavaStringCheckUtf8(obj.javaStringCheckUtf8)
    builder.setOptimizeFor(DescriptorProtos.FileOptions.OptimizeMode.forNumber(obj.optimizeFor.number))
    builder.setGoPackage(obj.goPackage)
    builder.setCcGenericServices(obj.ccGenericServices)
    builder.setJavaGenericServices(obj.javaGenericServices)
    builder.setPyGenericServices(obj.pyGenericServices)
    builder.setDeprecated(obj.deprecated)
    builder.setCcEnableArenas(obj.ccEnableArenas)
    builder.setObjcClassPrefix(obj.objcClassPrefix)
    builder.setCsharpNamespace(obj.csharpNamespace)
    builder.setSwiftPrefix(obj.swiftPrefix)
    builder.setPhpClassPrefix(obj.phpClassPrefix)
    builder.setPhpNamespace(obj.phpNamespace)
    builder.setPhpMetadataNamespace(obj.phpMetadataNamespace)
    builder.setRubyPackage(obj.rubyPackage)
    builder.setFeatures(FeatureSetConverter.convert(obj.features))
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
    builder.setMessageSetWireFormat(obj.messageSetWireFormat)
    builder.setNoStandardDescriptorAccessor(obj.noStandardDescriptorAccessor)
    builder.setDeprecated(obj.deprecated)
    builder.setMapEntry(obj.mapEntry)
    builder.setDeprecatedLegacyJsonFieldConflicts(obj.deprecatedLegacyJsonFieldConflicts)
    builder.setFeatures(FeatureSetConverter.convert(obj.features))
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
  	featureSupport = FieldOptionsConverter.FeatureSupportConverter.convert(obj.getFeatureSupport()),
  	uninterpretedOption = obj.getUninterpretedOptionList().map {
      UninterpretedOptionConverter.convert(it) },
  )

  override fun convert(obj: FieldOptions): DescriptorProtos.FieldOptions {
    val builder = DescriptorProtos.FieldOptions.newBuilder()
    builder.setCtype(DescriptorProtos.FieldOptions.CType.forNumber(obj.ctype.number))
    builder.setPacked(obj.packed)
    builder.setJstype(DescriptorProtos.FieldOptions.JSType.forNumber(obj.jstype.number))
    builder.setLazy(obj.lazy)
    builder.setUnverifiedLazy(obj.unverifiedLazy)
    builder.setDeprecated(obj.deprecated)
    builder.setWeak(obj.weak)
    builder.setDebugRedact(obj.debugRedact)
    builder.setRetention(DescriptorProtos.FieldOptions.OptionRetention.forNumber(obj.retention.number))
    builder.addAllTargets(obj.targets.map {
        DescriptorProtos.FieldOptions.OptionTargetType.forNumber(it.number) })
    builder.addAllEditionDefaults(obj.editionDefaults.map {
        FieldOptionsConverter.EditionDefaultConverter.convert(it) })
    builder.setFeatures(FeatureSetConverter.convert(obj.features))
    builder.setFeatureSupport(FieldOptionsConverter.FeatureSupportConverter.convert(obj.featureSupport))
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
      builder.setEdition(DescriptorProtos.Edition.forNumber(obj.edition.number))
      builder.setValue(obj.`value`)
      return builder.build()
    }
  }

  public open class FeatureSupportJvmConverter :
      ProtobufTypeMapper<FieldOptions.FeatureSupport, DescriptorProtos.FieldOptions.FeatureSupport>
      {
    override val descriptor: Descriptors.Descriptor =
        DescriptorProtos.FieldOptions.FeatureSupport.getDescriptor()

    override val parser: Parser<DescriptorProtos.FieldOptions.FeatureSupport> =
        DescriptorProtos.FieldOptions.FeatureSupport.parser()

    override val default: DescriptorProtos.FieldOptions.FeatureSupport =
        DescriptorProtos.FieldOptions.FeatureSupport.getDefaultInstance()

    override fun convert(obj: DescriptorProtos.FieldOptions.FeatureSupport):
        FieldOptions.FeatureSupport = FieldOptions.FeatureSupport(
    	editionIntroduced = Edition.forNumber(obj.getEditionIntroduced().number),
    	editionDeprecated = Edition.forNumber(obj.getEditionDeprecated().number),
    	deprecationWarning = obj.getDeprecationWarning(),
    	editionRemoved = Edition.forNumber(obj.getEditionRemoved().number),
    )

    override fun convert(obj: FieldOptions.FeatureSupport):
        DescriptorProtos.FieldOptions.FeatureSupport {
      val builder = DescriptorProtos.FieldOptions.FeatureSupport.newBuilder()
      builder.setEditionIntroduced(DescriptorProtos.Edition.forNumber(obj.editionIntroduced.number))
      builder.setEditionDeprecated(DescriptorProtos.Edition.forNumber(obj.editionDeprecated.number))
      builder.setDeprecationWarning(obj.deprecationWarning)
      builder.setEditionRemoved(DescriptorProtos.Edition.forNumber(obj.editionRemoved.number))
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
    builder.setFeatures(FeatureSetConverter.convert(obj.features))
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
    builder.setAllowAlias(obj.allowAlias)
    builder.setDeprecated(obj.deprecated)
    builder.setDeprecatedLegacyJsonFieldConflicts(obj.deprecatedLegacyJsonFieldConflicts)
    builder.setFeatures(FeatureSetConverter.convert(obj.features))
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
    builder.setDeprecated(obj.deprecated)
    builder.setFeatures(FeatureSetConverter.convert(obj.features))
    builder.setDebugRedact(obj.debugRedact)
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
    builder.setFeatures(FeatureSetConverter.convert(obj.features))
    builder.setDeprecated(obj.deprecated)
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
    builder.setDeprecated(obj.deprecated)
    builder.setIdempotencyLevel(DescriptorProtos.MethodOptions.IdempotencyLevel.forNumber(obj.idempotencyLevel.number))
    builder.setFeatures(FeatureSetConverter.convert(obj.features))
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
    builder.setIdentifierValue(obj.identifierValue)
    builder.setPositiveIntValue(obj.positiveIntValue.asJavaType)
    builder.setNegativeIntValue(obj.negativeIntValue)
    builder.setDoubleValue(obj.doubleValue)
    builder.setStringValue(ByteString.copyFrom(obj.stringValue))
    builder.setAggregateValue(obj.aggregateValue)
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
      builder.setNamePart(obj.namePart)
      builder.setIsExtension(obj.isExtension)
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
    builder.setFieldPresence(DescriptorProtos.FeatureSet.FieldPresence.forNumber(obj.fieldPresence.number))
    builder.setEnumType(DescriptorProtos.FeatureSet.EnumType.forNumber(obj.enumType.number))
    builder.setRepeatedFieldEncoding(DescriptorProtos.FeatureSet.RepeatedFieldEncoding.forNumber(obj.repeatedFieldEncoding.number))
    builder.setUtf8Validation(DescriptorProtos.FeatureSet.Utf8Validation.forNumber(obj.utf8Validation.number))
    builder.setMessageEncoding(DescriptorProtos.FeatureSet.MessageEncoding.forNumber(obj.messageEncoding.number))
    builder.setJsonFormat(DescriptorProtos.FeatureSet.JsonFormat.forNumber(obj.jsonFormat.number))
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
    builder.setMinimumEdition(DescriptorProtos.Edition.forNumber(obj.minimumEdition.number))
    builder.setMaximumEdition(DescriptorProtos.Edition.forNumber(obj.maximumEdition.number))
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
    	overridableFeatures = FeatureSetConverter.convert(obj.getOverridableFeatures()),
    	fixedFeatures = FeatureSetConverter.convert(obj.getFixedFeatures()),
    )

    override fun convert(obj: FeatureSetDefaults.FeatureSetEditionDefault):
        DescriptorProtos.FeatureSetDefaults.FeatureSetEditionDefault {
      val builder = DescriptorProtos.FeatureSetDefaults.FeatureSetEditionDefault.newBuilder()
      builder.setEdition(DescriptorProtos.Edition.forNumber(obj.edition.number))
      builder.setOverridableFeatures(FeatureSetConverter.convert(obj.overridableFeatures))
      builder.setFixedFeatures(FeatureSetConverter.convert(obj.fixedFeatures))
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
      builder.setLeadingComments(obj.leadingComments)
      builder.setTrailingComments(obj.trailingComments)
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
      builder.setSourceFile(obj.sourceFile)
      builder.setBegin(obj.begin)
      builder.setEnd(obj.end)
      builder.setSemantic(DescriptorProtos.GeneratedCodeInfo.Annotation.Semantic.forNumber(obj.semantic.number))
      return builder.build()
    }
  }
}
