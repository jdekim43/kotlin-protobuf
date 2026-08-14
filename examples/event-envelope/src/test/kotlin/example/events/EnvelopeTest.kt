package example.events

import example.events.v1.Envelope
import example.events.v1.OrderPlaced
import example.events.v1.PaymentFailed
import example.events.v1.TypeRegistry
import example.events.v1.UserRegistered
import example.events.v1.converter
import example.events.v1.parse
import example.events.v1.toAny
import google.protobuf.Any
import google.protobuf.Timestamp
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf

/**
 * The envelope, from the producer's side and the consumer's.
 *
 * The property worth pinning down is the one the pattern exists for: an envelope carrying a payload the
 * reader has never heard of still parses, still routes, and can still be parked — rather than failing
 * the stream for everybody behind it.
 */
class EnvelopeTest : StringSpec({

    fun envelope(payload: Any, eventId: String = "evt_1") = Envelope(
        eventId = eventId,
        subject = "ord_31",
        occurredAt = Timestamp(seconds = 1_755_100_800L),
        payload = payload,
        headers = mapOf("producer" to "accounts"),
    )

    "carries any of the three events over the same envelope" {
        val events = listOf(
            UserRegistered(userId = "usr_7", email = "amelie@example.com").toAny(),
            OrderPlaced(orderId = "ord_31", userId = "usr_7", totalCents = 5_830uL).toAny(),
            PaymentFailed(orderId = "ord_31", reason = "card_declined", attempt = 2u).toAny(),
        )

        val outcomes = events
            .map { decodeEnvelope(envelope(it).encode()) }
            .map { handle(it) }

        outcomes.map { it.shouldBeInstanceOf<Outcome.Handled>().summary } shouldContainExactly listOf(
            "send a welcome mail to amelie@example.com",
            "reserve stock for ord_31 (5830 cents)",
            "retry ord_31, attempt 2: card_declined",
        )
    }

    "keeps the payload opaque, so routing needs no knowledge of it" {
        val event = OrderPlaced(orderId = "ord_31", userId = "usr_7", totalCents = 5_830uL)

        val decoded = decodeEnvelope(envelope(event.toAny()).encode())

        // The routing fields are readable on their own…
        decoded.subject shouldBe "ord_31"
        decoded.headers shouldBe mapOf("producer" to "accounts")
        // …and the payload is exactly the bytes the producer's converter wrote, nothing added.
        decoded.payload?.value?.toList() shouldContainExactly
            OrderPlaced.converter.serialize(event).toList()
        decoded.payload?.typeUrl shouldBe OrderPlaced.TYPE_URL
    }

    "parses an envelope holding a type this build was never generated with" {
        // Published by a service deployed after this one. The envelope is a message like any other; only
        // its payload is unreadable here.
        val future = Any(
            typeUrl = "type.googleapis.com/example.events.v1.ShipmentDelayed",
            value = byteArrayOf(0x0a, 0x02, 0x68, 0x69),
        )

        val decoded = decodeEnvelope(envelope(future, eventId = "evt_4").encode())

        decoded.eventId shouldBe "evt_4"
        decoded.subject shouldBe "ord_31"
        isKnownType(decoded.payload) shouldBe false
        handle(decoded) shouldBe Outcome.DeadLettered(
            "type.googleapis.com/example.events.v1.ShipmentDelayed",
        )
    }

    "refuses to decode a payload as the wrong type" {
        val any = PaymentFailed(orderId = "ord_31", reason = "card_declined").toAny()

        // The bytes would happily decode as an OrderPlaced — both start with a string at field 1 — and
        // the result would be a plausible message that never existed. The type URL is what stops it.
        val failure = runCatching { OrderPlaced.parse(any) }.exceptionOrNull()

        failure.shouldNotBeNull()
        failure.message shouldContain OrderPlaced.TYPE_URL
    }

    "names every type this build was generated with" {
        // typeRegistry() on kotlin() emits this map. It is what tells "a payload this build will never
        // understand" from "one it simply does not handle" — and the type URLs in it are the same strings
        // TYPE_URL puts into an Any, prefix included.
        TypeRegistry.messages[UserRegistered.TYPE_URL] shouldBe UserRegistered::class
        TypeRegistry.messages[OrderPlaced.TYPE_URL] shouldBe OrderPlaced::class
        TypeRegistry.messages[PaymentFailed.TYPE_URL] shouldBe PaymentFailed::class

        UserRegistered.TYPE_URL shouldBe "type.googleapis.com/example.events.v1.UserRegistered"
    }
})
