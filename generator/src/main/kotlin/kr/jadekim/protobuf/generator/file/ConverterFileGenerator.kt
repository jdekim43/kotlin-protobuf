package kr.jadekim.protobuf.generator.file

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FileSpec
import kr.jadekim.protobuf.generator.addTo
import kr.jadekim.protobuf.generator.converter.mapper.MessageMapperGenerator
import kr.jadekim.protobuf.generator.converter.mapper.util.extention.outputConverterFileName
import kr.jadekim.protobuf.generator.util.extention.addGeneratorVersionAnnotation
import kr.jadekim.protobuf.generator.util.extention.outputPackage

class ConverterFileGenerator(
    val messageMapperGenerator: MessageMapperGenerator,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.outputPackage, descriptor.outputConverterFileName)
        spec.addFileComment("Transform from %L", descriptor.name)

        spec.addGeneratorVersionAnnotation()

        for (messageDescriptor in descriptor.messageTypes) {
            val (mapperSpec, imports) = messageMapperGenerator.generate(messageDescriptor)
            imports.addTo(spec)
            spec.addType(mapperSpec)
        }

        return spec.build()
    }
}