// Transform from google/protobuf/field_mask.proto
@file:GeneratorVersion(version = "0.3.2")

package google.protobuf

import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper

public object FieldMaskJvmConverter : ProtobufTypeMapper<FieldMask, com.google.protobuf.FieldMask> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.FieldMask.getDescriptor()

  public override val parser: Parser<com.google.protobuf.FieldMask> =
      com.google.protobuf.FieldMask.parser()

  public override fun convert(obj: com.google.protobuf.FieldMask): FieldMask = FieldMask(
  	paths = obj.getPathsList().map { it },
  )

  public override fun convert(obj: FieldMask): com.google.protobuf.FieldMask {
    val builder = com.google.protobuf.FieldMask.newBuilder()
    builder.addAllPaths(obj.paths.map { it })
    return builder.build()
  }
}
