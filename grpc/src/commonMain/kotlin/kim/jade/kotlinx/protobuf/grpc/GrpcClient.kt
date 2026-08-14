package kim.jade.kotlinx.protobuf.grpc

import kim.jade.kotlinx.protobuf.type.ProtobufServiceClient

open class GrpcClient(override val option: GrpcClientOption) : ProtobufServiceClient<GrpcClientOption> {

    constructor(host: String, port: Int, useTls: Boolean = false) : this(DefaultGrpcClientOption(host, port, useTls))
}
