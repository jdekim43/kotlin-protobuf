// Transform from google/protobuf/wrappers.proto
@file:GeneratorVersion(version = "0.3.3")

package google.protobuf

import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter

public actual object DoubleValueConverter : ProtobufConverter<DoubleValue> by
    DoubleValueJvmConverter

public actual object FloatValueConverter : ProtobufConverter<FloatValue> by FloatValueJvmConverter

public actual object Int64ValueConverter : ProtobufConverter<Int64Value> by Int64ValueJvmConverter

public actual object UInt64ValueConverter : ProtobufConverter<UInt64Value> by
    UInt64ValueJvmConverter

public actual object Int32ValueConverter : ProtobufConverter<Int32Value> by Int32ValueJvmConverter

public actual object UInt32ValueConverter : ProtobufConverter<UInt32Value> by
    UInt32ValueJvmConverter

public actual object BoolValueConverter : ProtobufConverter<BoolValue> by BoolValueJvmConverter

public actual object StringValueConverter : ProtobufConverter<StringValue> by
    StringValueJvmConverter

public actual object BytesValueConverter : ProtobufConverter<BytesValue> by BytesValueJvmConverter
