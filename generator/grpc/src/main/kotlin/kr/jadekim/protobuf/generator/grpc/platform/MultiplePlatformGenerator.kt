package kr.jadekim.protobuf.generator.grpc.platform

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import kr.jadekim.protobuf.generator.ImportName
import kr.jadekim.protobuf.generator.grpc.util.extension.interfaceTypeName
import kr.jadekim.protobuf.generator.util.ProtobufWordSplitter
import kr.jadekim.protobuf.generator.util.extention.outputTypeName
import kr.jadekim.protobuf.grpc.ClientOption
import net.pearx.kasechange.toCamelCase
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


//TODO: Remove duplicated codes with SinglePlatformGenerator
class MultiplePlatformGenerator : PlatformGrpcGenerator {

    override fun generate(descriptor: Descriptors.ServiceDescriptor): Pair<List<TypeSpec>, Set<ImportName>> {
        val serviceName = descriptor.outputTypeName
        val spec = TypeSpec.objectBuilder(serviceName)
        val imports = mutableSetOf<ImportName>()

        spec.addModifiers(KModifier.EXPECT)

        descriptor.writeInterfaceTo(spec)
        descriptor.writeServerTo(spec)
        descriptor.writeClientTo(spec)

        return listOf(spec.build()) to imports.toSet()
    }

    private fun Descriptors.ServiceDescriptor.writeInterfaceTo(spec: TypeSpec.Builder) {
        val name = interfaceTypeName
        val interfaceSpec = TypeSpec.interfaceBuilder(name)

        for (method in methods) {
            val functionName = method.name.toCamelCase(ProtobufWordSplitter)

            interfaceSpec.addFunction(
                FunSpec.builder(functionName)
                    .addModifiers(KModifier.SUSPEND, KModifier.ABSTRACT)
                    .addParameter("request", method.inputType.outputTypeName)
                    .returns(method.outputType.outputTypeName)
                    .build()
            )
        }

        spec.addType(interfaceSpec.build())
    }

    private fun Descriptors.ServiceDescriptor.writeServerTo(spec: TypeSpec.Builder) {
        val name = outputTypeName.nestedClass("Server")
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
    }

    private fun Descriptors.ServiceDescriptor.writeClientTo(spec: TypeSpec.Builder) {
        val name = outputTypeName.nestedClass("Client")
        val clientSpec = TypeSpec.classBuilder(name)
            .addModifiers(KModifier.OPEN)

        clientSpec.addSuperinterface(interfaceTypeName)

        clientSpec.primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("option", ClientOption::class)
                .build()
        )

        spec.addType(clientSpec.build())
    }
}