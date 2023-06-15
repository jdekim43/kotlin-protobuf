package kr.jadekim.protobuf.generator.grpc.platform

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kr.jadekim.protobuf.generator.ImportName
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
        val serviceName = descriptor.outputTypeName
        val spec = TypeSpec.objectBuilder(serviceName)
        val imports = mutableSetOf<ImportName>()

        if (isActual) {
            spec.addModifiers(KModifier.ACTUAL)
        }

        val interfaceTypeName = descriptor.writeInterfaceTo(spec)
        val serverTypeName = descriptor.writeServerTo(spec, platformTypeName)
        val clientTypeName = descriptor.writeClientTo(spec, platformTypeName)

        spec.addSuperinterface(
            GrpcService::class.typeName.parameterizedBy(
                interfaceTypeName,
                serverTypeName,
                clientTypeName,
            )
        )

        return listOf(spec.build()) to imports.toSet()
    }

    private fun Descriptors.ServiceDescriptor.writeInterfaceTo(spec: TypeSpec.Builder): TypeName {
        val name = interfaceTypeName
        val interfaceSpec = TypeSpec.interfaceBuilder(name)

        if (isActual) {
            interfaceSpec.addModifiers(KModifier.ACTUAL)
        }

        for (method in methods) {
            val functionName = method.name.toCamelCase(ProtobufWordSplitter)

            interfaceSpec.addFunction(
                FunSpec.builder(functionName)
                    .addModifiers(KModifier.SUSPEND, KModifier.ABSTRACT, KModifier.ACTUAL)
                    .addParameter("request", method.inputType.outputTypeName)
                    .returns(method.outputType.outputTypeName)
                    .build()
            )
        }

        spec.addType(interfaceSpec.build())

        return name
    }

    private fun Descriptors.ServiceDescriptor.writeServerTo(
        spec: TypeSpec.Builder,
        platformTypeName: ClassName
    ): TypeName {
        val name = outputTypeName.nestedClass("Server")
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
                .addModifiers(KModifier.ACTUAL)
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
        val name = outputTypeName.nestedClass("Client")
        val clientSpec = TypeSpec.classBuilder(name)
            .addModifiers(KModifier.OPEN, KModifier.ACTUAL)

        clientSpec.superclass(platformTypeName.nestedClass("Client"))
        clientSpec.addSuperclassConstructorParameter("option")

        clientSpec.addSuperinterface(interfaceTypeName)

        clientSpec.primaryConstructor(
            FunSpec.constructorBuilder()
                .addModifiers(KModifier.ACTUAL)
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