package kim.jade.kotlinx.protobuf.generator.grpc.gateway

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FileSpec
import kim.jade.kotlinx.protobuf.generator.addTo
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.gateway.util.extension.outputGrpcGatewayFileName
import kim.jade.kotlinx.protobuf.generator.grpc.gateway.util.extension.outputGrpcGatewayPackageName
import kim.jade.kotlinx.protobuf.generator.util.extention.addGeneratorVersionAnnotation

class GrpcGatewayFileGenerator(
    val grpcGatewayServiceGenerator: GrpcGatewayServiceGenerator,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.outputGrpcGatewayPackageName, descriptor.outputGrpcGatewayFileName)
        spec.addFileComment("Transform from %L", descriptor.name)
        spec.addGeneratorVersionAnnotation()

        for (serviceDescriptor in descriptor.services) {
            val (specs, imports) = grpcGatewayServiceGenerator.generate(serviceDescriptor)
            imports.addTo(spec)
            specs.forEach(spec::addType)
        }

        return spec.build()
    }
}