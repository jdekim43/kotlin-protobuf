package kr.jadekim.protobuf.grpc.gateway

import io.ktor.client.*

interface GrpcGatewayService<Interface, Client : Interface> {

    fun createClient(http: HttpClient): Client
}
