// Transform from google/protobuf/empty.proto
@file:GeneratorVersion(version = "0.4.0")

package google.protobuf

import java.lang.IllegalStateException
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.converter.parseProtobuf

public expect object EmptyConverter : ProtobufConverter<Empty>

public fun Empty.toAny(): Any = Any(Empty.TYPE_URL, with(EmptyConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Empty> = EmptyConverter): Empty {
  if (typeUrl != Empty.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val Empty.Companion.converter: EmptyConverter
  get() = EmptyConverter
