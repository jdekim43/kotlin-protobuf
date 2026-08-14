package kim.jade.kotlinx.protobuf.generator.converter.jvm

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FileSpec
import kim.jade.kotlinx.protobuf.generator.addTo
import kim.jade.kotlinx.protobuf.generator.converter.jvm.mapper.MessageMapperGenerator
import kim.jade.kotlinx.protobuf.generator.converter.jvm.util.extention.outputJvmConverterFileName
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.util.extention.addGeneratorVersionAnnotation
import kim.jade.kotlinx.protobuf.generator.util.extention.outputPackage

class JvmConverterFileGenerator(
    val messageMapperGenerator: MessageMapperGenerator,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.outputPackage, descriptor.outputJvmConverterFileName)
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