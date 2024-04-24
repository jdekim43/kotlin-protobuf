package kr.jadekim.protobuf.generator.grpc.platform

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kr.jadekim.protobuf.generator.ImportName
import kr.jadekim.protobuf.generator.grpc.util.extension.grpcClientTypeName
import kr.jadekim.protobuf.generator.grpc.util.extension.grpcServerTypeName
import kr.jadekim.protobuf.generator.grpc.util.extension.grpcTypeName
import kr.jadekim.protobuf.generator.grpc.util.extension.interfaceTypeName
import kr.jadekim.protobuf.generator.util.ProtobufWordSplitter
import kr.jadekim.protobuf.generator.util.extention.outputTypeName
import kr.jadekim.protobuf.generator.util.extention.typeName
import kr.jadekim.protobuf.grpc.ClientOption
import kr.jadekim.protobuf.grpc.GrpcService
import net.pearx.kasechange.toCamelCase
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class SinglePlatformGenerator(
    val isActual: Boolean = false,
    val resolvePlatformTypeName: Descriptors.ServiceDescriptor.() -> ClassName,
) : PlatformGrpcGenerator {

    override fun generate(descriptor: Descriptors.ServiceDescriptor): Pair<List<TypeSpec>, Set<ImportName>> {
        val platformTypeName = descriptor.resolvePlatformTypeName()
        val spec = TypeSpec.objectBuilder(descriptor.grpcTypeName)
        val imports = mutableSetOf<ImportName>()

        if (isActual) {
            spec.addModifiers(KModifier.ACTUAL)
        }

        val serverTypeName = descriptor.writeServerTo(spec, platformTypeName)
        val clientTypeName = descriptor.writeClientTo(spec, platformTypeName)

        spec.addSuperinterface(
            GrpcService::class.typeName.parameterizedBy(
                descriptor.interfaceTypeName,
                clientTypeName,
            )
        )

        return listOf(spec.build()) to imports.toSet()
    }

    private fun Descriptors.ServiceDescriptor.writeServerTo(
        spec: TypeSpec.Builder,
        platformTypeName: ClassName
    ): TypeName {
        val name = grpcServerTypeName
        val serverSpec = TypeSpec.classBuilder(name)
            .addModifiers(KModifier.ABSTRACT)

        if (isActual) {
            serverSpec.addModifiers(KModifier.ACTUAL)
        }

        serverSpec.superclass(platformTypeName.nestedClass("Server"))
        serverSpec.addSuperclassConstructorParameter("coroutineContext")

        serverSpec.addSuperinterface(interfaceTypeName)

        serverSpec.primaryConstructor(
            FunSpec.constructorBuilder()
                .apply { if (isActual) addModifiers(KModifier.ACTUAL) }
                .addParameter(
                    ParameterSpec.builder("coroutineContext", CoroutineContext::class)
                        .apply { if (!isActual) defaultValue("%T", EmptyCoroutineContext::class) }
                        .build(),
                )
                .build(),
        )

        spec.addType(serverSpec.build())

        return name
    }

    private fun Descriptors.ServiceDescriptor.writeClientTo(
        spec: TypeSpec.Builder,
        platformTypeName: ClassName
    ): TypeName {
        val name = grpcClientTypeName
        val clientSpec = TypeSpec.classBuilder(name)
            .addModifiers(KModifier.OPEN)
            .apply { if (isActual) addModifiers(KModifier.ACTUAL) }

        clientSpec.superclass(platformTypeName.nestedClass("Client"))
        clientSpec.addSuperclassConstructorParameter("option")

        clientSpec.addSuperinterface(interfaceTypeName)

        clientSpec.primaryConstructor(
            FunSpec.constructorBuilder()
                .apply { if (isActual) addModifiers(KModifier.ACTUAL) }
                .addParameter("option", ClientOption::class)
                .build()
        )

        spec.addType(clientSpec.build())

        FunSpec.builder("createClient")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("option", ClientOption::class)
            .returns(name)
            .addStatement("return %T(option)", name)
            .build()
            .let(spec::addFunction)

        return name
    }
}