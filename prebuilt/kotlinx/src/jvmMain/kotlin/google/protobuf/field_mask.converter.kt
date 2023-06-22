// Transform from google/protobuf/field_mask.proto
@file:GeneratorVersion(version = "0.3.3")

package google.protobuf

import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter

public actual object FieldMaskConverter : ProtobufConverter<FieldMask> by FieldMaskJvmConverter
