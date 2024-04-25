// Transform from google/protobuf/struct.proto
@file:GeneratorVersion(version = "0.4.0")

package google.protobuf

import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter

public actual object StructConverter : StructJvmConverter(), ProtobufConverter<Struct>

public actual object ValueConverter : ValueJvmConverter(), ProtobufConverter<Value>

public actual object ListValueConverter : ListValueJvmConverter(), ProtobufConverter<ListValue>
