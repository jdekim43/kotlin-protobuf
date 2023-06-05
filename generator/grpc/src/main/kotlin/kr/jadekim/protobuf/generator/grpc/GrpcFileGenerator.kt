package kr.jadekim.protobuf.generator.grpc

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FileSpec
import kr.jadekim.protobuf.generator.addTo
import kr.jadekim.protobuf.generator.converter.jvm.mapper.ServiceMapperGenerator
import kr.jadekim.protobuf.generator.file.FileGenerator
import kr.jadekim.protobuf.generator.util.extention.addGeneratorVersionAnnotation
import kr.jadekim.protobuf.generator.util.extention.addSyntaxAnnotation
import kr.jadekim.protobuf.generator.util.extention.outputGrpcFileName
import kr.jadekim.protobuf.generator.util.extention.outputPackage

class GrpcFileGenerator(
    private val serviceMapperGenerator: ServiceMapperGenerator,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.outputPackage, descriptor.outputGrpcFileName)
        spec.addFileComment("Transform from %L", descriptor.name)

        spec.addSyntaxAnnotation(descriptor)
        spec.addGeneratorVersionAnnotation()

        for (serviceDescriptor in descriptor.services) {
            val (serviceSpec, imports) = serviceMapperGenerator.generate(serviceDescriptor)
            imports.addTo(spec)
            spec.addType(serviceSpec)
        }

        return spec.build()
    }
}