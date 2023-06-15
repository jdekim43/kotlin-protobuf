// Transform from google/protobuf/struct.proto
@file:GeneratorVersion(version = "0.2.3")

package google.protobuf

import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper

public object StructJvmConverter : ProtobufTypeMapper<Struct, com.google.protobuf.Struct> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.Struct.getDescriptor()

  public override val parser: Parser<com.google.protobuf.Struct> =
      com.google.protobuf.Struct.parser()

  public override fun convert(obj: com.google.protobuf.Struct): Struct = Struct(
  	fields = obj.fieldsMap.map { it.key to ValueJvmConverter.convert(it.value) }.toMap(),
  )

  public override fun convert(obj: Struct): com.google.protobuf.Struct {
    val builder = com.google.protobuf.Struct.newBuilder()
    builder.putAllFields(obj.fields.map { it.key to ValueJvmConverter.convert(it.value) }.toMap())
    return builder.build()
  }
}

public object ValueJvmConverter : ProtobufTypeMapper<Value, com.google.protobuf.Value> {
  public override val descriptor: Descriptors.Descriptor = com.google.protobuf.Value.getDescriptor()

  public override val parser: Parser<com.google.protobuf.Value> = com.google.protobuf.Value.parser()

  public override fun convert(obj: com.google.protobuf.Value): Value = Value(
  	kind = mapOf(
  1 to { Value.KindOneOf.NullValue(NullValue.forNumber(obj.nullValue.number)) },
  2 to { Value.KindOneOf.NumberValue(obj.numberValue) },
  3 to { Value.KindOneOf.StringValue(obj.stringValue) },
  4 to { Value.KindOneOf.BoolValue(obj.boolValue) },
  5 to { Value.KindOneOf.StructValue(StructJvmConverter.convert(obj.structValue)) },
  6 to { Value.KindOneOf.ListValue(ListValueJvmConverter.convert(obj.listValue)) },
  ).getValue(obj.kindCase.number)(),
  )

  public override fun convert(obj: Value): com.google.protobuf.Value {
    val builder = com.google.protobuf.Value.newBuilder()
    when (obj.kind) {
      is Value.KindOneOf.NullValue ->
          builder.setNullValue(com.google.protobuf.NullValue.forNumber(obj.kind.value.number))
      is Value.KindOneOf.NumberValue -> builder.setNumberValue(obj.kind.value)
      is Value.KindOneOf.StringValue -> builder.setStringValue(obj.kind.value)
      is Value.KindOneOf.BoolValue -> builder.setBoolValue(obj.kind.value)
      is Value.KindOneOf.StructValue ->
          builder.setStructValue(StructJvmConverter.convert(obj.kind.value))
      is Value.KindOneOf.ListValue ->
          builder.setListValue(ListValueJvmConverter.convert(obj.kind.value))
    }
    return builder.build()
  }
}

public object ListValueJvmConverter : ProtobufTypeMapper<ListValue, com.google.protobuf.ListValue> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.ListValue.getDescriptor()

  public override val parser: Parser<com.google.protobuf.ListValue> =
      com.google.protobuf.ListValue.parser()

  public override fun convert(obj: com.google.protobuf.ListValue): ListValue = ListValue(
  	values = obj.valuesList.map { ValueJvmConverter.convert(it) },
  )

  public override fun convert(obj: ListValue): com.google.protobuf.ListValue {
    val builder = com.google.protobuf.ListValue.newBuilder()
    builder.addAllValues(obj.values.map { ValueJvmConverter.convert(it) })
    return builder.build()
  }
}
