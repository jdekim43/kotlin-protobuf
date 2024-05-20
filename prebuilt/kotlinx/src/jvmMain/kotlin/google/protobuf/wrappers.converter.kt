// Transform from google/protobuf/wrappers.proto
@file:GeneratorVersion(version = "0.4.1")

package google.protobuf

import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter

public actual object DoubleValueConverter : DoubleValueJvmConverter(),
    ProtobufConverter<DoubleValue>

public actual object FloatValueConverter : FloatValueJvmConverter(), ProtobufConverter<FloatValue>

public actual object Int64ValueConverter : Int64ValueJvmConverter(), ProtobufConverter<Int64Value>

public actual object UInt64ValueConverter : UInt64ValueJvmConverter(),
    ProtobufConverter<UInt64Value>

public actual object Int32ValueConverter : Int32ValueJvmConverter(), ProtobufConverter<Int32Value>

public actual object UInt32ValueConverter : UInt32ValueJvmConverter(),
    ProtobufConverter<UInt32Value>

public actual object BoolValueConverter : BoolValueJvmConverter(), ProtobufConverter<BoolValue>

public actual object StringValueConverter : StringValueJvmConverter(),
    ProtobufConverter<StringValue>

public actual object BytesValueConverter : BytesValueJvmConverter(), ProtobufConverter<BytesValue>
