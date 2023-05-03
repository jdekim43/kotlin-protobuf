package kr.jadekim.protobuf.generator.file

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import kr.jadekim.protobuf.annotation.ProtobufSyntax
import kr.jadekim.protobuf.generator.type.EnumTypeGenerator
import kr.jadekim.protobuf.generator.type.MessageTypeGenerator
import kr.jadekim.protobuf.generator.util.extention.outputFileName
import kr.jadekim.protobuf.generator.util.extention.outputPackage

class TypeFileGenerator(
    val enumTypeGenerator: EnumTypeGenerator,
    val messageTypeGenerator: MessageTypeGenerator,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.outputPackage, descriptor.outputFileName)
        spec.addFileComment("Transform from %L", descriptor.name)

        spec.addAnnotation(
            AnnotationSpec.builder(ProtobufSyntax::class)
                .addMember("syntax = %S", descriptor.syntax.name)
                .build()
        )

        for (enumDescriptor in descriptor.enumTypes) {
            val (enumSpec, imports) = enumTypeGenerator.generate(enumDescriptor)
            imports.forEach(spec::addImport)
            spec.addType(enumSpec)
        }

        for (messageDescriptor in descriptor.messageTypes) {
            val (messageSpec, imports) = messageTypeGenerator.generate(messageDescriptor)
            imports.forEach(spec::addImport)
            spec.addType(messageSpec)
        }

        return spec.build()
    }
}