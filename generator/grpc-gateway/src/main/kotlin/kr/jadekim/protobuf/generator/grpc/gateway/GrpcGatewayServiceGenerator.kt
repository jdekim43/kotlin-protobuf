package kr.jadekim.protobuf.generator.grpc.gateway

import com.google.api.AnnotationsProto
import com.google.api.HttpRule
import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.ktor.client.*
import io.ktor.http.*
import kr.jadekim.protobuf.generator.ImportName
import kr.jadekim.protobuf.generator.grpc.gateway.util.extension.grpcGatewayClientTypeName
import kr.jadekim.protobuf.generator.grpc.gateway.util.extension.grpcGatewayTypeName
import kr.jadekim.protobuf.generator.grpc.gateway.util.extension.interfaceTypeName
import kr.jadekim.protobuf.generator.util.ProtobufWordSplitter
import kr.jadekim.protobuf.generator.util.extention.*
import kr.jadekim.protobuf.grpc.gateway.GrpcGatewayService
import net.pearx.kasechange.toCamelCase

sealed class HttpRequestMeta(val method: HttpMethod, val path: String, val hasBody: Boolean) {
    class GET(path: String) : HttpRequestMeta(HttpMethod.Get, path, false)
    class PUT(path: String) : HttpRequestMeta(HttpMethod.Put, path, true)
    class POST(path: String) : HttpRequestMeta(HttpMethod.Post, path, true)
    class DELETE(path: String) : HttpRequestMeta(HttpMethod.Delete, path, false)
    class PATCH(path: String) : HttpRequestMeta(HttpMethod.Patch, path, true)
    class Custom(method: HttpMethod, path: String, hasBody: Boolean) : HttpRequestMeta(method, path, hasBody)

    val memberName = MemberName("io.ktor.client.request", method.value.lowercase())
}

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
        val requestMeta = rule.toHttpRequestMeta()
            ?: return notSupport("Not supported method (${rule.patternCase})")

        val inputFields = inputType.flattenFields()
        val (path, pathParameterNames) = polishPath(requestMeta.path)
        val queryParameters = if (requestMeta.hasBody) emptyList() else {
            inputFields.filter { it.flattenName() !in pathParameterNames }
        }
        val bodyExcludedFields = pathParameterNames + queryParameters.flattenNames()

        functionSpec.appendVariables(pathParameterNames, bodyExcludedFields)
        functionSpec.addStatement("return http.%M {", requestMeta.memberName)
        functionSpec.appendUrlOption(path, queryParameters)

        if (requestMeta.hasBody) {
            functionSpec.appendBody()
        }

        functionSpec.appendAttributes()
        functionSpec.addStatement("}.%M()", MemberName("io.ktor.client.call", "body"))

        spec.addFunction(functionSpec.build())
    }

    private fun FunSpec.Builder.appendVariables(pathParameterNames: List<String>, bodyExcludedFields: List<String>) {
        for (parameterName in pathParameterNames) {
            addStatement(
                "val %N = request.%N",
                parameterName,
                parameterName.toCamelCase(ProtobufWordSplitter)
            )
        }

        addStatement(
            "val bodyExcludedFields = listOf<String>(%L)",
            bodyExcludedFields.joinToString(",") { "\"$it\"" },
        )
    }

    private fun FunSpec.Builder.appendUrlOption(
        path: String,
        queryParameters: List<List<Descriptors.FieldDescriptor>>
    ) {
        addStatement("\turl {")
        addStatement("\t\t%M(%P)", MemberName("io.ktor.http", "path"), path)

        for (parameter in queryParameters) {
            addStatement(
                "\t\t%M(%S, request.%L)",
                MemberName("io.ktor.client.request", "parameter"),
                parameter.flattenName(),
                parameter.flattenOutputTypeName(),
            )
        }

        addStatement("\t}")
    }

    private fun FunSpec.Builder.appendBody() {
        addStatement("%M(request)", MemberName("io.ktor.client.request", "setBody"))
    }

    private fun FunSpec.Builder.appendAttributes() {
        addStatement(
            "\tattributes.put(%M, bodyExcludedFields)",
            MemberName("kr.jadekim.protobuf.grpc.gateway.ktor", "BODY_EXCLUDE_FIELDS")
        )
    }

    private fun HttpRule.toHttpRequestMeta(): HttpRequestMeta? = when (patternCase) {
        HttpRule.PatternCase.GET -> HttpRequestMeta.GET(get)
        HttpRule.PatternCase.PUT -> HttpRequestMeta.PUT(put)
        HttpRule.PatternCase.POST -> HttpRequestMeta.POST(post)
        HttpRule.PatternCase.DELETE -> HttpRequestMeta.DELETE(delete)
        HttpRule.PatternCase.PATCH -> HttpRequestMeta.PATCH(patch)
        HttpRule.PatternCase.CUSTOM -> if (HttpMethod.parse(custom.kind) in HttpMethod.DefaultMethods) {
            HttpRequestMeta.Custom(HttpMethod.parse(custom.kind), custom.path, false)
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
