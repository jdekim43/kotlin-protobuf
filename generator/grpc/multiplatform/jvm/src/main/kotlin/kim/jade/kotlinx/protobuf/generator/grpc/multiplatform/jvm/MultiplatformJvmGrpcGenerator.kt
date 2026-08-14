package kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.jvm

import kim.jade.kotlinx.protobuf.generator.Generator
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.GrpcFileGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.jvm.JvmGrpcFileGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.jvm.mapper.ServiceMapperGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.jvm.util.extension.jvmGrpcTypeName
import kim.jade.kotlinx.protobuf.generator.grpc.platform.SinglePlatformGenerator

fun main(args: Array<String>) = Generator.runWith(MultiplatformJvmGrpcGenerator)

object MultiplatformJvmGrpcGenerator : Generator() {

    override val generators: List<FileGenerator> = listOf(
        JvmGrpcFileGenerator(ServiceMapperGenerator()),
        GrpcFileGenerator(SinglePlatformGenerator(isActual = true) { jvmGrpcTypeName }),
    )
}