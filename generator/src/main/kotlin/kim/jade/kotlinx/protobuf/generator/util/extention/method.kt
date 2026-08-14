package kim.jade.kotlinx.protobuf.generator.util.extention

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import kim.jade.kotlinx.protobuf.generator.util.ProtobufWordSplitter
import net.pearx.kasechange.toCamelCase

val FLOW: ClassName = ClassName("kotlinx.coroutines.flow", "Flow")

/**
 * How an RPC's four streaming shapes turn into Kotlin. These follow grpc-kotlin's conventions, so the
 * JVM implementations can hand straight to `ClientCalls`/`ServerCalls` without adapting anything:
 *
 * | proto                                     | Kotlin                                     |
 * |-------------------------------------------|--------------------------------------------|
 * | `rpc F(Req) returns (Resp)`               | `suspend fun f(request: Req): Resp`        |
 * | `rpc F(Req) returns (stream Resp)`        | `fun f(request: Req): Flow<Resp>`          |
 * | `rpc F(stream Req) returns (Resp)`        | `suspend fun f(requests: Flow<Req>): Resp` |
 * | `rpc F(stream Req) returns (stream Resp)` | `fun f(requests: Flow<Req>): Flow<Resp>`   |
 */
val Descriptors.MethodDescriptor.functionName: String
    get() = name.toCamelCase(ProtobufWordSplitter)

val Descriptors.MethodDescriptor.requestParameterName: String
    get() = if (isClientStreaming) "requests" else "request"

val Descriptors.MethodDescriptor.requestTypeName: TypeName
    get() = inputType.outputTypeName.let { if (isClientStreaming) FLOW.parameterizedBy(it) else it }

val Descriptors.MethodDescriptor.responseTypeName: TypeName
    get() = outputType.outputTypeName.let { if (isServerStreaming) FLOW.parameterizedBy(it) else it }

val Descriptors.MethodDescriptor.isSuspending: Boolean
    get() = !isServerStreaming

fun Descriptors.MethodDescriptor.functionSpecBuilder(vararg modifiers: KModifier): FunSpec.Builder =
    FunSpec.builder(functionName)
        .addModifiers(modifiers.toList())
        .apply { if (isSuspending) addModifiers(KModifier.SUSPEND) }
        .addParameter(requestParameterName, requestTypeName)
        .returns(responseTypeName)
