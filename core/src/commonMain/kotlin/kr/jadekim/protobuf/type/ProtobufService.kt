package kr.jadekim.protobuf.type

interface ProtobufService

interface ProtobufServiceFactory<I : ProtobufService, C : I, CO : ProtobufServiceClientOption> {

    fun createClient(option: CO): C
}
