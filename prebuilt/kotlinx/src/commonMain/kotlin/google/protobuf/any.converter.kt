// Transform from google/protobuf/any.proto
@file:GeneratorVersion(version = "0.4.1")

package google.protobuf

import java.lang.IllegalStateException
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.converter.parseProtobuf

public expect object AnyConverter : ProtobufConverter<Any>

public fun Any.toAny(): Any = Any(Any.TYPE_URL, with(AnyConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Any> = AnyConverter): Any {
  if (typeUrl != Any.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val Any.Companion.converter: AnyConverter
  get() = AnyConverter
