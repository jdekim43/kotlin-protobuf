package kr.jadekim.protobuf.generator.converter

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FileSpec
import kr.jadekim.protobuf.generator.addTo
import kr.jadekim.protobuf.generator.converter.platform.PlatformConverterGenerator
import kr.jadekim.protobuf.generator.converter.util.extention.outputConverterFileName
import kr.jadekim.protobuf.generator.file.FileGenerator
import kr.jadekim.protobuf.generator.util.extention.addGeneratorVersionAnnotation
import kr.jadekim.protobuf.generator.util.extention.outputPackage

class ConverterFileGenerator(
    vararg val platformGenerators: PlatformConverterGenerator<Descriptors.Descriptor>,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.outputPackage, descriptor.outputConverterFileName)
        spec.addFileComment("Transform from %L", descriptor.name)

        spec.addGeneratorVersionAnnotation()

        for (messageDescriptor in descriptor.messageTypes) {
            for (generator in platformGenerators) {
                val (specs, imports) = generator.generate(messageDescriptor)
                imports.addTo(spec)
                specs.forEach(spec::addType)
            }
        }

        return spec.build()
    }
}