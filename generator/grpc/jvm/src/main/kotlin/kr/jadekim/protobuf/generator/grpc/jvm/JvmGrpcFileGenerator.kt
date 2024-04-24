package kr.jadekim.protobuf.generator.grpc.jvm

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FileSpec
import kr.jadekim.protobuf.generator.addTo
import kr.jadekim.protobuf.generator.file.FileGenerator
import kr.jadekim.protobuf.generator.grpc.jvm.mapper.ServiceMapperGenerator
import kr.jadekim.protobuf.generator.grpc.jvm.util.extension.outputJvmGrpcFileName
import kr.jadekim.protobuf.generator.grpc.jvm.util.extension.outputJvmGrpcPackageName
import kr.jadekim.protobuf.generator.util.extention.addGeneratorVersionAnnotation

class JvmGrpcFileGenerator(
    private val serviceMapperGenerator: ServiceMapperGenerator,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.outputJvmGrpcPackageName, descriptor.outputJvmGrpcFileName)
        spec.addFileComment("Transform from %L", descriptor.name)

        spec.addGeneratorVersionAnnotation()

        for (serviceDescriptor in descriptor.services) {
            val (serviceSpec, imports) = serviceMapperGenerator.generate(serviceDescriptor)
            imports.addTo(spec)
            spec.addType(serviceSpec)
        }

        return spec.build()
    }
}