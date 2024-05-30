package kr.jadekim.protobuf.grpc.gateway

import io.ktor.client.*
import kr.jadekim.protobuf.type.ProtobufServiceClient

open class GrpcGatewayClient(override val option: GrpcGatewayClientOption) : ProtobufServiceClient<GrpcGatewayClientOption> {

    constructor(httpClient: HttpClient) : this(GrpcGatewayClientOption(httpClient))

    open fun close() {
        option.httpClient.close()
    }
}
