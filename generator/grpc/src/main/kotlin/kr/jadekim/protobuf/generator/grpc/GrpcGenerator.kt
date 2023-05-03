package kr.jadekim.protobuf.generator.grpc

import kr.jadekim.protobuf.generator.Generator
import kr.jadekim.protobuf.generator.file.FileGenerator
import kr.jadekim.protobuf.generator.file.GrpcFileGenerator
import kr.jadekim.protobuf.generator.type.ServiceTypeGenerator

fun main(args: Array<String>) = Generator.runWith(GrpcGenerator)

object GrpcGenerator : Generator() {

    private val serviceTypeGenerator = ServiceTypeGenerator()

    private val grpcGenerator = GrpcFileGenerator(
        serviceTypeGenerator,
    )

    override val generators: List<FileGenerator> = listOf(grpcGenerator)
}