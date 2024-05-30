// Transform from google/protobuf/empty.proto
@file:GeneratorVersion(version = "0.5.1")

package google.protobuf

import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper

public open class EmptyJvmConverter : ProtobufTypeMapper<Empty, com.google.protobuf.Empty> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.Empty.getDescriptor()

  override val parser: Parser<com.google.protobuf.Empty> = com.google.protobuf.Empty.parser()

  override val default: com.google.protobuf.Empty = com.google.protobuf.Empty.getDefaultInstance()

  override fun convert(obj: com.google.protobuf.Empty): Empty = Empty(
  )

  override fun convert(obj: Empty): com.google.protobuf.Empty {
    val builder = com.google.protobuf.Empty.newBuilder()
    return builder.build()
  }
}
