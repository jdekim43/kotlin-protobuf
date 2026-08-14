package kim.jade.kotlinx.protobuf.integration

import cosmos.bank.v1beta1.MsgSend
import cosmos.bank.v1beta1.converter
import cosmos.base.v1beta1.Coin
import cosmos.base.v1beta1.converter
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kim.jade.kotlinx.protobuf.serialization.ProtobufFormat
import kim.jade.kotlinx.protobuf.serialization.ProtobufJsonFormat

/**
 * The kotlinx.serialization flavour of the generated types.
 *
 * `kotlinxSerialization()` annotates each message with a serializer that hands off to the converter when
 * the encoder is one of this project's, and falls back to a reflective one otherwise. Both formats built
 * on that are checked here: the binary one, which has to agree with the converter it delegates to, and
 * the JSON one, which goes through protobuf's own JSON mapping rather than kotlinx's defaults.
 */
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
})
