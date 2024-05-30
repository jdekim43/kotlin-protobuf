package kr.jadekim.protobuf.grpc

fun GrpcClient(
    channel: Channel,
    callOptions: io.grpc.CallOptions = io.grpc.CallOptions.DEFAULT,
) = GrpcClient(GrpcClientOption(channel, callOptions))
