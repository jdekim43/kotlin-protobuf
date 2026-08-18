package kim.jade.kotlinx.protobuf.integration

import cosmos.JsTypeRegistry
import cosmos.bank.v1beta1.MsgSend
import cosmos.bank.v1beta1.converter
import cosmos.base.v1beta1.Coin
import cosmos.tx.v1beta1.TxBody
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kim.jade.kotlinx.protobuf.converter.protobufjs.ProtobufJsTypeRegistry
import kim.jade.kotlinx.protobuf.serialization.ProtobufJsonFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import google.protobuf.Any as ProtobufAny

/**
 * `google.protobuf.Any` on JS, which is the one thing the descriptors a message carries cannot always
 * answer by themselves.
 *
 * A `TxBody` holds whatever messages a transaction happens to carry, and `tx.proto` imports none of them
 * — so printing one means telling protobuf.js where to find them. That is what `jsTypeRegistry(…)`
 * generates: the list of files these protos produced, which [ProtobufJsTypeRegistry] resolves against.
 * The JVM answers the same question with a protobuf-java `TypeRegistry` from `jvmTypeRegistry(…)`.
 */
class CosmosJsAnyTest : StringSpec({

    val send = MsgSend(
        fromAddress = "cosmos1sender",
        toAddress = "cosmos1recipient",
        amount = listOf(Coin("uatom", "100")),
    )

    fun packed() = ProtobufAny(
        typeUrl = "/cosmos.bank.v1beta1.MsgSend",
        value = MsgSend.converter.serialize(send),
    )

    "fails clearly when nothing describes what the Any holds" {
        // Deliberately before the registry is populated — this is the state a build is in until it asks
        // for one, and protobuf-java-util words the same failure the same way.
        val format = ProtobufJsonFormat()
        val body = TxBody(messages = listOf(packed()))

        val failure = runCatching { format.encodeToString(TxBody.KotlinxSerializer, body) }.exceptionOrNull()

        failure?.message shouldContain "Cannot find type for url"
    }

    "resolves an Any through the generated registry" {
        val format = ProtobufJsonFormat().apply { addTypes(JsTypeRegistry.files) }
        val body = TxBody(messages = listOf(packed()), memo = "a transfer")

        val json = Json.parseToJsonElement(format.encodeToString(TxBody.KotlinxSerializer, body)) as JsonObject
        val message = json["messages"]!!.jsonArray.single().jsonObject

        // The packed message's own fields sit alongside the URL, which is what protobuf's JSON mapping
        // says an Any holding an ordinary message looks like.
        message["@type"]?.jsonPrimitive?.content shouldBe "/cosmos.bank.v1beta1.MsgSend"
        message["fromAddress"]?.jsonPrimitive?.content shouldBe "cosmos1sender"
        message["toAddress"]?.jsonPrimitive?.content shouldBe "cosmos1recipient"
    }

    "round-trips an Any back to the same bytes" {
        val format = ProtobufJsonFormat().apply { addTypes(JsTypeRegistry.files) }
        val body = TxBody(messages = listOf(packed()), memo = "a transfer")

        val decoded = format.decodeFromString(
            TxBody.KotlinxSerializer,
            format.encodeToString(TxBody.KotlinxSerializer, body),
        )

        val back = decoded.messages.single()
        back.typeUrl shouldBe "/cosmos.bank.v1beta1.MsgSend"
        MsgSend.converter.deserialize(back.value) shouldBe send
        decoded.memo shouldBe "a transfer"
    }

    "the generated registry names every file these protos produced" {
        JsTypeRegistry.messages["/cosmos.bank.v1beta1.MsgSend"] shouldBe
            JsTypeRegistry.messages["/cosmos.bank.v1beta1.MsgSendResponse"]

        (JsTypeRegistry.files.size > 1) shouldBe true
    }
})
