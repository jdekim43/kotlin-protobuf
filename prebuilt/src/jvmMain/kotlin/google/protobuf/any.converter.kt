// Transform from google/protobuf/any.proto
@file:GeneratorVersion(version = "0.4.0")

package google.protobuf

import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter

public actual object AnyConverter : AnyJvmConverter(), ProtobufConverter<Any>
