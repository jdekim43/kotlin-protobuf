// Transform from google/protobuf/timestamp.proto
@file:GeneratorVersion(version = "0.3.3")

package google.protobuf

import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter

public actual object TimestampConverter : ProtobufConverter<Timestamp> by TimestampJvmConverter
