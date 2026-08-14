package kim.jade.kotlinx.protobuf.grpc.gateway

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import kim.jade.kotlinx.protobuf.grpc.gateway.ktor.plugin.BodyConverter
import kim.jade.kotlinx.protobuf.serialization.ProtobufJsonFormat

fun GrpcGatewayClientOption(
    url: String,
    engine: HttpClientEngine,
    format: ProtobufJsonFormat = ProtobufJsonFormat(),
    configure: HttpClientConfig<*>.() -> Unit = {},
) = GrpcGatewayClientOption(DefaultHttpClient(url, engine, format, configure))

@Suppress("FunctionName")
private fun DefaultHttpClient(
    url: String,
    engine: HttpClientEngine,
    protobufFormat: ProtobufJsonFormat = ProtobufJsonFormat(),
    configure: HttpClientConfig<*>.() -> Unit = {},
): HttpClient = HttpClient(engine) {
    expectSuccess = true

    install(DefaultRequest) {
        url(url)
    }

    install(BodyConverter) {
        format = protobufFormat
    }

    configure()
}