package kim.jade.kotlinx.protobuf.generator.grpc.jvm.mapper

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.grpc.*
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.kotlin.ClientCalls
import io.grpc.kotlin.ServerCalls
import kim.jade.kotlinx.protobuf.generator.ImportName
import kim.jade.kotlinx.protobuf.generator.converter.jvm.mapper.util.extention.delegatorTypeName
import kim.jade.kotlinx.protobuf.generator.converter.util.extention.converterTypeName
import kim.jade.kotlinx.protobuf.generator.grpc.jvm.util.extension.jvmGrpcTypeName
import kim.jade.kotlinx.protobuf.generator.grpc.util.extension.grpcClientTypeName
import kim.jade.kotlinx.protobuf.generator.grpc.util.extension.grpcServerTypeName
import kim.jade.kotlinx.protobuf.generator.grpc.util.extension.interfaceTypeName
import kim.jade.kotlinx.protobuf.generator.type.TypeGenerator
import kim.jade.kotlinx.protobuf.generator.type.TypeGeneratorPlugins
import kim.jade.kotlinx.protobuf.generator.type.applyTo
import kim.jade.kotlinx.protobuf.generator.util.ProtobufWordSplitter
import kim.jade.kotlinx.protobuf.generator.util.extention.functionName
import kim.jade.kotlinx.protobuf.generator.util.extention.functionSpecBuilder
import kim.jade.kotlinx.protobuf.generator.util.extention.requestParameterName
import kim.jade.kotlinx.protobuf.generator.util.extention.typeName
import kim.jade.kotlinx.protobuf.grpc.GrpcClientOption
import net.pearx.kasechange.toCamelCase
import net.pearx.kasechange.toPascalCase
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

private val FLOW_MAP = MemberName("kotlinx.coroutines.flow", "map")

class ServiceMapperGenerator(
    val plugins: TypeGeneratorPlugins<Descriptors.ServiceDescriptor> = emptyList(),
) : TypeGenerator<Descriptors.ServiceDescriptor> {

    private val Descriptors.MethodDescriptor.descriptorVariableName: String
        get() = name.toCamelCase(ProtobufWordSplitter) + "Descriptor"

    private val Descriptors.MethodDescriptor.serverMethodDefinitionName: String
        get() = when {
            isClientStreaming && isServerStreaming -> "bidiStreamingServerMethodDefinition"
            isClientStreaming -> "clientStreamingServerMethodDefinition"
            isServerStreaming -> "serverStreamingServerMethodDefinition"
            else -> "unaryServerMethodDefinition"
        }

    private val Descriptors.MethodDescriptor.clientRpcName: String
        get() = when {
            isClientStreaming && isServerStreaming -> "bidiStreamingRpc"
            isClientStreaming -> "clientStreamingRpc"
            isServerStreaming -> "serverStreamingRpc"
            else -> "unaryRpc"
        }

    private fun Descriptors.MethodDescriptor.convertRequest(source: String): CodeBlock =
        if (isClientStreaming) {
            CodeBlock.of("%N.%M { %T.convert(it) }", source, FLOW_MAP, inputType.converterTypeName)
        } else {
            CodeBlock.of("%T.convert(%N)", inputType.converterTypeName, source)
        }

    private fun Descriptors.MethodDescriptor.convertResponse(source: CodeBlock): CodeBlock =
        if (isServerStreaming) {
            CodeBlock.of("%L.%M { %T.convert(it) }", source, FLOW_MAP, outputType.converterTypeName)
        } else {
            CodeBlock.of("%T.convert(%L)", outputType.converterTypeName, source)
        }

    override fun generate(descriptor: Descriptors.ServiceDescriptor): Pair<TypeSpec, Set<ImportName>> {
        val name = descriptor.jvmGrpcTypeName
        val spec = TypeSpec.objectBuilder(name)
        val imports = mutableSetOf<ImportName>()

        descriptor.writeGlobalVariablesTo(spec)
        descriptor.writeServerTo(spec)
        descriptor.writeClientTo(spec)

        plugins.applyTo(spec, imports, descriptor)

        return spec.build() to imports.toSet()
    }

    private fun Descriptors.ServiceDescriptor.writeGlobalVariablesTo(spec: TypeSpec.Builder) {
        val delegatorTypeName = delegatorTypeName

        spec.addProperty(
            PropertySpec.builder("descriptor", ServiceDescriptor::class)
                .initializer("%T.getServiceDescriptor()!!", delegatorTypeName)
                .build(),
        )

        for (method in methods) {
            spec.addProperty(
                PropertySpec.builder(
                    method.descriptorVariableName,
                    MethodDescriptor::class.typeName.parameterizedBy(
                        method.inputType.delegatorTypeName,
                        method.outputType.delegatorTypeName,
                    ),
                )
                    .initializer(
                        "%T.get%LMethod()!!",
                        delegatorTypeName,
                        method.name.toPascalCase(ProtobufWordSplitter)
                    )
                    .build(),
            )
        }
    }

    private fun Descriptors.ServiceDescriptor.writeServerTo(spec: TypeSpec.Builder) {
        val serverTypeName = grpcServerTypeName
        val serverSpec = TypeSpec.classBuilder(serverTypeName)
            .addModifiers(KModifier.ABSTRACT)
            .superclass(AbstractCoroutineServerImpl::class)
            .addSuperclassConstructorParameter("context = coroutineContext")
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

        val bindServiceFunction = FunSpec.builder("bindService")
            .addModifiers(KModifier.OVERRIDE)
            .returns(ServerServiceDefinition::class)
            .addCode("return %T.builder(descriptor)\n", ServerServiceDefinition::class)

        for (method in methods) {
            serverSpec.addFunction(
                method.functionSpecBuilder(KModifier.OPEN, KModifier.OVERRIDE)
                    .addStatement(
                        "throw %T(%T.UNIMPLEMENTED.withDescription(%S))",
                        StatusException::class,
                        Status::class,
                        "Method ${method.fullName} is unimplemented",
                    )
                    .build()
            )

            val implementation = method.convertResponse(
                CodeBlock.of(
                    "%N(%L)",
                    method.functionName,
                    method.convertRequest(method.requestParameterName),
                )
            )

            bindServiceFunction.addCode("\t\t.addMethod(\n")
                .addCode("\t\t\t%T.%N(\n", ServerCalls::class, method.serverMethodDefinitionName)
                .addCode("\t\t\t\tcontext = this.context,\n")
                .addCode("\t\t\t\tdescriptor = %N,\n", method.descriptorVariableName)
                .addCode(
                    "\t\t\t\timplementation = { %N -> %L },\n",
                    method.requestParameterName,
                    implementation,
                )
                .addCode("\t\t\t)\n\t\t)\n")
        }

        bindServiceFunction
            .addCode("\t\t.build()\n")

        serverSpec.addFunction(bindServiceFunction.build())

        spec.addType(serverSpec.build())
    }

    private fun Descriptors.ServiceDescriptor.writeClientTo(spec: TypeSpec.Builder) {
        val clientTypeName = grpcClientTypeName
        val clientSpec = TypeSpec.classBuilder(clientTypeName)
            .addModifiers(KModifier.OPEN)
            .superclass(AbstractCoroutineStub::class.typeName.parameterizedBy(clientTypeName))
            .addSuperclassConstructorParameter("channel = option.channel")
            .addSuperclassConstructorParameter("callOptions = option.callOptions")
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
            .addFunction(
                FunSpec.builder("build")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("channel", Channel::class)
                    .addParameter("callOptions", CallOptions::class)
                    .returns(clientTypeName)
                    .addStatement("return %T(%T(channel, callOptions))", clientTypeName, GrpcClientOption::class)
                    .build()
            )

        for (method in methods) {
            val parameterName = method.requestParameterName

            clientSpec.addFunction(
                method.functionSpecBuilder(KModifier.OVERRIDE)
                    .addStatement(
                        "return %N(%N, %T())",
                        method.functionName,
                        parameterName,
                        Metadata::class,
                    )
                    .build()
            )

            val call = CodeBlock.of(
                "%T.%N(\n\t\t\toption.channel, %N,\n\t\t\t%L,\n\t\t\toption.callOptions, metadata,\n\t\t)",
                ClientCalls::class,
                method.clientRpcName,
                method.descriptorVariableName,
                method.convertRequest(parameterName),
            )

            clientSpec.addFunction(
                method.functionSpecBuilder()
                    .addParameter("metadata", Metadata::class)
                    .addCode("return %L\n", method.convertResponse(call))
                    .build()
            )
        }

        spec.addType(clientSpec.build())
    }
}