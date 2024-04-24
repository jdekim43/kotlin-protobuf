package kr.jadekim.protobuf.generator.grpc.gateway

import com.google.api.AnnotationsProto
import com.google.protobuf.ExtensionRegistry
import kr.jadekim.protobuf.generator.Generator
import kr.jadekim.protobuf.generator.file.FileGenerator

fun main(args: Array<String>) = Generator.runWith(GrpcGatewayGenerator)

object GrpcGatewayGenerator : Generator() {

    override val generators: List<FileGenerator> = listOf(
        GrpcGatewayFileGenerator(GrpcGatewayServiceGenerator()),
    )

    override fun onRegisterExtension(registry: ExtensionRegistry) {
        registry.add(AnnotationsProto.http)
    }
}