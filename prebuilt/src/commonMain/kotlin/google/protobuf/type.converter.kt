// Transform from google/protobuf/type.proto
@file:GeneratorVersion(version = "0.4.0")

package google.protobuf

import java.lang.IllegalStateException
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.converter.parseProtobuf

public expect object TypeConverter : ProtobufConverter<Type>

public fun Type.toAny(): Any = Any(Type.TYPE_URL, with(TypeConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Type> = TypeConverter): Type {
  if (typeUrl != Type.TYPE_URL) throw IllegalStateException("Please check the type_url")
  if (value == null) throw IllegalStateException("value can not be null")
  return value.parseProtobuf(converter)
}

public val Type.Companion.converter: TypeConverter
  get() = TypeConverter

public expect object FieldConverter : ProtobufConverter<Field>

public fun Field.toAny(): Any = Any(Field.TYPE_URL, with(FieldConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Field> = FieldConverter): Field {
  if (typeUrl != Field.TYPE_URL) throw IllegalStateException("Please check the type_url")
  if (value == null) throw IllegalStateException("value can not be null")
  return value.parseProtobuf(converter)
}

public val Field.Companion.converter: FieldConverter
  get() = FieldConverter

public expect object EnumConverter : ProtobufConverter<Enum>

public fun Enum.toAny(): Any = Any(Enum.TYPE_URL, with(EnumConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Enum> = EnumConverter): Enum {
  if (typeUrl != Enum.TYPE_URL) throw IllegalStateException("Please check the type_url")
  if (value == null) throw IllegalStateException("value can not be null")
  return value.parseProtobuf(converter)
}

public val Enum.Companion.converter: EnumConverter
  get() = EnumConverter

public expect object EnumValueConverter : ProtobufConverter<EnumValue>

public fun EnumValue.toAny(): Any = Any(EnumValue.TYPE_URL, with(EnumValueConverter) { toByteArray()
    })

public fun Any.parse(converter: ProtobufConverter<EnumValue> = EnumValueConverter): EnumValue {
  if (typeUrl != EnumValue.TYPE_URL) throw IllegalStateException("Please check the type_url")
  if (value == null) throw IllegalStateException("value can not be null")
  return value.parseProtobuf(converter)
}

public val EnumValue.Companion.converter: EnumValueConverter
  get() = EnumValueConverter

public expect object OptionConverter : ProtobufConverter<Option>

public fun Option.toAny(): Any = Any(Option.TYPE_URL, with(OptionConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Option> = OptionConverter): Option {
  if (typeUrl != Option.TYPE_URL) throw IllegalStateException("Please check the type_url")
  if (value == null) throw IllegalStateException("value can not be null")
  return value.parseProtobuf(converter)
}

public val Option.Companion.converter: OptionConverter
  get() = OptionConverter
