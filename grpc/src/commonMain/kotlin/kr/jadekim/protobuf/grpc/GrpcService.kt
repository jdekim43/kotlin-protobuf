package kr.jadekim.protobuf.grpc

interface GrpcService<Interface, Client : Interface> {

    fun createClient(option: ClientOption): Client
}