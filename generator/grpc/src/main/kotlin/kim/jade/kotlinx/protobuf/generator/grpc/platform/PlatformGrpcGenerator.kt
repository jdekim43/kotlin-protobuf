package kim.jade.kotlinx.protobuf.generator.grpc.platform

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.TypeSpec
import kim.jade.kotlinx.protobuf.generator.ImportName

interface PlatformGrpcGenerator {

    fun generate(descriptor: Descriptors.ServiceDescriptor): Pair<List<TypeSpec>, Set<ImportName>>
}