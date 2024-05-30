package kr.jadekim.protobuf.grpc

import io.grpc.ManagedChannelBuilder
import kr.jadekim.protobuf.type.ProtobufServiceClientOption

actual typealias Channel = io.grpc.Channel

actual class GrpcClientOption(
    val channel: Channel,
    val callOptions: io.grpc.CallOptions = io.grpc.CallOptions.DEFAULT,
) : ProtobufServiceClientOption

actual fun DefaultGrpcClientOption(
    host: String,
    port: Int,
    useTls: Boolean,
) = GrpcClientOption(
    ManagedChannelBuilder.forAddress(host, port)
        .run {
            if (useTls) {
                useTransportSecurity()
            } else {
                usePlaintext()
            }
        }
        .build(),
)