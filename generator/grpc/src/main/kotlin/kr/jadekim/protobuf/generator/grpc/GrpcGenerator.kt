package kr.jadekim.protobuf.generator.grpc

import kr.jadekim.protobuf.generator.Generator
import kr.jadekim.protobuf.generator.converter.jvm.mapper.ServiceMapperGenerator
import kr.jadekim.protobuf.generator.file.FileGenerator

fun main(args: Array<String>) = Generator.runWith(GrpcGenerator)

object GrpcGenerator : Generator() {

    private val serviceTypeGenerator = ServiceMapperGenerator()

    private val grpcGenerator = GrpcFileGenerator(
        serviceTypeGenerator,
    )

    override val generators: List<FileGenerator> = listOf(grpcGenerator)
}