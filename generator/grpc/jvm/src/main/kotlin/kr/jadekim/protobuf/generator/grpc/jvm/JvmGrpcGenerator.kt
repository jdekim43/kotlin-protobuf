package kr.jadekim.protobuf.generator.grpc.jvm

import kr.jadekim.protobuf.generator.Generator
import kr.jadekim.protobuf.generator.file.FileGenerator
import kr.jadekim.protobuf.generator.grpc.GrpcFileGenerator
import kr.jadekim.protobuf.generator.grpc.jvm.mapper.ServiceMapperGenerator
import kr.jadekim.protobuf.generator.grpc.jvm.util.extension.jvmGrpcTypeName
import kr.jadekim.protobuf.generator.grpc.platform.SinglePlatformGenerator

fun main(args: Array<String>) = Generator.runWith(JvmGrpcGenerator)

object JvmGrpcGenerator : Generator() {

    override val generators: List<FileGenerator> = listOf(
        JvmGrpcFileGenerator(ServiceMapperGenerator()),
        GrpcFileGenerator(SinglePlatformGenerator { jvmGrpcTypeName }),
    )
}