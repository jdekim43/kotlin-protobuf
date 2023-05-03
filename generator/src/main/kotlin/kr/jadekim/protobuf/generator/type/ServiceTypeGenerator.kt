package kr.jadekim.protobuf.generator.type

import com.google.protobuf.Descriptors
import kr.jadekim.protobuf.generator.converter.mapper.ServiceMapperGenerator

class ServiceTypeGenerator(
    val plugins: TypeGeneratorPlugins<Descriptors.ServiceDescriptor> = emptyList(),
) : TypeGenerator<Descriptors.ServiceDescriptor> by ServiceMapperGenerator(plugins)