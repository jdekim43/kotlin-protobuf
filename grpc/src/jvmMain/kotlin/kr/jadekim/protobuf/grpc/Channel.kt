package kr.jadekim.protobuf.grpc

actual typealias Channel = io.grpc.Channel

actual class ClientOption(
    val channel: Channel,
    val callOptions: io.grpc.CallOptions = io.grpc.CallOptions.DEFAULT,
)
