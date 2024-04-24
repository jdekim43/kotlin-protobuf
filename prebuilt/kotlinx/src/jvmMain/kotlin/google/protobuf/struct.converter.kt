// Transform from google/protobuf/struct.proto
@file:GeneratorVersion(version = "0.4.0")

package google.protobuf

import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter

public actual object StructConverter : ProtobufConverter<Struct> by StructJvmConverter

public actual object ValueConverter : ProtobufConverter<Value> by ValueJvmConverter

public actual object ListValueConverter : ProtobufConverter<ListValue> by ListValueJvmConverter
