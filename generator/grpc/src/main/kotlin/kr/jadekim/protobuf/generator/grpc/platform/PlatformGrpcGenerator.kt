package kr.jadekim.protobuf.generator.grpc.platform

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.TypeSpec
import kr.jadekim.protobuf.generator.ImportName

interface PlatformGrpcGenerator {

    fun generate(descriptor: Descriptors.ServiceDescriptor): Pair<List<TypeSpec>, Set<ImportName>>
}