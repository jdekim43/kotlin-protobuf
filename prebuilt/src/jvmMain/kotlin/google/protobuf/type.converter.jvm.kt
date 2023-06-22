// Transform from google/protobuf/type.proto
@file:GeneratorVersion(version = "0.2.3")

package google.protobuf

import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper

public object TypeJvmConverter : ProtobufTypeMapper<Type, com.google.protobuf.Type> {
  public override val descriptor: Descriptors.Descriptor = com.google.protobuf.Type.getDescriptor()

  public override val parser: Parser<com.google.protobuf.Type> = com.google.protobuf.Type.parser()

  public override fun convert(obj: com.google.protobuf.Type): Type = Type(
  	name = obj.name,
  	fields = obj.fieldsList.map { FieldJvmConverter.convert(it) },
  	oneofs = obj.oneofsList.map { it },
  	options = obj.optionsList.map { OptionJvmConverter.convert(it) },
  	sourceContext = SourceContextJvmConverter.convert(obj.sourceContext),
  	syntax = Syntax.forNumber(obj.syntax.number),
  	edition = obj.edition,
  )

  public override fun convert(obj: Type): com.google.protobuf.Type {
    val builder = com.google.protobuf.Type.newBuilder()
    builder.setName(obj.name)
    builder.addAllFields(obj.fields.map { FieldJvmConverter.convert(it) })
    builder.addAllOneofs(obj.oneofs.map { it })
    builder.addAllOptions(obj.options.map { OptionJvmConverter.convert(it) })
    builder.setSourceContext(SourceContextJvmConverter.convert(obj.sourceContext))
    builder.setSyntax(com.google.protobuf.Syntax.forNumber(obj.syntax.number))
    builder.setEdition(obj.edition)
    return builder.build()
  }
}

public object FieldJvmConverter : ProtobufTypeMapper<Field, com.google.protobuf.Field> {
  public override val descriptor: Descriptors.Descriptor = com.google.protobuf.Field.getDescriptor()

  public override val parser: Parser<com.google.protobuf.Field> = com.google.protobuf.Field.parser()

  public override fun convert(obj: com.google.protobuf.Field): Field = Field(
  	kind = Field.Kind.forNumber(obj.kind.number),
  	cardinality = Field.Cardinality.forNumber(obj.cardinality.number),
  	number = obj.number,
  	name = obj.name,
  	typeUrl = obj.typeUrl,
  	oneofIndex = obj.oneofIndex,
  	packed = obj.packed,
  	options = obj.optionsList.map { OptionJvmConverter.convert(it) },
  	jsonName = obj.jsonName,
  	defaultValue = obj.defaultValue,
  )

  public override fun convert(obj: Field): com.google.protobuf.Field {
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
  public override val descriptor: Descriptors.Descriptor = com.google.protobuf.Enum.getDescriptor()

  public override val parser: Parser<com.google.protobuf.Enum> = com.google.protobuf.Enum.parser()

  public override fun convert(obj: com.google.protobuf.Enum): Enum = Enum(
  	name = obj.name,
  	enumvalue = obj.enumvalueList.map { EnumValueJvmConverter.convert(it) },
  	options = obj.optionsList.map { OptionJvmConverter.convert(it) },
  	sourceContext = SourceContextJvmConverter.convert(obj.sourceContext),
  	syntax = Syntax.forNumber(obj.syntax.number),
  	edition = obj.edition,
  )

  public override fun convert(obj: Enum): com.google.protobuf.Enum {
    val builder = com.google.protobuf.Enum.newBuilder()
    builder.setName(obj.name)
    builder.addAllEnumvalue(obj.enumvalue.map { EnumValueJvmConverter.convert(it) })
    builder.addAllOptions(obj.options.map { OptionJvmConverter.convert(it) })
    builder.setSourceContext(SourceContextJvmConverter.convert(obj.sourceContext))
    builder.setSyntax(com.google.protobuf.Syntax.forNumber(obj.syntax.number))
    builder.setEdition(obj.edition)
    return builder.build()
  }
}

public object EnumValueJvmConverter : ProtobufTypeMapper<EnumValue, com.google.protobuf.EnumValue> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.EnumValue.getDescriptor()

  public override val parser: Parser<com.google.protobuf.EnumValue> =
      com.google.protobuf.EnumValue.parser()

  public override fun convert(obj: com.google.protobuf.EnumValue): EnumValue = EnumValue(
  	name = obj.name,
  	number = obj.number,
  	options = obj.optionsList.map { OptionJvmConverter.convert(it) },
  )

  public override fun convert(obj: EnumValue): com.google.protobuf.EnumValue {
    val builder = com.google.protobuf.EnumValue.newBuilder()
    builder.setName(obj.name)
    builder.setNumber(obj.number)
    builder.addAllOptions(obj.options.map { OptionJvmConverter.convert(it) })
    return builder.build()
  }
}

public object OptionJvmConverter : ProtobufTypeMapper<Option, com.google.protobuf.Option> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.Option.getDescriptor()

  public override val parser: Parser<com.google.protobuf.Option> =
      com.google.protobuf.Option.parser()

  public override fun convert(obj: com.google.protobuf.Option): Option = Option(
  	name = obj.name,
  	`value` = AnyJvmConverter.convert(obj.`value`),
  )

  public override fun convert(obj: Option): com.google.protobuf.Option {
    val builder = com.google.protobuf.Option.newBuilder()
    builder.setName(obj.name)
    builder.setValue(AnyJvmConverter.convert(obj.`value`))
    return builder.build()
  }
}