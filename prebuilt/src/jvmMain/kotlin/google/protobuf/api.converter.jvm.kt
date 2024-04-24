// Transform from google/protobuf/api.proto
@file:GeneratorVersion(version = "0.4.0")

package google.protobuf

import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper

public object ApiJvmConverter : ProtobufTypeMapper<Api, com.google.protobuf.Api> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.Api.getDescriptor()

  override val parser: Parser<com.google.protobuf.Api> = com.google.protobuf.Api.parser()

  override fun convert(obj: com.google.protobuf.Api): Api = Api(
  	name = obj.getName(),
  	methods = obj.getMethodsList().map { MethodJvmConverter.convert(it) },
  	options = obj.getOptionsList().map { OptionJvmConverter.convert(it) },
  	version = obj.getVersion(),
  	sourceContext = SourceContextJvmConverter.convert(obj.getSourceContext()),
  	mixins = obj.getMixinsList().map { MixinJvmConverter.convert(it) },
  	syntax = Syntax.forNumber(obj.getSyntax().number),
  )

  override fun convert(obj: Api): com.google.protobuf.Api {
    val builder = com.google.protobuf.Api.newBuilder()
    builder.setName(obj.name)
    builder.addAllMethods(obj.methods.map { MethodJvmConverter.convert(it) })
    builder.addAllOptions(obj.options.map { OptionJvmConverter.convert(it) })
    builder.setVersion(obj.version)
    val value4 = obj.sourceContext
    if (value4 != null) {
      builder.setSourceContext(SourceContextJvmConverter.convert(value4))
    }
    builder.addAllMixins(obj.mixins.map { MixinJvmConverter.convert(it) })
    builder.setSyntax(com.google.protobuf.Syntax.forNumber(obj.syntax.number))
    return builder.build()
  }
}

public object MethodJvmConverter : ProtobufTypeMapper<Method, com.google.protobuf.Method> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.Method.getDescriptor()

  override val parser: Parser<com.google.protobuf.Method> = com.google.protobuf.Method.parser()

  override fun convert(obj: com.google.protobuf.Method): Method = Method(
  	name = obj.getName(),
  	requestTypeUrl = obj.getRequestTypeUrl(),
  	requestStreaming = obj.getRequestStreaming(),
  	responseTypeUrl = obj.getResponseTypeUrl(),
  	responseStreaming = obj.getResponseStreaming(),
  	options = obj.getOptionsList().map { OptionJvmConverter.convert(it) },
  	syntax = Syntax.forNumber(obj.getSyntax().number),
  )

  override fun convert(obj: Method): com.google.protobuf.Method {
    val builder = com.google.protobuf.Method.newBuilder()
    builder.setName(obj.name)
    builder.setRequestTypeUrl(obj.requestTypeUrl)
    builder.setRequestStreaming(obj.requestStreaming)
    builder.setResponseTypeUrl(obj.responseTypeUrl)
    builder.setResponseStreaming(obj.responseStreaming)
    builder.addAllOptions(obj.options.map { OptionJvmConverter.convert(it) })
    builder.setSyntax(com.google.protobuf.Syntax.forNumber(obj.syntax.number))
    return builder.build()
  }
}

public object MixinJvmConverter : ProtobufTypeMapper<Mixin, com.google.protobuf.Mixin> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.Mixin.getDescriptor()

  override val parser: Parser<com.google.protobuf.Mixin> = com.google.protobuf.Mixin.parser()

  override fun convert(obj: com.google.protobuf.Mixin): Mixin = Mixin(
  	name = obj.getName(),
  	root = obj.getRoot(),
  )

  override fun convert(obj: Mixin): com.google.protobuf.Mixin {
    val builder = com.google.protobuf.Mixin.newBuilder()
    builder.setName(obj.name)
    builder.setRoot(obj.root)
    return builder.build()
  }
}
