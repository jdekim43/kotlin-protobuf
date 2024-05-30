package kr.jadekim.protobuf.generator.grpc.jvm.mapper

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.grpc.*
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.kotlin.ClientCalls
import io.grpc.kotlin.ServerCalls
import kr.jadekim.protobuf.generator.ImportName
import kr.jadekim.protobuf.generator.converter.jvm.mapper.util.extention.delegatorTypeName
import kr.jadekim.protobuf.generator.converter.util.extention.converterTypeName
import kr.jadekim.protobuf.generator.grpc.jvm.util.extension.jvmGrpcTypeName
import kr.jadekim.protobuf.generator.grpc.util.extension.grpcClientTypeName
import kr.jadekim.protobuf.generator.grpc.util.extension.grpcServerTypeName
import kr.jadekim.protobuf.generator.grpc.util.extension.interfaceTypeName
import kr.jadekim.protobuf.generator.type.TypeGenerator
import kr.jadekim.protobuf.generator.type.TypeGeneratorPlugins
import kr.jadekim.protobuf.generator.type.applyTo
import kr.jadekim.protobuf.generator.util.ProtobufWordSplitter
import kr.jadekim.protobuf.generator.util.extention.outputTypeName
import kr.jadekim.protobuf.generator.util.extention.typeName
import kr.jadekim.protobuf.grpc.GrpcClientOption
import net.pearx.kasechange.toCamelCase
import net.pearx.kasechange.toPascalCase
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class ServiceMapperGenerator(
    val plugins: TypeGeneratorPlugins<Descriptors.ServiceDescriptor> = emptyList(),
) : TypeGenerator<Descriptors.ServiceDescriptor> {

    private val Descriptors.MethodDescriptor.descriptorVariableName: String
        get() = name.toCamelCase(ProtobufWordSplitter) + "Descriptor"

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
            val functionName = method.name.toCamelCase(ProtobufWordSplitter)
            serverSpec.addFunction(
                FunSpec.builder(functionName)
                    .addModifiers(KModifier.OPEN, KModifier.SUSPEND, KModifier.OVERRIDE)
                    .addParameter("request", method.inputType.outputTypeName)
                    .returns(method.outputType.outputTypeName)
                    .addStatement(
                        "throw %T(%T.UNIMPLEMENTED.withDescription(%S))",
                        StatusException::class,
                        Status::class,
                        "Method ${method.fullName} is unimplemented",
                    )
                    .build()
            )

            bindServiceFunction.addCode("\t\t.addMethod(\n")
                .addCode("\t\t\t%T.unaryServerMethodDefinition(\n", ServerCalls::class)
                .addCode("\t\t\t\tcontext = this.context,\n")
                .addCode("\t\t\t\tdescriptor = %N,\n", method.descriptorVariableName)
                .addCode(
                    "\t\t\t\timplementation = { %T.convert(%N(%T.convert(it))) },\n",
                    method.outputType.converterTypeName,
                    functionName,
                    method.inputType.converterTypeName,
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
            val functionName = method.name.toCamelCase(ProtobufWordSplitter)

            clientSpec.addFunction(
                FunSpec.builder(functionName)
                    .addModifiers(KModifier.SUSPEND, KModifier.OVERRIDE)
                    .addParameter("request", method.inputType.outputTypeName)
                    .returns(method.outputType.outputTypeName)
                    .addStatement("return %N(request, %T())", functionName, Metadata::class)
                    .build()
            )

            clientSpec.addFunction(
                FunSpec.builder(functionName)
                    .addModifiers(KModifier.SUSPEND)
                    .addParameter("request", method.inputType.outputTypeName)
                    .addParameter("metadata", Metadata::class)
                    .returns(method.outputType.outputTypeName)
                    .addCode("return %T.convert(\n", method.outputType.converterTypeName)
                    .addCode("\t\t%T.unaryRpc(\n", ClientCalls::class)
                    .addCode("\t\t\toption.channel, %N,\n", method.descriptorVariableName)
                    .addCode("\t\t\t%T.convert(request),\n", method.inputType.converterTypeName)
                    .addCode("\t\t\toption.callOptions, metadata,\n")
                    .addCode("\t\t),\n\t)\n")
                    .build()
            )
        }

        spec.addType(clientSpec.build())
    }
}