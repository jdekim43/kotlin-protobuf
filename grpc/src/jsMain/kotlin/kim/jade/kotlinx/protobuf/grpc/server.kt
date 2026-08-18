package kim.jade.kotlinx.protobuf.grpc

import kim.jade.kotlinx.protobuf.grpc.node.Server
import kim.jade.kotlinx.protobuf.grpc.node.requireNode
import kim.jade.kotlinx.protobuf.type.ProtobufMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class GrpcServiceBinding internal constructor(
    val definition: dynamic,
    val implementation: dynamic,
)

fun Server.addService(binding: GrpcServiceBinding) {
    addService(binding.definition, binding.implementation)
}

fun grpcService(context: CoroutineContext, block: GrpcServiceBuilder.() -> Unit): GrpcServiceBinding {
    requireNode("Binding a gRPC service")

    val builder = GrpcServiceBuilder(CoroutineScope(context + SupervisorJob()))
    builder.block()

    return builder.build()
}

class GrpcServiceBuilder internal constructor(private val scope: CoroutineScope) {

    private val definition: dynamic = js("({})")
    private val implementation: dynamic = js("({})")

    /** `rpc F(Req) returns (Resp)`. */
    fun <Request : ProtobufMessage, Response : ProtobufMessage> unary(
        name: String,
        method: GrpcMethod<Request, Response>,
        handler: suspend (Request) -> Response,
    ) {
        declare(name, method)

        implementation[name] = { call: dynamic, callback: dynamic ->
            scope.launch {
                try {
                    callback(null, handler(call.request.unsafeCast<Request>()))
                } catch (cause: Throwable) {
                    callback(cause.toServiceError(), null)
                }
            }
            Unit
        }
    }

    /** `rpc F(Req) returns (stream Resp)`. */
    fun <Request : ProtobufMessage, Response : ProtobufMessage> serverStreaming(
        name: String,
        method: GrpcMethod<Request, Response>,
        handler: (Request) -> Flow<Response>,
    ) {
        declare(name, method)

        implementation[name] = { call: dynamic ->
            scope.launch {
                try {
                    handler(call.request.unsafeCast<Request>()).collect { call.write(it) }
                    call.end()
                } catch (cause: Throwable) {
                    call.emit("error", cause.toServiceError())
                }
            }
            Unit
        }
    }

    /** `rpc F(stream Req) returns (Resp)`. */
    fun <Request : ProtobufMessage, Response : ProtobufMessage> clientStreaming(
        name: String,
        method: GrpcMethod<Request, Response>,
        handler: suspend (Flow<Request>) -> Response,
    ) {
        declare(name, method)

        implementation[name] = { call: dynamic, callback: dynamic ->
            scope.launch {
                try {
                    callback(null, handler(requestFlow(call)))
                } catch (cause: Throwable) {
                    callback(cause.toServiceError(), null)
                }
            }
            Unit
        }
    }

    /** `rpc F(stream Req) returns (stream Resp)`. */
    fun <Request : ProtobufMessage, Response : ProtobufMessage> bidiStreaming(
        name: String,
        method: GrpcMethod<Request, Response>,
        handler: (Flow<Request>) -> Flow<Response>,
    ) {
        declare(name, method)

        implementation[name] = { call: dynamic ->
            scope.launch {
                try {
                    handler(requestFlow<Request>(call)).collect { call.write(it) }
                    call.end()
                } catch (cause: Throwable) {
                    call.emit("error", cause.toServiceError())
                }
            }
            Unit
        }
    }

    internal fun build(): GrpcServiceBinding = GrpcServiceBinding(definition, implementation)

    private fun declare(name: String, method: GrpcMethod<*, *>) {
        val entry: dynamic = js("({})")
        entry.path = method.path
        entry.requestStream = method.requestStream
        entry.responseStream = method.responseStream
        entry.requestSerialize = method.serializeRequest
        entry.requestDeserialize = method.deserializeRequest
        entry.responseSerialize = method.serializeResponse
        entry.responseDeserialize = method.deserializeResponse

        definition[name] = entry
    }
}

private fun <Request : ProtobufMessage> requestFlow(call: dynamic): Flow<Request> =
    callbackFlow {
        call.on("data") { value: dynamic -> trySend(value.unsafeCast<Request>()) }
        call.on("error") { error: dynamic -> close(GrpcStatusException(GrpcStatus.UNKNOWN, "$error", error)) }
        call.on("end") { -> close() }

        awaitClose { }
    }

private fun Throwable.toServiceError(): dynamic {
    val error: dynamic = js("({})")

    if (this is GrpcStatusException) {
        error.code = code
        error.details = details
    } else {
        error.code = GrpcStatus.UNKNOWN.code
        error.details = message ?: toString()
    }

    return error
}
