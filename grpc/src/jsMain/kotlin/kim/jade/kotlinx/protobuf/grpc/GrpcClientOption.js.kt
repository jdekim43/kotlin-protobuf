package kim.jade.kotlinx.protobuf.grpc

import kim.jade.kotlinx.protobuf.grpc.node.ChannelCredentials
import kim.jade.kotlinx.protobuf.grpc.node.Client
import kim.jade.kotlinx.protobuf.grpc.node.Metadata
import kim.jade.kotlinx.protobuf.grpc.node.credentials
import kim.jade.kotlinx.protobuf.grpc.node.requireNode
import kim.jade.kotlinx.protobuf.type.ProtobufServiceClientOption

actual abstract class Channel {

    abstract val client: Client

    abstract fun close()
}

class NodeChannel(
    val address: String,
    val credentials: ChannelCredentials,
) : Channel() {

    init {
        requireNode("Opening a gRPC channel")
    }

    override val client: Client = Client(address, credentials)

    override fun close() {
        client.close()
    }
}

actual class GrpcClientOption(
    val channel: Channel,
    val metadata: Metadata = Metadata(),
) : ProtobufServiceClientOption

@Suppress("FunctionName")
actual fun DefaultGrpcClientOption(
    host: String,
    port: Int,
    useTls: Boolean,
): GrpcClientOption = GrpcClientOption(
    NodeChannel(
        "$host:$port",
        if (useTls) credentials.createSsl() else credentials.createInsecure(),
    )
)
