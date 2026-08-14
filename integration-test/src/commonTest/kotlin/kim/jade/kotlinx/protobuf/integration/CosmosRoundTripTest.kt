package kim.jade.kotlinx.protobuf.integration

import cosmos.bank.v1beta1.MsgSend
import cosmos.bank.v1beta1.converter
import cosmos.base.query.v1beta1.PageRequest
import cosmos.base.query.v1beta1.converter
import cosmos.base.v1beta1.Coin
import cosmos.base.v1beta1.converter
import cosmos.base.v1beta1.parse
import cosmos.base.v1beta1.toAny
import cosmos.tx.signing.v1beta1.SignMode
import cosmos.tx.v1beta1.ModeInfo
import cosmos.tx.v1beta1.converter
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import tendermint.crypto.PublicKey
import tendermint.crypto.converter

/**
 * The generated types serialize and come back unchanged.
 *
 * These messages are picked for what they cover rather than for what they mean: [PageRequest] is the
 * only cosmos message that puts `uint64`, `bytes` and `bool` in one place, [MsgSend] nests a repeated
 * message, and [ModeInfo] is a `oneof`. Between them they hit every mapping that is not a plain string.
 */
class CosmosRoundTripTest : StringSpec({

    "round-trips a flat message" {
        val coin = Coin(denom = "uatom", amount = "1000000")

        val decoded = Coin.converter.deserialize(Coin.converter.serialize(coin))

        decoded shouldBe coin
    }

    "round-trips an empty message" {
        // Every field at its proto3 default encodes to no bytes at all, and has to come back as itself.
        val empty = Coin()

        val encoded = Coin.converter.serialize(empty)

        encoded.size shouldBe 0
        Coin.converter.deserialize(encoded) shouldBe empty
    }

    "round-trips a repeated field of nested messages" {
        val message = MsgSend(
            fromAddress = "cosmos1sender",
            toAddress = "cosmos1recipient",
            amount = listOf(Coin("uatom", "100"), Coin("uosmo", "250")),
        )

        val decoded = MsgSend.converter.deserialize(MsgSend.converter.serialize(message))

        decoded shouldBe message
        decoded.amount shouldContainExactly message.amount
    }

    "round-trips uint64, bytes and bool" {
        val request = PageRequest(
            key = byteArrayOf(1, 2, 3, -1, 0, 127),
            // Past Long.MAX_VALUE on purpose: a uint64 maps to ULong, and anything that reads it as a
            // signed long would come back negative.
            offset = ULong.MAX_VALUE - 1uL,
            limit = 100uL,
            countTotal = true,
            reverse = false,
        )

        val decoded = PageRequest.converter.deserialize(PageRequest.converter.serialize(request))

        // A message holding a ByteArray compares by content, so plain equality is the assertion — the
        // generator emits equals/hashCode for exactly this reason.
        decoded shouldBe request
        decoded.hashCode() shouldBe request.hashCode()
        decoded.key.toList() shouldContainExactly request.key.toList()
        decoded.offset shouldBe request.offset
    }

    "compares messages holding bytes by content" {
        // The defect this guards: Kotlin's data class equals compares arrays by identity, so without
        // generated equals/hashCode these two would differ, and would land in different buckets of a Set.
        val a = PageRequest(key = byteArrayOf(1, 2, 3), limit = 10uL)
        val b = PageRequest(key = byteArrayOf(1, 2, 3), limit = 10uL)

        a shouldBe b
        a.hashCode() shouldBe b.hashCode()
        setOf(a, b).size shouldBe 1

        PageRequest(key = byteArrayOf(1, 2, 4), limit = 10uL) shouldNotBe a
    }

    "round-trips each branch of a oneof" {
        val single = ModeInfo(ModeInfo.SumOneOf.Single(ModeInfo.Single(SignMode.SIGN_MODE_DIRECT)))
        val multi = ModeInfo(ModeInfo.SumOneOf.Multi(ModeInfo.Multi(modeInfos = listOf(single))))

        withClue("the Single branch") {
            val decoded = ModeInfo.converter.deserialize(ModeInfo.converter.serialize(single))
            decoded.sum.shouldBeInstanceOf<ModeInfo.SumOneOf.Single>()
            decoded shouldBe single
        }

        withClue("the Multi branch, which carries the Single one inside it") {
            // Multi holds a CompactBitArray, whose elems is a ByteArray — so this also covers content
            // equality reaching through a nested message and a one-of branch.
            val decoded = ModeInfo.converter.deserialize(ModeInfo.converter.serialize(multi))
            decoded.sum.shouldBeInstanceOf<ModeInfo.SumOneOf.Multi>()
            decoded shouldBe multi
        }
    }

    "round-trips a one-of that was never set" {
        // A one-of has a state beyond its branches, and it is the one a default-constructed message is
        // in. Defaulting to the first branch instead would invent a value the sender never wrote, put it
        // on the wire on the way back out, and — on the way in — leave a message whose one-of is absent
        // with no case to decode into at all.
        val unset = ModeInfo()

        unset.sum shouldBe null

        val encoded = ModeInfo.converter.serialize(unset)

        encoded.size shouldBe 0
        ModeInfo.converter.deserialize(encoded) shouldBe unset
    }

    "compares a one-of branch wrapping bytes by content" {
        // Such a branch is generated as a data class rather than a @JvmInline value class: a value class
        // may not declare equals, so it could only ever compare its array by identity.
        val a = PublicKey(PublicKey.SumOneOf.Ed25519(byteArrayOf(9, 8, 7)))
        val b = PublicKey(PublicKey.SumOneOf.Ed25519(byteArrayOf(9, 8, 7)))

        a shouldBe b
        a.hashCode() shouldBe b.hashCode()
        PublicKey.converter.deserialize(PublicKey.converter.serialize(a)) shouldBe a
    }

    "gives an enum a TYPE_URL and looks values up by number" {
        SignMode.TYPE_URL shouldBe "/cosmos.tx.signing.v1beta1.SignMode"

        // Sparse on purpose: cosmos numbers these 0, 1, 3, 127, 191, so forNumber cannot be an index.
        SignMode.forNumber(0) shouldBe SignMode.SIGN_MODE_UNSPECIFIED
        SignMode.forNumber(127) shouldBe SignMode.SIGN_MODE_LEGACY_AMINO_JSON
        SignMode.entries.first() shouldBe SignMode.SIGN_MODE_UNSPECIFIED

        // An enum field defaults to the first declared value, which proto3 requires to be number 0.
        ModeInfo.Single().mode shouldBe SignMode.SIGN_MODE_UNSPECIFIED
    }

    "packs into Any and back out" {
        val coin = Coin(denom = "uatom", amount = "42")

        val any = coin.toAny()

        any.typeUrl shouldBe Coin.TYPE_URL
        // No prefix: that is what a Cosmos node puts in an Any, and what the chain expects back.
        any.typeUrl shouldBe "/cosmos.base.v1beta1.Coin"
        // No converter argument, and no ambiguity: parse hangs off the message's companion, so the four
        // messages coin.proto declares get four entry points that cannot collide.
        Coin.parse(any) shouldBe coin
    }

    "refuses to unpack an Any holding a different type" {
        val any = Coin("uatom", "42").toAny().copy(typeUrl = MsgSend.TYPE_URL)

        val failure = runCatching { Coin.parse(any) }.exceptionOrNull()

        failure.shouldBeInstanceOf<IllegalStateException>()
        failure?.message shouldContain Coin.TYPE_URL
    }
})
