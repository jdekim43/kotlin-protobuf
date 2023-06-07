package kr.jadekim.protobuf.generator.grpc.multiplatform

import kr.jadekim.protobuf.generator.Generator
import kr.jadekim.protobuf.generator.file.FileGenerator
import kr.jadekim.protobuf.generator.grpc.GrpcFileGenerator
import kr.jadekim.protobuf.generator.grpc.platform.MultiplePlatformGenerator

fun main(args: Array<String>) = Generator.runWith(MultiplatformGrpcGenerator)

object MultiplatformGrpcGenerator : Generator() {

    override val generators: List<FileGenerator> = listOf(
        GrpcFileGenerator(MultiplePlatformGenerator()),
    )
}