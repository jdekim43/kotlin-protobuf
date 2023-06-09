// Transform from google/protobuf/source_context.proto
@file:GeneratorVersion(version = "0.3.3")

package google.protobuf

import java.lang.IllegalStateException
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.converter.parseProtobuf

public expect object SourceContextConverter : ProtobufConverter<SourceContext>

public fun SourceContext.toAny(): Any = Any(SourceContext.TYPE_URL, with(SourceContextConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<SourceContext>): SourceContext {
  if (typeUrl != SourceContext.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}
