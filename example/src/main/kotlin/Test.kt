import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.encodeToString
import kr.jadekim.protobuf.example.Example
import kr.jadekim.protobuf.kotlinx.ProtobufFormat
import kr.jadekim.protobuf.kotlinx.ProtobufJsonFormat
import protobuf.example.SimpleMessage
import protobuf.example.SimpleMessageConverter
import protobuf.example.parse
import protobuf.example.toAny
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
        .setTest2(message.test2?.toInt() ?: 0)
        .build()
        .toByteArray()

    println(Base64.getEncoder().encodeToString(serialized))
    println(Base64.getEncoder().encodeToString(nativeSerialized))

    println(protobuf.decodeFromByteArray<SimpleMessage>(serialized))
    println(protobuf.decodeFromByteArray<SimpleMessage>(nativeSerialized))
    println(Example.SimpleMessage.parseFrom(serialized))
    println(Example.SimpleMessage.parseFrom(nativeSerialized))

    println(SimpleMessage.serializer())

    val testMessage = Test(
        "testText",
        message.toAny(),
    )

    val json = ProtobufJsonFormat(JvmTypeRegistry.messages)

    val jsonSerialized = json.encodeToString(testMessage)
    val jsonDeserialized = json.decodeFromString<Test>(jsonSerialized)
    println(jsonSerialized)
    println(jsonDeserialized)
    println(jsonDeserialized.unknown.parse(SimpleMessageConverter))
}
