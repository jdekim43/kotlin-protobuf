package kr.jadekim.protobuf.generator.file

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import kr.jadekim.protobuf.annotation.ProtobufSyntax
import kr.jadekim.protobuf.generator.type.ServiceTypeGenerator
import kr.jadekim.protobuf.generator.util.extention.outputGrpcFileName
import kr.jadekim.protobuf.generator.util.extention.outputPackage

class GrpcFileGenerator(
    private val serviceTypeGenerator: ServiceTypeGenerator,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.outputPackage, descriptor.outputGrpcFileName)
        spec.addFileComment("Transform from %L", descriptor.name)

        spec.addAnnotation(
            AnnotationSpec.builder(ProtobufSyntax::class)
                .addMember("syntax = %S", descriptor.syntax.name)
                .build()
        )

        for (serviceDescriptor in descriptor.services) {
            val (serviceSpec, imports) = serviceTypeGenerator.generate(serviceDescriptor)
            imports.forEach(spec::addImport)
            spec.addType(serviceSpec)
        }

        return spec.build()
    }
}