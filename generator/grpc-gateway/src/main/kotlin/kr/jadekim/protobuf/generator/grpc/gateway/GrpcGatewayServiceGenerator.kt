package kr.jadekim.protobuf.generator.grpc.gateway

import com.google.api.AnnotationsProto
import com.google.api.HttpRule
import com.google.protobuf.DescriptorProtos
import com.google.protobuf.DescriptorProtos.FileDescriptorProto
import com.google.protobuf.Descriptors
import com.google.protobuf.ExtensionRegistry
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.ktor.client.*
import io.ktor.http.*
import kr.jadekim.protobuf.generator.ImportName
import kr.jadekim.protobuf.generator.grpc.gateway.util.extension.grpcGatewayClientTypeName
import kr.jadekim.protobuf.generator.grpc.gateway.util.extension.grpcGatewayTypeName
import kr.jadekim.protobuf.generator.grpc.gateway.util.extension.interfaceTypeName
import kr.jadekim.protobuf.generator.util.ProtobufWordSplitter
import kr.jadekim.protobuf.generator.util.extention.outputTypeName
import kr.jadekim.protobuf.generator.util.extention.typeName
import kr.jadekim.protobuf.grpc.gateway.GrpcGatewayService
import net.pearx.kasechange.toCamelCase
import net.pearx.kasechange.toLowerSpaceCase
import java.io.FileInputStream

class GrpcGatewayServiceGenerator {

    fun generate(descriptor: Descriptors.ServiceDescriptor): Pair<List<TypeSpec>, Set<ImportName>> {
        val spec = TypeSpec.objectBuilder(descriptor.grpcGatewayTypeName)
        val imports = mutableSetOf<ImportName>()

        val clientTypeName = descriptor.writeClientTo(spec)

        spec.addSuperinterface(
            GrpcGatewayService::class.typeName.parameterizedBy(
                descriptor.interfaceTypeName,
                clientTypeName,
            )
        )

        return listOf(spec.build()) to imports.toSet()
    }

    private fun Descriptors.ServiceDescriptor.writeClientTo(
        spec: TypeSpec.Builder
    ): TypeName {
        val name = grpcGatewayClientTypeName
        val clientSpec = TypeSpec.classBuilder(name)
            .addModifiers(KModifier.OPEN)

        clientSpec.addSuperinterface(interfaceTypeName)

        clientSpec.primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("http", HttpClient::class)
                .build()
        )
        clientSpec.addProperty(
            PropertySpec.builder("http", HttpClient::class)
                .initializer("http")
                .addModifiers(KModifier.PRIVATE)
                .build()
        )

        for (methodDescriptor in methods) {
            methodDescriptor.writeEndpointTo(clientSpec)
        }

        spec.addType(clientSpec.build())

        FunSpec.builder("createClient")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("http", HttpClient::class)
            .returns(name)
            .addStatement("return %T(http)", name)
            .build()
            .let(spec::addFunction)

        return name
    }

    private fun Descriptors.MethodDescriptor.writeEndpointTo(spec: TypeSpec.Builder) {
        val functionName = name.toCamelCase(ProtobufWordSplitter)
        val functionSpec = FunSpec.builder(functionName)
            .addModifiers(KModifier.SUSPEND, KModifier.OVERRIDE)
            .addParameter("request", inputType.outputTypeName)
            .returns(outputType.outputTypeName)

        fun notSupport(message: String) {
            functionSpec.addStatement("throw %T(%S)", NotImplementedError::class, message)
            spec.addFunction(functionSpec.build())
        }

        val rule = options.getExtension(AnnotationsProto.http) ?: return notSupport("Not defined http option")
        val (methodName, pathRaw) = rule.toKtorMethodName()
            ?: return notSupport("Not supported method (${options.hasExtension(AnnotationsProto.http)} ${options} ${rule.patternCase})")

        val (path, pathParameterNames) = polishPath(pathRaw)
        functionSpec.addStatement(
            "val pathParameterNames = listOf<String>(%L)",
            pathParameterNames.joinToString(",") { "\"$it\"" },
        )
        for (parameterName in pathParameterNames) {
            functionSpec.addStatement("val %N = request.%N", parameterName, parameterName.toCamelCase(ProtobufWordSplitter))
        }

        functionSpec.addStatement("return http.%M {", MemberName("io.ktor.client.request", methodName))
        functionSpec.addStatement("\turl {")
        functionSpec.addStatement("\t\t%M(%P)", MemberName("io.ktor.http", "path"), path)
        functionSpec.addStatement("\t}")

        functionSpec.addStatement(
            "\tattributes.put(%M, pathParameterNames)",
            MemberName("kr.jadekim.protobuf.grpc.gateway", "PATH_PARAMETER_NAMES")
        )
        functionSpec.addStatement("}.%M()", MemberName("io.ktor.client.call", "body"))

        spec.addFunction(functionSpec.build())
    }

    private fun HttpRule.toKtorMethodName(): Pair<String, String>? = when (patternCase) {
        HttpRule.PatternCase.GET -> "get" to get
        HttpRule.PatternCase.PUT -> "put" to put
        HttpRule.PatternCase.POST -> "post" to post
        HttpRule.PatternCase.DELETE -> "delete" to delete
        HttpRule.PatternCase.PATCH -> "patch" to patch
        HttpRule.PatternCase.CUSTOM -> if (HttpMethod.parse(custom.kind) in HttpMethod.DefaultMethods) {
            custom.kind.toLowerSpaceCase() to custom.path
        } else null

        HttpRule.PatternCase.PATTERN_NOT_SET, null -> null
    }

    private val pathParameterRegex = """\{(.+?)}""".toRegex()

    private fun polishPath(pathRaw: String): Pair<String, List<String>> {
        var path = pathRaw
        val parameterNames = mutableListOf<String>()

        for (parameter in pathParameterRegex.findAll(pathRaw)) {
            if (parameter.groupValues.size < 2) continue
            var parameterName = parameter.groupValues[1]
            val delimiterIndex = parameterName.indexOf('=')
            if (delimiterIndex > 0) {
                parameterName = parameterName.substring(0, delimiterIndex)
            }

            path = path.replace(parameter.value, "\${$parameterName}")
            parameterNames += parameterName
        }

        return path to parameterNames
    }
}

fun loadProto(path: String, extensionRegistry: ExtensionRegistry = ExtensionRegistry.getEmptyRegistry()): FileDescriptorProto = FileInputStream(path).use {
    DescriptorProtos.FileDescriptorSet.parseFrom(it).fileList.first()
}

fun main() {
//    val httpProto = loadProto("/Users/jade/Work/my/kotlinx-protobuf/example/build/extracted-include-protos/main/google/api/http.proto")
//    val descriptorProto = loadProto("/Users/jade/Work/my/kotlinx-protobuf/example/build/extracted-include-protos/main/google/protobuf/descriptor.proto")
//    val annotationProto = loadProto("/Users/jade/Work/my/kotlinx-protobuf/example/build/extracted-include-protos/main/google/api/annotations.proto")

//    val httpDescriptor = Descriptors.FileDescriptor.buildFrom(httpProto, emptyArray())
//    val descriptorDescriptor = Descriptors.FileDescriptor.buildFrom(descriptorProto, emptyArray())
//    val annotationDescriptor = Descriptors.FileDescriptor.buildFrom(annotationProto, arrayOf(httpDescriptor, descriptorDescriptor))

//    val httpExtensionField = annotationDescriptor.findExtensionByName("http")
//    val extensionRegistry = ExtensionRegistry.newInstance().apply {
//        add(httpExtensionField)
//    }

    val testProto = loadProto("/Users/jade/Work/my/kotlinx-protobuf/example/src/proto/protobuf/example/test.proto")
//    val testDescriptor = Descriptors.FileDescriptor.buildFrom(testProto, arrayOf(annotationDescriptor))


//    val method = testDescriptor.services.find { it.name == "Messaging" }
//        ?.findMethodByName("GetMessage")

//    if (method == null) {
//        println("Not found")
//        return
//    }

//    println(method.name)
//    println(method.options.unknownFields.asMap())
//    println(method.options.hasField(httpExtensionField))

    println("do")
}
