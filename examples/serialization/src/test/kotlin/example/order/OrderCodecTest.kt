package example.order

import example.order.v1.LineItem
import example.order.v1.Order
import example.order.v1.Pickup
import example.order.v1.Shipping
import example.order.v1.Status
import example.order.v1.converter
import example.order.v1.parse
import example.order.v1.toAny
import google.protobuf.Timestamp
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import io.kotest.matchers.types.shouldBeInstanceOf
import kim.jade.kotlinx.protobuf.serialization.ProtobufFormat
import kim.jade.kotlinx.protobuf.serialization.ProtobufJsonFormat

/**
 * What the demo prints, as assertions.
 *
 * An example that only compiles proves the generator ran. These check the properties a service actually
 * relies on when it puts one of these messages on a queue: that it comes back the same, that presence
 * survives the trip, and that the JSON form is protobuf's rather than Kotlin's.
 */
class OrderCodecTest : StringSpec({

    fun order() = Order(
        id = "ord_01H8XG",
        customerId = "cus_4412",
        status = Status.STATUS_PAID,
        items = listOf(
            LineItem(sku = "ESPRESSO-1KG", quantity = 2u, unitPriceCents = 2_190uL),
            LineItem(sku = "FILTER-V60", quantity = 1u, unitPriceCents = 1_450uL),
        ),
        labels = mapOf("channel" to "web", "locale" to "en-GB"),
        placedAt = Timestamp(seconds = 1_755_100_800L),
        idempotencyKey = byteArrayOf(0x4a, 0x1f, 0x00, 0x7e),
        totalCents = 5_830uL,
        fulfilment = Order.FulfilmentOneOf.Shipping(
            Shipping(address = "12 Rue Oberkampf, Paris", carrier = "colissimo"),
        ),
    )

    "round-trips every field kind in one message" {
        val decoded = Order.converter.deserialize(Order.converter.serialize(order()))

        // Plain equality is the assertion, including the bytes field: the generator emits equals and
        // hashCode that compare arrays by content, because Kotlin's own would compare them by identity.
        decoded shouldBe order()
        decoded.hashCode() shouldBe order().hashCode()
        decoded.items shouldContainExactly order().items
        decoded.labels shouldBe mapOf("channel" to "web", "locale" to "en-GB")
        decoded.totalCents shouldBe 5_830uL
        decoded.placedAt?.seconds shouldBe 1_755_100_800L
        decoded.idempotencyKey.toList() shouldContainExactly listOf<Byte>(0x4a, 0x1f, 0x00, 0x7e)
    }

    "keeps absent and empty apart on a field with presence" {
        // couponCode is `optional string` — explicit presence — so these are two different messages and
        // the wire has to say which one it is.
        val absent = order()
        val empty = order().copy(couponCode = "")

        absent shouldNotBe empty
        // The empty coupon costs two bytes — a tag and a zero length — that the absent one does not.
        Order.converter.serialize(absent).size shouldBeLessThan Order.converter.serialize(empty).size

        Order.converter.deserialize(Order.converter.serialize(absent)).couponCode shouldBe null
        Order.converter.deserialize(Order.converter.serialize(empty)).couponCode shouldBe ""
    }

    "writes nothing for a message that is entirely at its defaults" {
        // Every field of a LineItem has implicit presence, so its default value is its absence and the
        // encoder has nothing left to say.
        LineItem.converter.serialize(LineItem()).size shouldBe 0

        // So does an empty Order. `placed_at` is a singular message and therefore has presence, so an
        // unset one is absent rather than an empty submessage, and absence is not written.
        Order.converter.serialize(Order()).size shouldBe 0
        Order.converter.deserialize(Order.converter.serialize(Order())) shouldBe Order()

        // Which is exactly what makes the empty submessage sayable. A `placed_at` that is present and
        // entirely at its own defaults costs the two bytes the absent one does not — a tag and a zero
        // length — and comes back as something other than absent.
        val epoch = Order().copy(placedAt = Timestamp())

        Order.converter.serialize(epoch).size shouldBe 2
        Order.converter.deserialize(Order.converter.serialize(epoch)) shouldBe epoch
        epoch shouldNotBe Order()
    }

    "round-trips each branch of the fulfilment one-of, including neither" {
        val pickup = order().copy(
            fulfilment = Order.FulfilmentOneOf.Pickup(Pickup(storeId = "store_7")),
        )
        val drafting = order().copy(fulfilment = null)

        Order.converter.deserialize(Order.converter.serialize(pickup))
            .fulfilment.shouldBeInstanceOf<Order.FulfilmentOneOf.Pickup>()
        // Null is a state of its own, not a stand-in for the first branch: an order being drafted has
        // chosen no fulfilment, and defaulting to Shipping would invent an address nobody typed.
        Order.converter.deserialize(Order.converter.serialize(drafting)).fulfilment shouldBe null
    }

    "encodes JSON the way protobuf does, not the way kotlinx would" {
        val json = ProtobufJsonFormat().encodeToString(Order.KotlinxSerializer, order())

        // snake_case in the schema, camelCase on the wire — protobuf's JSON mapping, and what every
        // other language's protobuf JSON produces for this message.
        json shouldContain "\"customerId\""
        json shouldNotContain "customer_id"
        // uint64 as a string, so a JSON reader with doubles for numbers cannot silently round it.
        json shouldContain "\"totalCents\": \"5830\""
        // Enums by name, bytes as base64, timestamps as RFC 3339.
        json shouldContain "\"STATUS_PAID\""
        json shouldContain "\"idempotencyKey\": \"Sh8Afg==\""
        json shouldContain "2025-08-13T16:00:00Z"
        // An absent optional field is left out entirely rather than emitted as null.
        json shouldNotContain "couponCode"

        ProtobufJsonFormat().decodeFromString(Order.KotlinxSerializer, json) shouldBe order()
    }

    "is far smaller as protobuf than as JSON" {
        val binary = Order.converter.serialize(order())
        val json = ProtobufJsonFormat().encodeToString(Order.KotlinxSerializer, order())

        // The point of the binary form: field numbers instead of names, varints instead of digits. The
        // ratio here is about 3x, and grows with how repetitive the field names are.
        binary.size shouldBeLessThan json.length / 2
    }

    "agrees with kotlinx.serialization's binary format" {
        // kotlinxSerialization() annotates each type with a serializer that hands off to the converter
        // when the encoder is one of this project's, so a build already using kotlinx formats can keep
        // using them without a second encoding of the same message.
        val viaFormat = ProtobufFormat().encodeToByteArray(Order.KotlinxSerializer, order())

        viaFormat.toList() shouldContainExactly Order.converter.serialize(order()).toList()
    }

    "packs into Any for storing next to messages of other types" {
        val any = order().toAny()

        any.typeUrl shouldBe "type.googleapis.com/example.order.v1.Order"
        Order.parse(any) shouldBe order()
    }
})
