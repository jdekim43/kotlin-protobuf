// Transform from google/protobuf/type.proto
@file:GeneratorVersion(version = "0.3.3")

package google.protobuf

import java.lang.IllegalStateException
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.converter.parseProtobuf

public expect object TypeConverter : ProtobufConverter<Type>

public fun Type.toAny(): Any = Any(Type.TYPE_URL, with(TypeConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Type>): Type {
  if (typeUrl != Type.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public expect object FieldConverter : ProtobufConverter<Field>

public fun Field.toAny(): Any = Any(Field.TYPE_URL, with(FieldConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Field>): Field {
  if (typeUrl != Field.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public expect object EnumConverter : ProtobufConverter<Enum>

public fun Enum.toAny(): Any = Any(Enum.TYPE_URL, with(EnumConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Enum>): Enum {
  if (typeUrl != Enum.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public expect object EnumValueConverter : ProtobufConverter<EnumValue>

public fun EnumValue.toAny(): Any = Any(EnumValue.TYPE_URL, with(EnumValueConverter) { toByteArray()
    })

public fun Any.parse(converter: ProtobufConverter<EnumValue>): EnumValue {
  if (typeUrl != EnumValue.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public expect object OptionConverter : ProtobufConverter<Option>

public fun Option.toAny(): Any = Any(Option.TYPE_URL, with(OptionConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Option>): Option {
  if (typeUrl != Option.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}
