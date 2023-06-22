// Transform from google/protobuf/source_context.proto
@file:GeneratorVersion(version = "0.3.2")

package google.protobuf

import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper

public object SourceContextJvmConverter :
    ProtobufTypeMapper<SourceContext, com.google.protobuf.SourceContext> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.SourceContext.getDescriptor()

  public override val parser: Parser<com.google.protobuf.SourceContext> =
      com.google.protobuf.SourceContext.parser()

  public override fun convert(obj: com.google.protobuf.SourceContext): SourceContext =
      SourceContext(
  	fileName = obj.getFileName(),
  )

  public override fun convert(obj: SourceContext): com.google.protobuf.SourceContext {
    val builder = com.google.protobuf.SourceContext.newBuilder()
    builder.setFileName(obj.fileName)
    return builder.build()
  }
}
