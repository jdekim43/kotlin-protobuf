package kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.js

import kim.jade.kotlinx.protobuf.generator.Generator
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.GrpcFileGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.js.mapper.ServiceJsMapperGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.js.util.extension.jsGrpcTypeName
import kim.jade.kotlinx.protobuf.generator.grpc.platform.SinglePlatformGenerator

fun main(args: Array<String>) = Generator.runWith(MultiplatformJsGrpcGenerator)

object MultiplatformJsGrpcGenerator : Generator() {

    override val generators: List<FileGenerator> = listOf(
        JsGrpcFileGenerator(ServiceJsMapperGenerator()),
        GrpcFileGenerator(SinglePlatformGenerator(isActual = true) { jsGrpcTypeName }),
    )
}
