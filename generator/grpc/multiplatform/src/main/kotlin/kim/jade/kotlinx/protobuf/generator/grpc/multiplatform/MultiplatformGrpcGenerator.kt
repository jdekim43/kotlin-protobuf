package kim.jade.kotlinx.protobuf.generator.grpc.multiplatform

import kim.jade.kotlinx.protobuf.generator.Generator
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.GrpcFileGenerator
import kim.jade.kotlinx.protobuf.generator.grpc.platform.MultiplePlatformGenerator

fun main(args: Array<String>) = Generator.runWith(MultiplatformGrpcGenerator)

object MultiplatformGrpcGenerator : Generator() {

    override val generators: List<FileGenerator> = listOf(
        GrpcFileGenerator(MultiplePlatformGenerator()),
    )
}