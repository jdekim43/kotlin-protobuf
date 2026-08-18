package kim.jade.kotlinx.protobuf.grpc

import kim.jade.kotlinx.protobuf.grpc.node.ClientCall
import kim.jade.kotlinx.protobuf.grpc.node.ClientWritableStream
import kim.jade.kotlinx.protobuf.type.ProtobufMessage
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GrpcStatusException(
    val status: GrpcStatus,
    val details: String,
    val code: Int = status.code,
    val error: dynamic = null,
) : RuntimeException("gRPC call failed with ${describe(status, code)}: $details")

private fun describe(status: GrpcStatus, code: Int): String =
    if (status.code == code) "$status" else "$status (unknown code $code)"

private fun statusExceptionOf(error: dynamic): GrpcStatusException {
    val code = error?.code as? Int

    return GrpcStatusException(
        status = GrpcStatus.forCode(code),
        details = (error?.details as? String) ?: (error?.message as? String) ?: "unknown error",
        code = code ?: GrpcStatus.UNKNOWN.code,
        error = error,
    )
}

private fun <T> CancellableContinuation<T>.resumeFrom(error: dynamic, value: Any?) {
    if (error != null) {
        resumeWithException(statusExceptionOf(error))
    } else {
        @Suppress("UNCHECKED_CAST")
        resume(value as T)
    }
}

private fun CancellableContinuation<*>.cancelCallOnCancellation(call: ClientCall) {
    invokeOnCancellation { call.cancel() }
}

suspend fun <Request : ProtobufMessage, Response : ProtobufMessage> GrpcClientOption.unaryCall(
    method: GrpcMethod<Request, Response>,
    request: Request,
): Response = suspendCancellableCoroutine<Response> { continuation ->
    val call = channel.client.makeUnaryRequest(
        method.path,
        method.serializeRequest,
        method.deserializeResponse,
        request,
        metadata,
    ) { error, value -> continuation.resumeFrom(error, value) }

    continuation.cancelCallOnCancellation(call)
}

/** `rpc F(Req) returns (stream Resp)`. */
fun <Request : ProtobufMessage, Response : ProtobufMessage> GrpcClientOption.serverStreamingCall(
    method: GrpcMethod<Request, Response>,
    request: Request,
): Flow<Response> {
    val client = channel.client
    val callMetadata = metadata

    return callbackFlow {
        val call = client.makeServerStreamRequest(
        method.path,
        method.serializeRequest,
        method.deserializeResponse,
            request,
            callMetadata,
        )

        call.on("data") { value -> trySend(value.unsafeCast<Response>()) }
        call.on("error") { error -> close(statusExceptionOf(error)) }
        call.on("end") { close() }

        awaitClose { call.cancel() }
    }
}

/** `rpc F(stream Req) returns (Resp)`. */
suspend fun <Request : ProtobufMessage, Response : ProtobufMessage> GrpcClientOption.clientStreamingCall(
    method: GrpcMethod<Request, Response>,
    requests: Flow<Request>,
): Response = coroutineScope {
    lateinit var call: ClientWritableStream

    val response = suspendCancellableCoroutine<Response> { continuation ->
        call = channel.client.makeClientStreamRequest(
            method.path,
            method.serializeRequest,
            method.deserializeResponse,
            metadata,
        ) { error, value -> continuation.resumeFrom(error, value) }

        continuation.cancelCallOnCancellation(call)

        launch {
            try {
                requests.collect { call.write(it) }
                call.end()
            } catch (cause: Throwable) {
                call.cancel()
                throw cause
            }
        }
    }

    response
}

fun <Request : ProtobufMessage, Response : ProtobufMessage> GrpcClientOption.bidiStreamingCall(
    method: GrpcMethod<Request, Response>,
    requests: Flow<Request>,
): Flow<Response> {
    val client = channel.client
    val callMetadata = metadata

    return callbackFlow {
        val call = client.makeBidiStreamRequest(
            method.path,
            method.serializeRequest,
            method.deserializeResponse,
            callMetadata,
        )

        call.on("data") { value -> trySend(value.unsafeCast<Response>()) }
        call.on("error") { error -> close(statusExceptionOf(error)) }
        call.on("end") { close() }

        val writer = launch {
            requests.collect { call.write(it) }
            call.end()
        }

        awaitClose {
            writer.cancel()
            call.cancel()
        }
    }
}
