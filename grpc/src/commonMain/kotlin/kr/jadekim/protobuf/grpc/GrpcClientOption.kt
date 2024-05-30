package kr.jadekim.protobuf.grpc

import kr.jadekim.protobuf.type.ProtobufServiceClientOption

expect abstract class Channel

expect class GrpcClientOption : ProtobufServiceClientOption

expect fun DefaultGrpcClientOption(
    host: String,
    port: Int,
    useTls: Boolean = false,
): GrpcClientOption