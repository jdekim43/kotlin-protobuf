// Transform from google/protobuf/type.proto
@file:GeneratorVersion(version = "0.4.0")

package google.protobuf

import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter

public actual object TypeConverter : TypeJvmConverter(), ProtobufConverter<Type>

public actual object FieldConverter : FieldJvmConverter(), ProtobufConverter<Field>

public actual object EnumConverter : EnumJvmConverter(), ProtobufConverter<Enum>

public actual object EnumValueConverter : EnumValueJvmConverter(), ProtobufConverter<EnumValue>

public actual object OptionConverter : OptionJvmConverter(), ProtobufConverter<Option>
