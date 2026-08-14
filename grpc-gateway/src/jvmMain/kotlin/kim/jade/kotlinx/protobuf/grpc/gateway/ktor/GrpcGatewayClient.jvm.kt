package kim.jade.kotlinx.protobuf.grpc.gateway.ktor

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import kim.jade.kotlinx.protobuf.grpc.gateway.ktor.plugin.BodyConverter
import kim.jade.kotlinx.protobuf.serialization.ProtobufJsonFormat

actual class GrpcGatewayClientConfigVariables(
    actual val url: String,
    var format: ProtobufJsonFormat = ProtobufJsonFormat(),
) {

    actual constructor(url: String) : this(url, ProtobufJsonFormat())
}

@Suppress("FunctionName")
actual fun <T : HttpClientEngineConfig> GrpcGatewayClient(
    engineFactory: HttpClientEngineFactory<T>,
    config: GrpcGatewayClientConfigVariables,
    block: HttpClientConfig<T>.() -> Unit,
) = HttpClient(engineFactory) {
    expectSuccess = true

    install(DefaultRequest) {
        url(config.url)
    }

    install(BodyConverter) {
        format = config.format
    }

    block()
}