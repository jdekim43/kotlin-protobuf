package example.events

import example.events.v1.Envelope
import example.events.v1.OrderPlaced
import example.events.v1.PaymentFailed
import example.events.v1.TypeRegistry
import example.events.v1.UserRegistered
import example.events.v1.toAny
import google.protobuf.Any
import google.protobuf.Timestamp

/**
 * A producer writing three unrelated events onto one stream, and a consumer reading them back.
 *
 * Run it with `./gradlew :examples:event-envelope:run`.
 */
fun main() {
    val stream: List<ByteArray> = listOf(
        Envelope(
            eventId = "evt_1",
            subject = "usr_7",
            occurredAt = Timestamp(seconds = 1_755_100_800L),
            payload = UserRegistered(userId = "usr_7", email = "amelie@example.com").toAny(),
            headers = mapOf("producer" to "accounts", "trace-id" to "9f2c"),
        ),
        Envelope(
            eventId = "evt_2",
            subject = "ord_31",
            occurredAt = Timestamp(seconds = 1_755_100_930L),
            payload = OrderPlaced(orderId = "ord_31", userId = "usr_7", totalCents = 5_830uL).toAny(),
        ),
        Envelope(
            eventId = "evt_3",
            subject = "ord_31",
            occurredAt = Timestamp(seconds = 1_755_101_112L),
            payload = PaymentFailed(orderId = "ord_31", reason = "card_declined", attempt = 2u).toAny(),
        ),
        // Published by a service that was deployed after this consumer. Nothing here knows the type.
        Envelope(
            eventId = "evt_4",
            subject = "ord_31",
            occurredAt = Timestamp(seconds = 1_755_101_400L),
            payload = Any(
                typeUrl = "type.googleapis.com/example.events.v1.ShipmentDelayed",
                value = byteArrayOf(0x0a, 0x06, 0x6f, 0x72, 0x64, 0x5f, 0x33, 0x31),
            ),
        ),
    ).map { it.encode() }

    // The consumer only ever sees bytes. It decodes the envelope, and the payload not at all until it
    // has recognised the type.
    println("consuming ${stream.size} envelopes")
    for (bytes in stream) {
        val envelope = decodeEnvelope(bytes)
        val outcome = handle(envelope)
        val known = if (isKnownType(envelope.payload)) "known" else "unknown to this build"

        val typeName = envelope.payload?.typeUrl?.substringAfterLast('/') ?: "no payload"

        println("  ${envelope.eventId}  $typeName ($known)")
        when (outcome) {
            is Outcome.Handled -> println("    → ${outcome.summary}")
            is Outcome.DeadLettered -> println("    → dead-lettered: ${outcome.typeUrl}")
            is Outcome.Malformed -> println("    → rejected: envelope carried no payload")
        }
    }

    // The routing fields are readable without touching the payload at all — which is what lets a broker,
    // a fan-out worker or an audit log handle events whose schemas it was never compiled against.
    val first = decodeEnvelope(stream.first())
    println()
    println("routing without decoding the payload")
    println("  subject : ${first.subject}")
    println("  headers : ${first.headers}")
    println("  payload : ${first.payload?.value?.size ?: 0} opaque bytes")

    // The generated registry, which is what "unknown to this build" above was decided against.
    println()
    println("types this build was generated with")
    TypeRegistry.messages.forEach { (typeUrl, type) ->
        println("  $typeUrl → ${type.simpleName}")
    }
}
