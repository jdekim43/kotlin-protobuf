// Transform from google/protobuf/field_mask.proto
@file:GeneratorVersion(version = "0.5.1")

package google.protobuf

import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper

public open class FieldMaskJvmConverter :
    ProtobufTypeMapper<FieldMask, com.google.protobuf.FieldMask> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.FieldMask.getDescriptor()

  override val parser: Parser<com.google.protobuf.FieldMask> =
      com.google.protobuf.FieldMask.parser()

  override val default: com.google.protobuf.FieldMask =
      com.google.protobuf.FieldMask.getDefaultInstance()

  override fun convert(obj: com.google.protobuf.FieldMask): FieldMask = FieldMask(
  	paths = obj.getPathsList().map { it },
  )

  override fun convert(obj: FieldMask): com.google.protobuf.FieldMask {
    val builder = com.google.protobuf.FieldMask.newBuilder()
    builder.addAllPaths(obj.paths.map { it })
    return builder.build()
  }
}
