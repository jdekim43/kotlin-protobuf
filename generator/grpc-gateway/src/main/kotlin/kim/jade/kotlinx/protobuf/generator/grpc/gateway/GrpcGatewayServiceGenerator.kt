package kim.jade.kotlinx.protobuf.generator.grpc.gateway

import com.google.api.AnnotationsProto
import com.google.api.HttpRule
import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.ktor.client.*
import io.ktor.http.*
import kim.jade.kotlinx.protobuf.generator.ImportName
import kim.jade.kotlinx.protobuf.generator.grpc.gateway.util.extension.grpcGatewayClientTypeName
import kim.jade.kotlinx.protobuf.generator.grpc.gateway.util.extension.grpcGatewayTypeName
import kim.jade.kotlinx.protobuf.generator.grpc.gateway.util.extension.interfaceTypeName
import kim.jade.kotlinx.protobuf.generator.util.extention.*
import kim.jade.kotlinx.protobuf.grpc.gateway.GrpcGatewayClientOption
import kim.jade.kotlinx.protobuf.grpc.gateway.GrpcGatewayServiceFactory

private const val WHOLE_MESSAGE = "*"

private val ENCODE_URL_PATH_PART = MemberName("io.ktor.http", "encodeURLPathPart", true)
private val ENCODE_URL_PATH = MemberName("io.ktor.http", "encodeURLPath", true)
private val KTOR_PATH = MemberName("io.ktor.http", "path")
private val KTOR_PARAMETER = MemberName("io.ktor.client.request", "parameter")
private val KTOR_SET_BODY = MemberName("io.ktor.client.request", "setBody")
private val KTOR_BODY = MemberName("io.ktor.client.call", "body")
private val BODY_EXCLUDE_FIELDS = MemberName("kim.jade.kotlinx.protobuf.grpc.gateway.ktor", "BODY_EXCLUDE_FIELDS")

private class HttpBinding(
    val method: HttpMethod,
    val template: String,
    val body: String,
    val responseBody: String,
) {

    val memberName = MemberName("io.ktor.client.request", method.value.lowercase())
}

class GrpcGatewayServiceGenerator {

    fun generate(descriptor: Descriptors.ServiceDescriptor): Pair<List<TypeSpec>, Set<ImportName>> {
        val spec = TypeSpec.objectBuilder(descriptor.grpcGatewayTypeName)
        val imports = mutableSetOf<ImportName>()

        val clientTypeName = descriptor.writeClientTo(spec, imports)

        spec.addSuperinterface(
            GrpcGatewayServiceFactory::class.typeName.parameterizedBy(
                descriptor.interfaceTypeName,
                clientTypeName,
            )
        )

        return listOf(spec.build()) to imports.toSet()
    }

    private fun Descriptors.ServiceDescriptor.writeClientTo(
        spec: TypeSpec.Builder,
        imports: MutableSet<ImportName>,
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
            methodDescriptor.writeEndpointsTo(clientSpec, imports)
        }

        spec.addType(clientSpec.build())

        FunSpec.builder("createClient")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("option", GrpcGatewayClientOption::class)
            .returns(name)
            .addStatement("return %T(option.httpClient)", name)
            .build()
            .let(spec::addFunction)

        return name
    }

    private fun Descriptors.MethodDescriptor.writeEndpointsTo(
        spec: TypeSpec.Builder,
        imports: MutableSet<ImportName>,
    ) {
        fun notSupported(message: String) {
            val builder = functionSpecBuilder(KModifier.OVERRIDE)
                .addStatement("throw %T(%S)", NotImplementedError::class, message)

            builder.addDeprecatedAnnotation(message, level = DeprecationLevel.ERROR)

            spec.addFunction(builder.build())
        }

        if (isClientStreaming || isServerStreaming) {
            return notSupported("Streaming RPCs are not supported by the grpc-gateway client ($fullName)")
        }

        val rule = options.getExtension(AnnotationsProto.http)
            ?: return notSupported("Not defined http option ($fullName)")

        val bindings = (listOf(rule) + rule.additionalBindingsList).map { it.toHttpBinding() }

        val primary = bindings.first()
            ?: return notSupported("Not supported method (${rule.patternCase}) for $fullName")

        spec.addFunction(
            functionSpecBuilder(KModifier.OVERRIDE)
                .writeEndpoint(this, primary, imports)
                .build()
        )

        bindings.drop(1).forEachIndexed { index, binding ->
            if (binding == null) {
                return@forEachIndexed
            }

            val builder = FunSpec.builder("${functionName}Binding${index + 2}")
                .addModifiers(KModifier.SUSPEND)
                .addKdoc(
                    "Additional binding %L of [%N]: `%L %L`.",
                    index + 2,
                    functionName,
                    binding.method.value,
                    binding.template,
                )
                .addParameter(requestParameterName, requestTypeName)
                .returns(responseTypeName)

            spec.addFunction(builder.writeEndpoint(this, binding, imports).build())
        }
    }

    private fun FunSpec.Builder.writeEndpoint(
        method: Descriptors.MethodDescriptor,
        binding: HttpBinding,
        imports: MutableSet<ImportName>,
    ): FunSpec.Builder {
        val path = method.inputType.resolvePathTemplate(binding.template, method.fullName, imports)
        val bodyPath = binding.resolveBodyPath(method)
        val queryParameters = method.inputType.flattenFields().filter { field ->
            when {
                path.boundNames.any { field.isUnder(it) } -> false
                binding.body == WHOLE_MESSAGE -> false
                bodyPath != null && field.isUnder(binding.body) -> false
                else -> true
            }
        }

        val responseField = binding.resolveResponseBodyField(method)

        for (variable in path.variables) {
            addStatement("val %N = %L", variable.name, variable.source)
        }

        if (responseField == null) {
            addStatement("return http.%M {", binding.memberName)
        } else {
            addStatement("val responseBody = http.%M {", binding.memberName)
        }

        appendUrl(path, queryParameters, imports)
        binding.appendBody(this, bodyPath)
        appendBodyExcludedFields(binding, path)

        if (responseField == null) {
            addStatement("}.%M()", KTOR_BODY)
        } else {
            addStatement("}.%M<%T>()", KTOR_BODY, responseField.outputTypeName.copy(nullable = false))
            addStatement(
                "return %T(%N = responseBody)",
                method.outputType.outputTypeName,
                responseField.outputVariableNameString,
            )
        }

        return this
    }

    private fun FunSpec.Builder.appendUrl(
        path: ResolvedPath,
        queryParameters: List<List<Descriptors.FieldDescriptor>>,
        imports: MutableSet<ImportName>,
    ) {
        addStatement("\turl {")
        addStatement("\t\t%M(%P)", KTOR_PATH, path.expression)

        for (parameter in queryParameters) {
            val leaf = parameter.last()
            val (conversion, conversionImport) = leaf.stringConversionCall()
            conversionImport?.let { imports += ImportName(it) }

            val (accessor, nullable) = parameter.accessorExpression("request")

            if (leaf.isRepeated) {
                val elements = if (nullable) "$accessor.orEmpty()" else accessor
                addStatement(
                    "\t\t$elements.forEach { %M(%S, it.$conversion) }",
                    KTOR_PARAMETER,
                    parameter.flattenName(),
                )
            } else if (nullable) {
                addStatement(
                    "\t\t$accessor?.let { %M(%S, it.$conversion) }",
                    KTOR_PARAMETER,
                    parameter.flattenName(),
                )
            } else {
                addStatement(
                    "\t\t%M(%S, $accessor.$conversion)",
                    KTOR_PARAMETER,
                    parameter.flattenName(),
                )
            }
        }

        addStatement("\t}")
    }

    private fun HttpBinding.appendBody(spec: FunSpec.Builder, bodyPath: List<Descriptors.FieldDescriptor>?) {
        when {
            body.isEmpty() -> Unit

            body == WHOLE_MESSAGE -> spec.addStatement("\t%M(request)", KTOR_SET_BODY)

            bodyPath != null -> {
                val (accessor, nullable) = bodyPath.accessorExpression("request")

                if (nullable) {
                    spec.addStatement("\t$accessor?.let { %M(it) }", KTOR_SET_BODY)
                } else {
                    spec.addStatement("\t%M($accessor)", KTOR_SET_BODY)
                }
            }
        }
    }

    private fun FunSpec.Builder.appendBodyExcludedFields(binding: HttpBinding, path: ResolvedPath) {
        val excluded = if (binding.body == WHOLE_MESSAGE) path.boundJsonNames else emptyList()

        addStatement(
            "\tattributes.put(%M, listOf(%L))",
            BODY_EXCLUDE_FIELDS,
            excluded.joinToString(", ") { "\"$it\"" },
        )
    }

    private fun HttpBinding.resolveBodyPath(method: Descriptors.MethodDescriptor): List<Descriptors.FieldDescriptor>? {
        if (body.isEmpty() || body == WHOLE_MESSAGE) {
            return null
        }

        return method.inputType.resolveFieldPath(body)
            ?: throw IllegalStateException(
                "body: \"$body\" on ${method.fullName} names no field of ${method.inputType.fullName}"
            )
    }

    private fun HttpBinding.resolveResponseBodyField(method: Descriptors.MethodDescriptor): Descriptors.FieldDescriptor? {
        if (responseBody.isEmpty() || responseBody == WHOLE_MESSAGE) {
            return null
        }

        return method.outputType.findFieldByName(responseBody)
            ?: throw IllegalStateException(
                "response_body: \"$responseBody\" on ${method.fullName} names no field of " +
                    method.outputType.fullName
            )
    }

    private fun HttpRule.toHttpBinding(): HttpBinding? = when (patternCase) {
        HttpRule.PatternCase.GET -> HttpBinding(HttpMethod.Get, get, body, responseBody)
        HttpRule.PatternCase.PUT -> HttpBinding(HttpMethod.Put, put, body, responseBody)
        HttpRule.PatternCase.POST -> HttpBinding(HttpMethod.Post, post, body, responseBody)
        HttpRule.PatternCase.DELETE -> HttpBinding(HttpMethod.Delete, delete, body, responseBody)
        HttpRule.PatternCase.PATCH -> HttpBinding(HttpMethod.Patch, patch, body, responseBody)
        HttpRule.PatternCase.CUSTOM -> HttpMethod.parse(custom.kind)
            .takeIf { it in HttpMethod.DefaultMethods }
            ?.let { HttpBinding(it, custom.path, body, responseBody) }

        HttpRule.PatternCase.PATTERN_NOT_SET, null -> null
    }
}

private class ResolvedPath(
    val expression: String,
    val variables: List<PathVariable>,
    val boundNames: List<String>,
    val boundJsonNames: List<String>,
)

private class PathVariable(val name: String, val source: String)

private val PATH_VARIABLE = """\{([^{}]+)}""".toRegex()

/**
 * Turns a `google.api.http` path template into the Kotlin string that builds the URL.
 *
 * The grammar is
 *
 * ```
 * Template = "/" Segments [ Verb ] ;
 * Segment  = "*" | "**" | LITERAL | Variable ;
 * Variable = "{" FieldPath [ "=" Segments ] "}" ;
 * Verb     = ":" LITERAL ;
 * ```
 *
 * so everything outside a `{…}` — the literal segments and any `:verb` suffix — is already the path,
 * and only the variables have to be substituted. The sub-template after `=` decides the escaping rather
 * than the value: `{name=**}` is the one form allowed to span segments, so its value keeps its slashes
 * and everything else has them escaped. A template with no `=` means `*`, a single segment.
 */
private fun Descriptors.Descriptor.resolvePathTemplate(
    template: String,
    methodName: String,
    imports: MutableSet<ImportName>,
): ResolvedPath {
    val boundNames = mutableListOf<String>()
    val boundJsonNames = mutableListOf<String>()
    val variables = mutableListOf<PathVariable>()

    val escaped = template.replace("$", "\${'$'}")

    val expression = PATH_VARIABLE.replace(escaped) { match ->
        val declaration = match.groupValues[1]
        val separator = declaration.indexOf('=')
        val fieldPath = if (separator < 0) declaration else declaration.substring(0, separator)
        val subTemplate = if (separator < 0) "*" else declaration.substring(separator + 1)

        val fields = resolveFieldPath(fieldPath.trim())
            ?: throw IllegalStateException(
                "Path template \"$template\" on $methodName binds {$fieldPath}, which names no field of $fullName"
            )

        boundNames += fields.flattenName()
        boundJsonNames += fields.flattenJsonName()

        val encoder = if (subTemplate.contains("**")) ENCODE_URL_PATH else ENCODE_URL_PATH_PART
        imports += ImportName(encoder)

        val (conversion, conversionImport) = fields.last().stringConversionCall()
        conversionImport?.let { imports += ImportName(it) }

        val (accessor, nullable) = fields.accessorExpression("request")
        val name = "pathParameter${variables.size}"

        variables += PathVariable(
            name,
            if (nullable) "$accessor?.$conversion ?: \"\"" else "$accessor.$conversion",
        )

        "\${$name.${encoder.simpleName}()}"
    }

    return ResolvedPath(expression, variables, boundNames, boundJsonNames)
}

private fun Descriptors.Descriptor.resolveFieldPath(path: String): List<Descriptors.FieldDescriptor>? {
    var current: Descriptors.Descriptor? = this
    val resolved = mutableListOf<Descriptors.FieldDescriptor>()

    for (name in path.split('.')) {
        val field = current?.findFieldByName(name) ?: return null

        resolved += field
        current = when (field.type) {
            Descriptors.FieldDescriptor.Type.MESSAGE, Descriptors.FieldDescriptor.Type.GROUP -> field.messageType
            else -> null
        }
    }

    return resolved.ifEmpty { null }
}
