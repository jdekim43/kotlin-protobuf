package kr.jadekim.protobuf.grpc.gateway.ktor

import io.ktor.client.*
import io.ktor.client.engine.*

expect class GrpcGatewayClientConfigVariables constructor(url: String) {
    val url: String
}

expect fun <T : HttpClientEngineConfig> GrpcGatewayClient(
    engineFactory: HttpClientEngineFactory<T>,
    config: GrpcGatewayClientConfigVariables,
    block: HttpClientConfig<T>.() -> Unit = {},
): HttpClient