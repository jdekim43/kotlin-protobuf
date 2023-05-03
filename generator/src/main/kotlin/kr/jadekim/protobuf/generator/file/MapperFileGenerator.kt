package kr.jadekim.protobuf.generator.file

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FileSpec
import kr.jadekim.protobuf.generator.converter.mapper.MessageMapperGenerator
import kr.jadekim.protobuf.generator.converter.mapper.util.extention.mapperOutputPackage
import kr.jadekim.protobuf.generator.util.extention.outputFileName

class MapperFileGenerator(
    val messageMapperGenerator: MessageMapperGenerator,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.mapperOutputPackage, descriptor.outputFileName)
        spec.addFileComment("Built for %L", descriptor.name)

        for (messageDescriptor in descriptor.messageTypes) {
            val (mapperSpec, imports) = messageMapperGenerator.generate(messageDescriptor)
            imports.forEach(spec::addImport)
            spec.addType(mapperSpec)
        }

        return spec.build()
    }
}