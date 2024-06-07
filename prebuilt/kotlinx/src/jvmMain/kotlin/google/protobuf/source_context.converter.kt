// Transform from google/protobuf/source_context.proto
@file:GeneratorVersion(version = "0.5.2")

package google.protobuf

import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter

public actual object SourceContextConverter : SourceContextJvmConverter(),
    ProtobufConverter<SourceContext>
