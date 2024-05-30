package kr.jadekim.protobuf.grpc.gateway

import io.ktor.client.*
import kr.jadekim.protobuf.type.ProtobufServiceClientOption

class GrpcGatewayClientOption(
    val httpClient: HttpClient,
) : ProtobufServiceClientOption
