package kim.jade.kotlinx.protobuf.generator.grpc.gateway

import com.google.api.AnnotationsProto
import com.google.protobuf.ExtensionRegistry
import kim.jade.kotlinx.protobuf.generator.Generator
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator

fun main(args: Array<String>) = Generator.runWith(GrpcGatewayGenerator)

object GrpcGatewayGenerator : Generator() {

    override val generators: List<FileGenerator> = listOf(
        GrpcGatewayFileGenerator(GrpcGatewayServiceGenerator()),
    )

    override fun onRegisterExtension(registry: ExtensionRegistry) {
        registry.add(AnnotationsProto.http)
    }
}