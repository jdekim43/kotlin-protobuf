import cosmos.params.v1beta1.QueryParamsRequest
import cosmos.params.v1beta1.grpc.gateway.QueryGrpcGateway
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.logging.*
import kr.jadekim.protobuf.grpc.gateway.GrpcGatewayClientOption
import kr.jadekim.protobuf.grpc.gateway.ktor.GrpcGatewayClient
import kr.jadekim.protobuf.grpc.gateway.ktor.GrpcGatewayClientConfigVariables
import kr.jadekim.protobuf.kotlinx.ProtobufJsonFormat

suspend fun main() {
    val configVariables = GrpcGatewayClientConfigVariables(
        url = "https://sentry.lcd.injective.network",
        format = ProtobufJsonFormat(JvmTypeRegistry.messages, SerializersModules.messages)
    )
    val httpClient = GrpcGatewayClient(OkHttp, configVariables) {
        install(Logging) {
            level = LogLevel.ALL
        }
    }
    val client = QueryGrpcGateway.createClient(GrpcGatewayClientOption(httpClient))

    val response = client.params(QueryParamsRequest("gov", "voting_period"))

    println(response)
}