// Transform from google/protobuf/field_mask.proto
@file:GeneratorVersion(version = "0.5.2")

package google.protobuf

import java.lang.IllegalStateException
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.converter.parseProtobuf

public expect object FieldMaskConverter : ProtobufConverter<FieldMask>

public fun FieldMask.toAny(): Any = Any(FieldMask.TYPE_URL, with(FieldMaskConverter) { toByteArray()
    })

public fun Any.parse(converter: ProtobufConverter<FieldMask> = FieldMaskConverter): FieldMask {
  if (typeUrl != FieldMask.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val FieldMask.Companion.converter: FieldMaskConverter
  get() = FieldMaskConverter
