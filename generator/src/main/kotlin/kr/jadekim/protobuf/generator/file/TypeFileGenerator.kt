package kr.jadekim.protobuf.generator.file

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FileSpec
import kr.jadekim.protobuf.generator.addTo
import kr.jadekim.protobuf.generator.type.EnumTypeGenerator
import kr.jadekim.protobuf.generator.type.MessageTypeGenerator
import kr.jadekim.protobuf.generator.util.extention.addGeneratorVersionAnnotation
import kr.jadekim.protobuf.generator.util.extention.addSyntaxAnnotation
import kr.jadekim.protobuf.generator.util.extention.outputFileName
import kr.jadekim.protobuf.generator.util.extention.outputPackage

class TypeFileGenerator(
    val enumTypeGenerator: EnumTypeGenerator,
    val messageTypeGenerator: MessageTypeGenerator,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.outputPackage, descriptor.outputFileName)
        spec.addFileComment("Transform from %L", descriptor.name)

        spec.addSyntaxAnnotation(descriptor)
        spec.addGeneratorVersionAnnotation()

        for (enumDescriptor in descriptor.enumTypes) {
            val (enumSpec, imports) = enumTypeGenerator.generate(enumDescriptor)
            imports.addTo(spec)
            spec.addType(enumSpec)
        }

        for (messageDescriptor in descriptor.messageTypes) {
            val (messageSpec, imports) = messageTypeGenerator.generate(messageDescriptor)
            imports.addTo(spec)
            spec.addType(messageSpec)
        }

        return spec.build()
    }
}