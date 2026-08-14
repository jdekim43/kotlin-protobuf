package kim.jade.kotlinx.protobuf.grpc

import kim.jade.kotlinx.protobuf.type.ProtobufServiceClientOption

expect abstract class Channel

expect class GrpcClientOption : ProtobufServiceClientOption

expect fun DefaultGrpcClientOption(
    host: String,
    port: Int,
    useTls: Boolean = false,
): GrpcClientOption