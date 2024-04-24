// Transform from google/protobuf/type.proto
@file:GeneratorVersion(version = "0.4.0")

package google.protobuf

import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter

public actual object TypeConverter : ProtobufConverter<Type> by TypeJvmConverter

public actual object FieldConverter : ProtobufConverter<Field> by FieldJvmConverter

public actual object EnumConverter : ProtobufConverter<Enum> by EnumJvmConverter

public actual object EnumValueConverter : ProtobufConverter<EnumValue> by EnumValueJvmConverter

public actual object OptionConverter : ProtobufConverter<Option> by OptionJvmConverter
