// Transform from google/protobuf/empty.proto
@file:GeneratorVersion(version = "0.3.3")

package google.protobuf

import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter

public actual object EmptyConverter : ProtobufConverter<Empty> by EmptyJvmConverter
