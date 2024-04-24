import com.google.api.AnnotationsProto
import com.google.api.HttpRule
import grpc.jvm.MessagingGrpcJvm
import grpc.jvm.TestServiceGrpcJvm
import io.grpc.MethodDescriptor
import io.grpc.ServerServiceDefinition
import io.grpc.protobuf.ProtoMethodDescriptorSupplier

//fun main() {
//    val message = SimpleMessage(
//        "testValue",
//        1u,
//    )
//
//    val protobuf = ProtobufFormat()
//    val serialized = protobuf.encodeToByteArray(message)
//    val nativeSerialized = Example.SimpleMessage.newBuilder()
//        .setTest(message.test)
//        .setTest2(message.test2!!.toInt())
//        .build()
//        .toByteArray()
//
//    println(Base64.getEncoder().encodeToString(serialized))
//    println(Base64.getEncoder().encodeToString(nativeSerialized))
//
//    println(protobuf.decodeFromByteArray<SimpleMessage>(serialized))
//    println(protobuf.decodeFromByteArray<SimpleMessage>(nativeSerialized))
//    println(Example.SimpleMessage.parseFrom(serialized))
//    println(Example.SimpleMessage.parseFrom(nativeSerialized))
//
//    println(SimpleMessage.serializer())
//}


private val pathParameterRegex = """\{(.+?)}""".toRegex()

private fun polishPath(pathRaw: String): Pair<String, List<String>> {
    var path = pathRaw
    val parameterNames = mutableListOf<String>()

    for (parameter in pathParameterRegex.findAll(pathRaw)) {
        if (parameter.groupValues.size < 2) continue
        var parameterName = parameter.groupValues[1]
        val delimiterIndex = parameterName.indexOf('=')
        if (delimiterIndex > 0) {
            parameterName = parameterName.substring(0, delimiterIndex)
        }

        path = path.replace(parameter.value, "\${$parameterName}")
        parameterNames += parameterName
    }

    return path to parameterNames
}

val MethodDescriptor<*, *>.httpExtension: HttpRule?
    get() = (schemaDescriptor as? ProtoMethodDescriptorSupplier)?.methodDescriptor?.options?.getExtension(
        AnnotationsProto.http
    )

fun main() {
    TestOuterClass.getDescriptor().services.forEach { service ->
        println(service.name)
        service.methods.forEach { method ->
            println(method.name)
            val http = method.options.getExtension(AnnotationsProto.http)
            println(method.options.hasExtension(AnnotationsProto.http))
            println(http)
        }
    }

    println(TestServiceGrpcJvm.testTESTDescriptor.httpExtension ?: "[empty]")
    println(TestServiceGrpcJvm.testGatewayDescriptor.httpExtension ?: "[empty]")
    println(MessagingGrpcJvm.getMessageDescriptor.httpExtension ?: "[empty]")
}
