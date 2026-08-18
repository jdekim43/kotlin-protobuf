package kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.js

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FileSpec
import kim.jade.kotlinx.protobuf.generator.addTo
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.js.mapper.ServiceJsMapperGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.js.util.extension.outputJsGrpcFileName
import kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.js.util.extension.outputJsGrpcPackageName
import kim.jade.kotlinx.protobuf.generator.util.extention.addGeneratorVersionAnnotation

class JsGrpcFileGenerator(
    private val serviceJsMapperGenerator: ServiceJsMapperGenerator,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.outputJsGrpcPackageName, descriptor.outputJsGrpcFileName)
        spec.addFileComment("Transform from %L", descriptor.name)

        spec.addGeneratorVersionAnnotation()

        for (serviceDescriptor in descriptor.services) {
            val (serviceSpec, imports) = serviceJsMapperGenerator.generate(serviceDescriptor)
            imports.addTo(spec)
            spec.addType(serviceSpec)
        }

        return spec.build()
    }
}
