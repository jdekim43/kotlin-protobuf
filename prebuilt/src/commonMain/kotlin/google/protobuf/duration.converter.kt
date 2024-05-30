// Transform from google/protobuf/duration.proto
@file:GeneratorVersion(version = "0.5.1")

package google.protobuf

import java.lang.IllegalStateException
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.converter.parseProtobuf

public expect object DurationConverter : ProtobufConverter<Duration>

public fun Duration.toAny(): Any = Any(Duration.TYPE_URL, with(DurationConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Duration> = DurationConverter): Duration {
  if (typeUrl != Duration.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val Duration.Companion.converter: DurationConverter
  get() = DurationConverter
