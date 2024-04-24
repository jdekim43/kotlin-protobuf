// Transform from google/protobuf/type.proto
@file:GeneratorVersion(version = "0.4.0")

package google.protobuf

import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper

public object TypeJvmConverter : ProtobufTypeMapper<Type, com.google.protobuf.Type> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.Type.getDescriptor()

  override val parser: Parser<com.google.protobuf.Type> = com.google.protobuf.Type.parser()

  override fun convert(obj: com.google.protobuf.Type): Type = Type(
  	name = obj.getName(),
  	fields = obj.getFieldsList().map { FieldJvmConverter.convert(it) },
  	oneofs = obj.getOneofsList().map { it },
  	options = obj.getOptionsList().map { OptionJvmConverter.convert(it) },
  	sourceContext = SourceContextJvmConverter.convert(obj.getSourceContext()),
  	syntax = Syntax.forNumber(obj.getSyntax().number),
  	edition = obj.getEdition(),
  )

  override fun convert(obj: Type): com.google.protobuf.Type {
    val builder = com.google.protobuf.Type.newBuilder()
    builder.setName(obj.name)
    builder.addAllFields(obj.fields.map { FieldJvmConverter.convert(it) })
    builder.addAllOneofs(obj.oneofs.map { it })
    builder.addAllOptions(obj.options.map { OptionJvmConverter.convert(it) })
    val value4 = obj.sourceContext
    if (value4 != null) {
      builder.setSourceContext(SourceContextJvmConverter.convert(value4))
    }
    builder.setSyntax(com.google.protobuf.Syntax.forNumber(obj.syntax.number))
    builder.setEdition(obj.edition)
    return builder.build()
  }
}

public object FieldJvmConverter : ProtobufTypeMapper<Field, com.google.protobuf.Field> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.Field.getDescriptor()

  override val parser: Parser<com.google.protobuf.Field> = com.google.protobuf.Field.parser()

  override fun convert(obj: com.google.protobuf.Field): Field = Field(
  	kind = Field.Kind.forNumber(obj.getKind().number),
  	cardinality = Field.Cardinality.forNumber(obj.getCardinality().number),
  	number = obj.getNumber(),
  	name = obj.getName(),
  	typeUrl = obj.getTypeUrl(),
  	oneofIndex = obj.getOneofIndex(),
  	packed = obj.getPacked(),
  	options = obj.getOptionsList().map { OptionJvmConverter.convert(it) },
  	jsonName = obj.getJsonName(),
  	defaultValue = obj.getDefaultValue(),
  )

  override fun convert(obj: Field): com.google.protobuf.Field {
    val builder = com.google.protobuf.Field.newBuilder()
    builder.setKind(com.google.protobuf.Field.Kind.forNumber(obj.kind.number))
    builder.setCardinality(com.google.protobuf.Field.Cardinality.forNumber(obj.cardinality.number))
    builder.setNumber(obj.number)
    builder.setName(obj.name)
    builder.setTypeUrl(obj.typeUrl)
    builder.setOneofIndex(obj.oneofIndex)
    builder.setPacked(obj.packed)
    builder.addAllOptions(obj.options.map { OptionJvmConverter.convert(it) })
    builder.setJsonName(obj.jsonName)
    builder.setDefaultValue(obj.defaultValue)
    return builder.build()
  }
}

public object EnumJvmConverter : ProtobufTypeMapper<Enum, com.google.protobuf.Enum> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.Enum.getDescriptor()

  override val parser: Parser<com.google.protobuf.Enum> = com.google.protobuf.Enum.parser()

  override fun convert(obj: com.google.protobuf.Enum): Enum = Enum(
  	name = obj.getName(),
  	enumvalue = obj.getEnumvalueList().map { EnumValueJvmConverter.convert(it) },
  	options = obj.getOptionsList().map { OptionJvmConverter.convert(it) },
  	sourceContext = SourceContextJvmConverter.convert(obj.getSourceContext()),
  	syntax = Syntax.forNumber(obj.getSyntax().number),
  	edition = obj.getEdition(),
  )

  override fun convert(obj: Enum): com.google.protobuf.Enum {
    val builder = com.google.protobuf.Enum.newBuilder()
    builder.setName(obj.name)
    builder.addAllEnumvalue(obj.enumvalue.map { EnumValueJvmConverter.convert(it) })
    builder.addAllOptions(obj.options.map { OptionJvmConverter.convert(it) })
    val value3 = obj.sourceContext
    if (value3 != null) {
      builder.setSourceContext(SourceContextJvmConverter.convert(value3))
    }
    builder.setSyntax(com.google.protobuf.Syntax.forNumber(obj.syntax.number))
    builder.setEdition(obj.edition)
    return builder.build()
  }
}

public object EnumValueJvmConverter : ProtobufTypeMapper<EnumValue, com.google.protobuf.EnumValue> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.EnumValue.getDescriptor()

  override val parser: Parser<com.google.protobuf.EnumValue> =
      com.google.protobuf.EnumValue.parser()

  override fun convert(obj: com.google.protobuf.EnumValue): EnumValue = EnumValue(
  	name = obj.getName(),
  	number = obj.getNumber(),
  	options = obj.getOptionsList().map { OptionJvmConverter.convert(it) },
  )

  override fun convert(obj: EnumValue): com.google.protobuf.EnumValue {
    val builder = com.google.protobuf.EnumValue.newBuilder()
    builder.setName(obj.name)
    builder.setNumber(obj.number)
    builder.addAllOptions(obj.options.map { OptionJvmConverter.convert(it) })
    return builder.build()
  }
}

public object OptionJvmConverter : ProtobufTypeMapper<Option, com.google.protobuf.Option> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.Option.getDescriptor()

  override val parser: Parser<com.google.protobuf.Option> = com.google.protobuf.Option.parser()

  override fun convert(obj: com.google.protobuf.Option): Option = Option(
  	name = obj.getName(),
  	`value` = AnyJvmConverter.convert(obj.getValue()),
  )

  override fun convert(obj: Option): com.google.protobuf.Option {
    val builder = com.google.protobuf.Option.newBuilder()
    builder.setName(obj.name)
    val value1 = obj.`value`
    if (value1 != null) {
      builder.setValue(AnyJvmConverter.convert(value1))
    }
    return builder.build()
  }
}
