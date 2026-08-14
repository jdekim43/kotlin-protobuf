package kim.jade.kotlinx.protobuf.generator.grpc.jvm

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FileSpec
import kim.jade.kotlinx.protobuf.generator.addTo
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.jvm.mapper.ServiceMapperGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.jvm.util.extension.outputJvmGrpcFileName
import kim.jade.kotlinx.protobuf.generator.grpc.jvm.util.extension.outputJvmGrpcPackageName
import kim.jade.kotlinx.protobuf.generator.util.extention.addGeneratorVersionAnnotation

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