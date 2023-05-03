package kr.jadekim.protobuf.generator.converter.mapper

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.grpc.*
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.kotlin.ClientCalls
import io.grpc.kotlin.ServerCalls
import kr.jadekim.protobuf.generator.converter.mapper.util.extention.delegatorTypeName
import kr.jadekim.protobuf.generator.converter.mapper.util.extention.mapperTypeName
import kr.jadekim.protobuf.generator.type.TypeGenerator
import kr.jadekim.protobuf.generator.type.TypeGeneratorPlugins
import kr.jadekim.protobuf.generator.type.applyTo
import kr.jadekim.protobuf.generator.util.extention.outputTypeName
import kr.jadekim.protobuf.generator.util.extention.typeName
import net.pearx.kasechange.toCamelCase
import net.pearx.kasechange.toPascalCase
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class ServiceMapperGenerator(
    val plugins: TypeGeneratorPlugins<Descriptors.ServiceDescriptor> = emptyList(),
) : TypeGenerator<Descriptors.ServiceDescriptor> {

    private val Descriptors.MethodDescriptor.descriptorVariableName: String
        get() = name.toCamelCase() + "Descriptor"

    override fun generate(descriptor: Descriptors.ServiceDescriptor): Pair<TypeSpec, Set<Import>> {
        val name = descriptor.outputTypeName
        val spec = TypeSpec.objectBuilder(name)
        val imports = mutableSetOf<Import>()

        descriptor.writeGlobalVariablesTo(spec)
        descriptor.writeServerTo(spec)
        descriptor.writeClientTo(spec)

        plugins.applyTo(spec, imports, descriptor)

        return spec.build() to imports.toSet()
    }

    private fun Descriptors.ServiceDescriptor.writeGlobalVariablesTo(spec: TypeSpec.Builder) {
        val delegatorTypeName = delegatorTypeName

        spec.addProperty(
            PropertySpec.builder("descriptor", ServiceDescriptor::class, KModifier.PRIVATE)
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
                    KModifier.PRIVATE,
                )
                    .initializer("%T.get%LMethod()!!", delegatorTypeName, method.name.toPascalCase())
                    .build(),
            )
        }
    }

    private fun Descriptors.ServiceDescriptor.writeServerTo(spec: TypeSpec.Builder) {
        val serverTypeName = outputTypeName.nestedClass("Server")
        val serverSpec = TypeSpec.classBuilder(serverTypeName)
            .addModifiers(KModifier.ABSTRACT)
            .superclass(AbstractCoroutineServerImpl::class)
            .addSuperclassConstructorParameter("context = coroutineContext")
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
            val functionName = method.name.toCamelCase()
            serverSpec.addFunction(
                FunSpec.builder(functionName)
                    .addModifiers(KModifier.OPEN, KModifier.SUSPEND)
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
                .addCode("\t\t\t\tdescriptor = %L,\n", method.descriptorVariableName)
                .addCode(
                    "\t\t\t\timplementation = { %T.convert(%L(%T.convert(it))) },\n",
                    method.outputType.mapperTypeName,
                    functionName,
                    method.inputType.mapperTypeName,
                )
                .addCode("\t\t\t)\n\t\t)\n")
        }

        bindServiceFunction
            .addCode("\t\t.build()\n")

        serverSpec.addFunction(bindServiceFunction.build())

        spec.addType(serverSpec.build())
    }

    private fun Descriptors.ServiceDescriptor.writeClientTo(spec: TypeSpec.Builder) {
        val clientTypeName = outputTypeName.nestedClass("Client")
        val clientSpec = TypeSpec.classBuilder(clientTypeName)
            .superclass(AbstractCoroutineStub::class.typeName.parameterizedBy(clientTypeName))
            .addSuperclassConstructorParameter("channel = channel")
            .addSuperclassConstructorParameter("callOptions = callOptions")
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("channel", Channel::class)
                    .addParameter(
                        ParameterSpec.builder("callOptions", CallOptions::class)
                            .defaultValue("%T.DEFAULT", CallOptions::class)
                            .build()
                    )
                    .build()
            )
            .addFunction(
                FunSpec.builder("build")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("channel", Channel::class)
                    .addParameter("callOptions", CallOptions::class)
                    .returns(clientTypeName)
                    .addStatement("return %T(channel, callOptions)", clientTypeName)
                    .build()
            )

        for (method in methods) {
            val functionName = method.name.toCamelCase()

            clientSpec.addFunction(
                FunSpec.builder(functionName)
                    .addModifiers(KModifier.SUSPEND)
                    .addParameter("request", method.inputType.outputTypeName)
                    .addParameter(
                        ParameterSpec.builder("metadata", Metadata::class)
                            .defaultValue("%T()", Metadata::class)
                            .build()
                    )
                    .returns(method.outputType.outputTypeName)
                    .addCode("return %T.convert(\n", method.outputType.mapperTypeName)
                    .addCode("\t\t%T.unaryRpc(\n", ClientCalls::class)
                    .addCode("\t\t\tchannel, %L,\n", method.descriptorVariableName)
                    .addCode("\t\t\t%T.convert(request),\n", method.inputType.mapperTypeName)
                    .addCode("\t\t\tcallOptions, metadata,\n")
                    .addCode("\t\t),\n\t)\n")
                    .build()
            )
        }

        spec.addType(clientSpec.build())
    }
}