package kim.jade.kotlinx.protobuf.grpc.gateway

import io.ktor.client.*
import kim.jade.kotlinx.protobuf.type.ProtobufServiceClient

open class GrpcGatewayClient(override val option: GrpcGatewayClientOption) :
    ProtobufServiceClient<GrpcGatewayClientOption> {

    constructor(httpClient: HttpClient) : this(GrpcGatewayClientOption(httpClient))

    open fun close() {
        option.httpClient.close()
    }
}
