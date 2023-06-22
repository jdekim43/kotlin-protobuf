// Transform from google/protobuf/timestamp.proto
@file:GeneratorVersion(version = "0.3.3")

package google.protobuf

import java.lang.IllegalStateException
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.converter.parseProtobuf

public expect object TimestampConverter : ProtobufConverter<Timestamp>

public fun Timestamp.toAny(): Any = Any(Timestamp.TYPE_URL, with(TimestampConverter) { toByteArray()
    })

public fun Any.parse(converter: ProtobufConverter<Timestamp>): Timestamp {
  if (typeUrl != Timestamp.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}
