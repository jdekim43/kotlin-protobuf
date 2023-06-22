import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kr.jadekim.protobuf.example.Example
import kr.jadekim.protobuf.kotlinx.ProtobufFormat
import protobuf.example.SimpleMessage
import java.util.*

fun main() {
    val message = SimpleMessage(
        "testValue",
        1u,
    )

    val protobuf = ProtobufFormat()
    val serialized = protobuf.encodeToByteArray(message)
    val nativeSerialized = Example.SimpleMessage.newBuilder()
        .setTest(message.test)
        .setTest2(message.test2!!.toInt())
        .build()
        .toByteArray()

    println(Base64.getEncoder().encodeToString(serialized))
    println(Base64.getEncoder().encodeToString(nativeSerialized))

    println(protobuf.decodeFromByteArray<SimpleMessage>(serialized))
    println(protobuf.decodeFromByteArray<SimpleMessage>(nativeSerialized))
    println(Example.SimpleMessage.parseFrom(serialized))
    println(Example.SimpleMessage.parseFrom(nativeSerialized))

    println(SimpleMessage.serializer())
}
