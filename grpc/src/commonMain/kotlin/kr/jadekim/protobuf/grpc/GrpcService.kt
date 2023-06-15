package kr.jadekim.protobuf.grpc

interface GrpcService<Interface, Server : Interface, Client : Interface> {

    fun createClient(option: ClientOption): Client
}