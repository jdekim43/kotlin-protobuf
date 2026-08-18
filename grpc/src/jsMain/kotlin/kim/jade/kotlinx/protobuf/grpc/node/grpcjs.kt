@file:JsModule("@grpc/grpc-js")

package kim.jade.kotlinx.protobuf.grpc.node

import org.khronos.webgl.Uint8Array

external class Client(
    address: String,
    credentials: ChannelCredentials,
    options: dynamic = definedExternally,
) {

    fun makeUnaryRequest(
        method: String,
        serialize: (Any?) -> Uint8Array,
        deserialize: (Uint8Array) -> Any?,
        argument: Any?,
        metadata: Metadata,
        callback: (error: dynamic, value: Any?) -> Unit,
    ): ClientCall

    fun makeServerStreamRequest(
        method: String,
        serialize: (Any?) -> Uint8Array,
        deserialize: (Uint8Array) -> Any?,
        argument: Any?,
        metadata: Metadata,
    ): ClientReadableStream

    fun makeClientStreamRequest(
        method: String,
        serialize: (Any?) -> Uint8Array,
        deserialize: (Uint8Array) -> Any?,
        metadata: Metadata,
        callback: (error: dynamic, value: Any?) -> Unit,
    ): ClientWritableStream

    fun makeBidiStreamRequest(
        method: String,
        serialize: (Any?) -> Uint8Array,
        deserialize: (Uint8Array) -> Any?,
        metadata: Metadata,
    ): ClientDuplexStream

    fun close()
}

external interface ClientCall {

    fun cancel()
}

external interface ClientReadableStream : ClientCall {

    fun on(event: String, listener: (dynamic) -> Unit): ClientReadableStream
}

external interface ClientWritableStream : ClientCall {

    fun write(chunk: Any?): Boolean

    fun end()

    fun on(event: String, listener: (dynamic) -> Unit): ClientWritableStream
}

external interface ClientDuplexStream : ClientCall {

    fun write(chunk: Any?): Boolean

    fun end()

    fun on(event: String, listener: (dynamic) -> Unit): ClientDuplexStream
}

external class Metadata {

    fun add(key: String, value: String)

    fun get(key: String): Array<dynamic>
}

external interface ChannelCredentials

external object credentials {

    fun createInsecure(): ChannelCredentials

    fun createSsl(): ChannelCredentials
}

external interface ServerCredentialsHandle

external object ServerCredentials {

    fun createInsecure(): ServerCredentialsHandle
}

external class Server {

    fun addService(service: dynamic, implementation: dynamic)

    fun bindAsync(
        port: String,
        credentials: ServerCredentialsHandle,
        callback: (error: dynamic, port: Int) -> Unit,
    )

    fun tryShutdown(callback: (error: dynamic) -> Unit)

    fun forceShutdown()
}
