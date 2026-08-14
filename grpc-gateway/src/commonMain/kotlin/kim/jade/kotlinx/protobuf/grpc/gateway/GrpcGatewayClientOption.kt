package kim.jade.kotlinx.protobuf.grpc.gateway

import io.ktor.client.*
import kim.jade.kotlinx.protobuf.type.ProtobufServiceClientOption

class GrpcGatewayClientOption(
    val httpClient: HttpClient,
) : ProtobufServiceClientOption
