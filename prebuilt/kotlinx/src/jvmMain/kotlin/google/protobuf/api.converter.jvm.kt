// Transform from google/protobuf/api.proto
@file:GeneratorVersion(version = "0.2.3")

package google.protobuf

import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper

public object ApiJvmConverter : ProtobufTypeMapper<Api, com.google.protobuf.Api> {
  public override val descriptor: Descriptors.Descriptor = com.google.protobuf.Api.getDescriptor()

  public override val parser: Parser<com.google.protobuf.Api> = com.google.protobuf.Api.parser()

  public override fun convert(obj: com.google.protobuf.Api): Api = Api(
  	name = obj.name,
  	methods = obj.methodsList.map { MethodJvmConverter.convert(it) },
  	options = obj.optionsList.map { OptionJvmConverter.convert(it) },
  	version = obj.version,
  	sourceContext = SourceContextJvmConverter.convert(obj.sourceContext),
  	mixins = obj.mixinsList.map { MixinJvmConverter.convert(it) },
  	syntax = Syntax.forNumber(obj.syntax.number),
  )

  public override fun convert(obj: Api): com.google.protobuf.Api {
    val builder = com.google.protobuf.Api.newBuilder()
    builder.setName(obj.name)
    builder.addAllMethods(obj.methods.map { MethodJvmConverter.convert(it) })
    builder.addAllOptions(obj.options.map { OptionJvmConverter.convert(it) })
    builder.setVersion(obj.version)
    builder.setSourceContext(SourceContextJvmConverter.convert(obj.sourceContext))
    builder.addAllMixins(obj.mixins.map { MixinJvmConverter.convert(it) })
    builder.setSyntax(com.google.protobuf.Syntax.forNumber(obj.syntax.number))
    return builder.build()
  }
}

public object MethodJvmConverter : ProtobufTypeMapper<Method, com.google.protobuf.Method> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.Method.getDescriptor()

  public override val parser: Parser<com.google.protobuf.Method> =
      com.google.protobuf.Method.parser()

  public override fun convert(obj: com.google.protobuf.Method): Method = Method(
  	name = obj.name,
  	requestTypeUrl = obj.requestTypeUrl,
  	requestStreaming = obj.requestStreaming,
  	responseTypeUrl = obj.responseTypeUrl,
  	responseStreaming = obj.responseStreaming,
  	options = obj.optionsList.map { OptionJvmConverter.convert(it) },
  	syntax = Syntax.forNumber(obj.syntax.number),
  )

  public override fun convert(obj: Method): com.google.protobuf.Method {
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
  public override val descriptor: Descriptors.Descriptor = com.google.protobuf.Mixin.getDescriptor()

  public override val parser: Parser<com.google.protobuf.Mixin> = com.google.protobuf.Mixin.parser()

  public override fun convert(obj: com.google.protobuf.Mixin): Mixin = Mixin(
  	name = obj.name,
  	root = obj.root,
  )

  public override fun convert(obj: Mixin): com.google.protobuf.Mixin {
    val builder = com.google.protobuf.Mixin.newBuilder()
    builder.setName(obj.name)
    builder.setRoot(obj.root)
    return builder.build()
  }
}
