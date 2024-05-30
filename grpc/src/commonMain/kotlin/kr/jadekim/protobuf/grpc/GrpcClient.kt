package kr.jadekim.protobuf.grpc

import kr.jadekim.protobuf.type.ProtobufServiceClient

open class GrpcClient(override val option: GrpcClientOption) : ProtobufServiceClient<GrpcClientOption> {

    constructor(host: String, port: Int, useTls: Boolean = false) : this(DefaultGrpcClientOption(host, port, useTls))
}
