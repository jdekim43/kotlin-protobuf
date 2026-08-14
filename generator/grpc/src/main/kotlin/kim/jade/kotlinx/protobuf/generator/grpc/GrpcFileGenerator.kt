package kim.jade.kotlinx.protobuf.generator.grpc

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FileSpec
import kim.jade.kotlinx.protobuf.generator.addTo
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.platform.PlatformGrpcGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.util.extension.outputGrpcFileName
import kim.jade.kotlinx.protobuf.generator.grpc.util.extension.outputGrpcPackageName
import kim.jade.kotlinx.protobuf.generator.util.extention.addGeneratorVersionAnnotation

class GrpcFileGenerator(
    vararg val platformGenerators: PlatformGrpcGenerator,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.outputGrpcPackageName, descriptor.outputGrpcFileName)
        spec.addFileComment("Transform from %L", descriptor.name)

        spec.addGeneratorVersionAnnotation()

        for (serviceDescriptor in descriptor.services) {
            for (generator in platformGenerators) {
                val (specs, imports) = generator.generate(serviceDescriptor)
                imports.addTo(spec)
                specs.forEach(spec::addType)
            }
        }

        return spec.build()
    }
}