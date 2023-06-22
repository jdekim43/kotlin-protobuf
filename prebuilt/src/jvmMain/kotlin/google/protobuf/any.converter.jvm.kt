// Transform from google/protobuf/any.proto
@file:GeneratorVersion(version = "0.2.3")

package google.protobuf

import com.google.protobuf.ByteString
import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper

public object AnyJvmConverter : ProtobufTypeMapper<Any, com.google.protobuf.Any> {
  public override val descriptor: Descriptors.Descriptor = com.google.protobuf.Any.getDescriptor()

  public override val parser: Parser<com.google.protobuf.Any> = com.google.protobuf.Any.parser()

  public override fun convert(obj: com.google.protobuf.Any): Any = Any(
  	typeUrl = obj.typeUrl,
  	`value` = obj.`value`.toByteArray(),
  )

  public override fun convert(obj: Any): com.google.protobuf.Any {
    val builder = com.google.protobuf.Any.newBuilder()
    builder.setTypeUrl(obj.typeUrl)
    builder.setValue(ByteString.copyFrom(obj.`value`))
    return builder.build()
  }
}