// Transform from google/protobuf/struct.proto
@file:GeneratorVersion(version = "0.5.2")

package google.protobuf

import java.lang.IllegalStateException
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.converter.parseProtobuf

public expect object StructConverter : ProtobufConverter<Struct>

public fun Struct.toAny(): Any = Any(Struct.TYPE_URL, with(StructConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Struct> = StructConverter): Struct {
  if (typeUrl != Struct.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val Struct.Companion.converter: StructConverter
  get() = StructConverter

public expect object ValueConverter : ProtobufConverter<Value>

public fun Value.toAny(): Any = Any(Value.TYPE_URL, with(ValueConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Value> = ValueConverter): Value {
  if (typeUrl != Value.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val Value.Companion.converter: ValueConverter
  get() = ValueConverter

public expect object ListValueConverter : ProtobufConverter<ListValue>

public fun ListValue.toAny(): Any = Any(ListValue.TYPE_URL, with(ListValueConverter) { toByteArray()
    })

public fun Any.parse(converter: ProtobufConverter<ListValue> = ListValueConverter): ListValue {
  if (typeUrl != ListValue.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val ListValue.Companion.converter: ListValueConverter
  get() = ListValueConverter
