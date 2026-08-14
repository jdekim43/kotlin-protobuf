package kim.jade.kotlinx.protobuf.integration

import com.google.protobuf.ByteString
import cosmos.bank.v1beta1.MsgSend
import cosmos.bank.v1beta1.converter
import cosmos.base.query.v1beta1.PageRequest
import cosmos.base.query.v1beta1.converter
import cosmos.base.v1beta1.Coin
import cosmos.base.v1beta1.converter
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import cosmos.bank.v1beta1.Tx as JavaTx
import cosmos.base.query.v1beta1.Pagination as JavaPagination
import cosmos.base.v1beta1.CoinOuterClass as JavaCoin

/**
 * The bytes are protobuf, not merely bytes that survive a round trip through this library.
 *
 * A codec that is wrong in the same way twice passes every round-trip test there is. These check the
 * output against protoc-gen-java's, in both directions, over the same cosmos-sdk schema — which is the
 * only way to know that something on the other end of a socket will understand it.
 */
class CosmosWireCompatibilityTest : StringSpec({

    "encodes a flat message byte for byte as protobuf-java does" {
        val coin = Coin(denom = "uatom", amount = "1000000")
        val java = JavaCoin.Coin.newBuilder().setDenom("uatom").setAmount("1000000").build()

        Coin.converter.serialize(coin).toList() shouldContainExactly java.toByteArray().toList()
    }

    "produces bytes protobuf-java can parse" {
        val message = MsgSend(
            fromAddress = "cosmos1sender",
            toAddress = "cosmos1recipient",
            amount = listOf(Coin("uatom", "100"), Coin("uosmo", "250")),
        )

        val parsed = JavaTx.MsgSend.parseFrom(MsgSend.converter.serialize(message))

        parsed.fromAddress shouldBe "cosmos1sender"
        parsed.toAddress shouldBe "cosmos1recipient"
        parsed.amountList.map { it.denom to it.amount } shouldContainExactly
            listOf("uatom" to "100", "uosmo" to "250")
    }

    "parses bytes protobuf-java produced" {
        val java = JavaTx.MsgSend.newBuilder()
            .setFromAddress("cosmos1sender")
            .setToAddress("cosmos1recipient")
            .addAmount(JavaCoin.Coin.newBuilder().setDenom("uatom").setAmount("100"))
            .addAmount(JavaCoin.Coin.newBuilder().setDenom("uosmo").setAmount("250"))
            .build()

        val decoded = MsgSend.converter.deserialize(java.toByteArray())

        decoded shouldBe MsgSend(
            fromAddress = "cosmos1sender",
            toAddress = "cosmos1recipient",
            amount = listOf(Coin("uatom", "100"), Coin("uosmo", "250")),
        )
    }

    "agrees with protobuf-java on uint64, bytes and bool" {
        // The mappings with room to disagree: a uint64 above Long.MAX_VALUE is negative read as signed,
        // and bytes has to survive as-is rather than through a string.
        val key = byteArrayOf(1, 2, 3, -1, 0, 127)
        val offset = ULong.MAX_VALUE - 1uL

        val request = PageRequest(key = key, offset = offset, limit = 100uL, countTotal = true)
        val java = JavaPagination.PageRequest.newBuilder()
            .setKey(ByteString.copyFrom(key))
            .setOffset(offset.toLong())
            .setLimit(100L)
            .setCountTotal(true)
            .build()

        PageRequest.converter.serialize(request).toList() shouldContainExactly java.toByteArray().toList()

        val decoded = PageRequest.converter.deserialize(java.toByteArray())
        decoded.key.toList() shouldContainExactly key.toList()
        decoded.offset shouldBe offset
        decoded.limit shouldBe 100uL
        decoded.countTotal shouldBe true
    }

    "encodes an all-defaults message as the empty message protobuf-java expects" {
        // proto3 writes nothing for a field at its default, so both sides must produce zero bytes and
        // read those back as a fully defaulted message rather than as a parse failure.
        val encoded = Coin.converter.serialize(Coin())

        encoded.size shouldBe 0
        JavaCoin.Coin.parseFrom(encoded) shouldBe JavaCoin.Coin.getDefaultInstance()
    }
})
