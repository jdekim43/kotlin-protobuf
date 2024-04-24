package kr.jadekim.protobuf.generator.grpc.platform

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kr.jadekim.protobuf.generator.ImportName
import kr.jadekim.protobuf.generator.grpc.util.extension.grpcClientTypeName
import kr.jadekim.protobuf.generator.grpc.util.extension.grpcServerTypeName
import kr.jadekim.protobuf.generator.grpc.util.extension.grpcTypeName
import kr.jadekim.protobuf.generator.grpc.util.extension.interfaceTypeName
import kr.jadekim.protobuf.generator.util.extention.typeName
import kr.jadekim.protobuf.grpc.ClientOption
import kr.jadekim.protobuf.grpc.GrpcService
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


//TODO: Remove duplicated codes with SinglePlatformGenerator
class MultiplePlatformGenerator : PlatformGrpcGenerator {

    override fun generate(descriptor: Descriptors.ServiceDescriptor): Pair<List<TypeSpec>, Set<ImportName>> {
        val spec = TypeSpec.objectBuilder(descriptor.grpcTypeName)
        val imports = mutableSetOf<ImportName>()

        spec.addModifiers(KModifier.EXPECT)

        val serverTypeName = descriptor.writeServerTo(spec)
        val clientTypeName = descriptor.writeClientTo(spec)

        spec.addSuperinterface(
            GrpcService::class.typeName.parameterizedBy(
                descriptor.interfaceTypeName,
                clientTypeName,
            )
        )

        return listOf(spec.build()) to imports.toSet()
    }

    private fun Descriptors.ServiceDescriptor.writeServerTo(spec: TypeSpec.Builder): TypeName {
        val name = grpcServerTypeName
        val serverSpec = TypeSpec.classBuilder(name)
            .addModifiers(KModifier.ABSTRACT)

        serverSpec.addSuperinterface(interfaceTypeName)

        serverSpec.primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter(
                    ParameterSpec.builder("coroutineContext", CoroutineContext::class)
                        .defaultValue("%T", EmptyCoroutineContext::class).build(),
                )
                .build(),
        )

        spec.addType(serverSpec.build())

        return name
    }

    private fun Descriptors.ServiceDescriptor.writeClientTo(spec: TypeSpec.Builder): TypeName {
        val name = grpcClientTypeName
        val clientSpec = TypeSpec.classBuilder(name)
            .addModifiers(KModifier.OPEN)

        clientSpec.addSuperinterface(interfaceTypeName)

        clientSpec.primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("option", ClientOption::class)
                .build()
        )

        spec.addType(clientSpec.build())

        return name
    }
}