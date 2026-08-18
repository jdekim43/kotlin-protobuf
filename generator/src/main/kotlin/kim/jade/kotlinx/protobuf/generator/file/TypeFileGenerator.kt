package kim.jade.kotlinx.protobuf.generator.file

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FileSpec
import kim.jade.kotlinx.protobuf.generator.addTo
import kim.jade.kotlinx.protobuf.generator.type.EnumTypeGenerator
import kim.jade.kotlinx.protobuf.generator.type.MessageTypeGenerator
import kim.jade.kotlinx.protobuf.generator.type.ServiceTypeGenerator
import kim.jade.kotlinx.protobuf.generator.util.extention.addGeneratorVersionAnnotation
import kim.jade.kotlinx.protobuf.generator.util.extention.addOptionAnnotations
import kim.jade.kotlinx.protobuf.generator.util.extention.addProtobufFileAnnotation
import kim.jade.kotlinx.protobuf.generator.util.extention.outputFileName
import kim.jade.kotlinx.protobuf.generator.util.extention.outputPackage

class TypeFileGenerator(
    val enumTypeGenerator: EnumTypeGenerator,
    val messageTypeGenerator: MessageTypeGenerator,
    val serviceTypeGenerator: ServiceTypeGenerator,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.outputPackage, descriptor.outputFileName)
        spec.addFileComment("Transform from %L", descriptor.name)

        spec.addGeneratorVersionAnnotation()
        spec.addProtobufFileAnnotation(descriptor)
        spec.addOptionAnnotations(descriptor.options)

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

        for (serviceDescriptor in descriptor.services) {
            val (serviceSpec, imports) = serviceTypeGenerator.generate(serviceDescriptor)
            imports.addTo(spec)
            spec.addType(serviceSpec)
        }

        return spec.build()
    }
}