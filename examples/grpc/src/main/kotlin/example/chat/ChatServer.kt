package example.chat

import example.chat.v1.grpc.ChatServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Server
import io.grpc.ServerBuilder
import kim.jade.kotlinx.protobuf.grpc.GrpcClientOption

/**
 * A real server on a real port, and clients for it.
 *
 * The generated `Server` is an `io.grpc.BindableService`, so it goes to `addService` like any other; the
 * transport, the port and TLS stay grpc-java's business rather than this library's.
 */
class ChatServer(port: Int = 0) : AutoCloseable {

    val service = InMemoryChatService()

    private val server: Server = ServerBuilder.forPort(port)
        .addService(service)
        .build()
        .start()

    /** The port actually bound. Port 0 asks the OS for a free one, which is what the tests use. */
    val port: Int get() = server.port

    private val channels = mutableListOf<ManagedChannel>()

    /**
     * A client speaking to this server.
     *
     * The channel is grpc-java's, and this project only wraps it: `GrpcClientOption(channel)` for one
     * that is already configured, or `DefaultGrpcClientOption(host, port, useTls)` to have a plaintext or
     * TLS channel built for you. The client it returns *is* the `ChatService` interface, so the calling
     * code cannot tell a local implementation from a remote one.
     */
    fun client(): ChatServiceGrpc.Client {
        val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()
        channels += channel
        return ChatServiceGrpc.createClient(GrpcClientOption(channel))
    }

    override fun close() {
        channels.forEach { it.shutdownNow() }
        server.shutdownNow()
    }
}
