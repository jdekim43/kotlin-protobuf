package kim.jade.kotlinx.protobuf.integration

import cosmos.bank.v1beta1.MsgSend
import cosmos.bank.v1beta1.converter
import cosmos.base.v1beta1.Coin
import cosmos.base.query.v1beta1.PageRequest
import cosmos.base.v1beta1.converter
import cosmos.staking.v1beta1.Params
import cosmos.tx.signing.v1beta1.SignMode
import cosmos.tx.v1beta1.ModeInfo
import google.protobuf.Duration
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kim.jade.kotlinx.protobuf.serialization.ProtobufFormat
import kim.jade.kotlinx.protobuf.serialization.ProtobufJsonFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * The kotlinx.serialization flavour of the generated types.
 *
 * `kotlinxSerialization()` annotates each message with a serializer that hands off to the converter when
 * the encoder is one of this project's, and falls back to a reflective one otherwise. Both formats built
 * on that are checked here: the binary one, which has to agree with the converter it delegates to, and
 * the JSON one, which goes through protobuf's own JSON mapping rather than kotlinx's defaults.
 *
 * In `commonTest` on purpose. `ProtobufJsonFormat` prints through protobuf-java-util on the JVM and
 * protobuf.js on JS, and the whole point of the mapping is that a reader cannot tell which produced it —
 * so the values below are asserted on both, and a platform that drifts fails here rather than at some
 * gateway.
 *
 * On the parsed document rather than the printed string: the two implementations lay JSON out
 * differently — protobuf-java-util indents, protobuf.js does not — and whitespace is the one thing the
 * mapping says nothing about.
 */
private fun printed(json: String): JsonObject = Json.parseToJsonElement(json) as JsonObject

class CosmosSerializationFormatTest : StringSpec({

    "encodes through ProtobufFormat exactly as the converter does" {
        val coin = Coin(denom = "uatom", amount = "1000")

        val viaFormat = ProtobufFormat().encodeToByteArray(Coin.KotlinxSerializer, coin)

        // The serializer delegates to CoinConverter for this encoder, so the two must not diverge.
        viaFormat.toList() shouldContainExactly Coin.converter.serialize(coin).toList()
        ProtobufFormat().decodeFromByteArray(Coin.KotlinxSerializer, viaFormat) shouldBe coin
    }

    "round-trips a nested message through ProtobufFormat" {
        val message = MsgSend(
            fromAddress = "cosmos1sender",
            toAddress = "cosmos1recipient",
            amount = listOf(Coin("uatom", "100"), Coin("uosmo", "250")),
        )
        val format = ProtobufFormat()

        val decoded = format.decodeFromByteArray(
            MsgSend.KotlinxSerializer,
            format.encodeToByteArray(MsgSend.KotlinxSerializer, message),
        )

        decoded shouldBe message
    }

    "uses protobuf's JSON mapping, not kotlinx's defaults" {
        val format = ProtobufJsonFormat()
        val message = MsgSend(
            fromAddress = "cosmos1sender",
            toAddress = "cosmos1recipient",
            amount = listOf(Coin("uatom", "100")),
        )

        val json = format.encodeToString(MsgSend.KotlinxSerializer, message)

        // protobuf JSON camelCases the field names declared as from_address / to_address in the proto.
        json shouldContain "fromAddress"
        json shouldContain "toAddress"
        format.decodeFromString(MsgSend.KotlinxSerializer, json) shouldBe message
    }

    "prints a 64-bit field as a string, on every platform" {
        // The rule that costs the most to get wrong: a JS reader silently rounds anything past 2^53 if
        // the number goes out unquoted.
        val page = PageRequest(key = byteArrayOf(1, 2, 3), limit = 9007199254740993uL, countTotal = true)

        val json = printed(ProtobufJsonFormat().encodeToString(PageRequest.KotlinxSerializer, page))

        json["limit"]?.jsonPrimitive?.isString shouldBe true
        json["limit"]?.jsonPrimitive?.content shouldBe "9007199254740993"
        // bytes are base64, not an array of numbers.
        json["key"]?.jsonPrimitive?.content shouldBe "AQID"
        json["countTotal"]?.jsonPrimitive?.content shouldBe "true"
    }

    "prints an enum by name, on every platform" {
        val info = ModeInfo(sum = ModeInfo.SumOneOf.Single(ModeInfo.Single(mode = SignMode.SIGN_MODE_DIRECT)))

        val json = ProtobufJsonFormat().encodeToString(ModeInfo.KotlinxSerializer, info)

        json shouldContain "SIGN_MODE_DIRECT"
    }

    "gives a well-known type its own JSON form, on every platform" {
        // A Duration is "1.5s", not {"seconds":…,"nanos":…}. protobuf.js implements none of these forms
        // itself, so this is the assertion that keeps the JS side honest.
        val params = Params(unbondingTime = Duration(seconds = 90L, nanos = 500_000_000))

        val json = printed(ProtobufJsonFormat().encodeToString(Params.KotlinxSerializer, params))

        json["unbondingTime"]?.jsonPrimitive?.content shouldBe "90.500s"
    }

    "round-trips every one of those shapes back" {
        val format = ProtobufJsonFormat()
        val page = PageRequest(key = byteArrayOf(1, 2, 3), limit = 9007199254740993uL, countTotal = true)

        val decoded = format.decodeFromString(
            PageRequest.KotlinxSerializer,
            format.encodeToString(PageRequest.KotlinxSerializer, page),
        )

        decoded.limit shouldBe page.limit
        decoded.key.toList() shouldContainExactly page.key.toList()
        decoded.countTotal shouldBe page.countTotal
    }
})
