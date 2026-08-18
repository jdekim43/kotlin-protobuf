package kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.js.mapper

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kim.jade.kotlinx.protobuf.generator.ImportName
import kim.jade.kotlinx.protobuf.generator.converter.util.extention.converterTypeName
import kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.js.util.extension.*
import kim.jade.kotlinx.protobuf.generator.grpc.util.extension.grpcClientTypeName
import kim.jade.kotlinx.protobuf.generator.grpc.util.extension.grpcServerTypeName
import kim.jade.kotlinx.protobuf.generator.grpc.util.extension.interfaceTypeName
import kim.jade.kotlinx.protobuf.generator.type.TypeGenerator
import kim.jade.kotlinx.protobuf.generator.type.TypeGeneratorPlugins
import kim.jade.kotlinx.protobuf.generator.type.applyTo
import kim.jade.kotlinx.protobuf.generator.util.ProtobufWordSplitter
import kim.jade.kotlinx.protobuf.generator.util.extention.functionName
import kim.jade.kotlinx.protobuf.generator.util.extention.functionSpecBuilder
import kim.jade.kotlinx.protobuf.generator.util.extention.outputTypeName
import kim.jade.kotlinx.protobuf.generator.util.extention.requestParameterName
import kim.jade.kotlinx.protobuf.grpc.GrpcClientOption
import net.pearx.kasechange.toCamelCase
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class ServiceJsMapperGenerator(
    val plugins: TypeGeneratorPlugins<Descriptors.ServiceDescriptor> = emptyList(),
) : TypeGenerator<Descriptors.ServiceDescriptor> {

    private val Descriptors.MethodDescriptor.methodVariableName: String
        get() = name.toCamelCase(ProtobufWordSplitter) + "Method"

    override fun generate(descriptor: Descriptors.ServiceDescriptor): Pair<TypeSpec, Set<ImportName>> {
        val spec = TypeSpec.objectBuilder(descriptor.jsGrpcTypeName)
        val imports = mutableSetOf<ImportName>()

        descriptor.writeMethodsTo(spec)
        descriptor.writeServerTo(spec)
        descriptor.writeClientTo(spec)

        plugins.applyTo(spec, imports, descriptor)

        return spec.build() to imports.toSet()
    }

    private fun Descriptors.ServiceDescriptor.writeMethodsTo(spec: TypeSpec.Builder) {
        for (method in methods) {
            spec.addProperty(
                PropertySpec.builder(
                    method.methodVariableName,
                    JsGrpcRuntime.GRPC_METHOD.parameterizedBy(
                        method.inputType.outputTypeName,
                        method.outputType.outputTypeName,
                    ),
                )
                    .initializer(
                        CodeBlock.builder()
                            .add("%T(\n", JsGrpcRuntime.GRPC_METHOD)
                            .indent()
                            .add("path = %S,\n", "/${fullName}/${method.name}")
                            .add("requestConverter = %T,\n", method.inputType.converterTypeName)
                            .add("responseConverter = %T,\n", method.outputType.converterTypeName)
                            .add("requestStream = %L,\n", method.isClientStreaming)
                            .add("responseStream = %L,\n", method.isServerStreaming)
                            .unindent()
                            .add(")")
                            .build()
                    )
                    .build()
            )
        }
    }

    private fun Descriptors.ServiceDescriptor.writeServerTo(spec: TypeSpec.Builder) {
        val serverSpec = TypeSpec.classBuilder(grpcServerTypeName)
            .addModifiers(KModifier.ABSTRACT)
            .addSuperinterface(interfaceTypeName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(
                        ParameterSpec.builder("coroutineContext", CoroutineContext::class)
                            .defaultValue("%T", EmptyCoroutineContext::class)
                            .build()
                    )
                    .build()
            )
            .addProperty(
                PropertySpec.builder("coroutineContext", CoroutineContext::class)
                    .initializer("coroutineContext")
                    .build()
            )

        val bindServiceFunction = FunSpec.builder("bindService")
            .returns(JsGrpcRuntime.GRPC_SERVICE_BINDING)
            .addCode("return %M(coroutineContext) {\n", JsGrpcRuntime.GRPC_SERVICE)

        for (method in methods) {
            serverSpec.addFunction(
                method.functionSpecBuilder(KModifier.OPEN, KModifier.OVERRIDE)
                    .addStatement(
                        "throw %T(%T.UNIMPLEMENTED, %S)",
                        JsGrpcRuntime.GRPC_STATUS_EXCEPTION,
                        JsGrpcRuntime.GRPC_STATUS,
                        "Method ${method.fullName} is unimplemented",
                    )
                    .build()
            )

            bindServiceFunction.addCode(
                "\t%N(%S, %N) { %N(it) }\n",
                method.serviceBuilderFunctionName,
                method.functionName,
                method.methodVariableName,
                method.functionName,
            )
        }

        bindServiceFunction.addCode("}\n")
        serverSpec.addFunction(bindServiceFunction.build())

        spec.addType(serverSpec.build())
    }

    private fun Descriptors.ServiceDescriptor.writeClientTo(spec: TypeSpec.Builder) {
        val clientSpec = TypeSpec.classBuilder(grpcClientTypeName)
            .addModifiers(KModifier.OPEN)
            .addSuperinterface(interfaceTypeName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("option", GrpcClientOption::class)
                    .build()
            )
            .addProperty(
                PropertySpec.builder("option", GrpcClientOption::class)
                    .addModifiers(KModifier.PRIVATE)
                    .initializer("option")
                    .build()
            )

        for (method in methods) {
            clientSpec.addFunction(
                method.functionSpecBuilder(KModifier.OVERRIDE)
                    .addStatement(
                        "return option.%M(%N, %N)",
                        method.clientCallMemberName,
                        method.methodVariableName,
                        method.requestParameterName,
                    )
                    .build()
            )
        }

        spec.addType(clientSpec.build())
    }
}
