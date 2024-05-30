package kr.jadekim.protobuf.type

interface ProtobufServiceClientOption

interface ProtobufServiceClient<CO : ProtobufServiceClientOption> {

    val option: CO

    fun <I : ProtobufService, C : I, SF : ProtobufServiceFactory<I, C, CO>> service(factory: SF): C {
        return factory.createClient(option)
    }
}