package example.events

import example.events.v1.Envelope
import example.events.v1.OrderPlaced
import example.events.v1.PaymentFailed
import example.events.v1.TypeRegistry
import example.events.v1.UserRegistered
import example.events.v1.converter
import example.events.v1.parse
import google.protobuf.Any

/** What a consumer did with an envelope. */
sealed interface Outcome {
    data class Handled(val summary: String) : Outcome

    /** The payload named a type this build was not generated with — park it, do not fail the stream. */
    data class DeadLettered(val typeUrl: String) : Outcome

    /** The envelope carried no payload at all, which no producer of this schema should ever send. */
    object Malformed : Outcome
}

/**
 * The consumer side: decide from the type URL, decode once the type is known.
 *
 * `parse` hangs off each message's companion and checks the URL before decoding, so the branch and the
 * decode cannot disagree — asking `OrderPlaced` to parse a `PaymentFailed` throws rather than handing
 * back a message made of misread bytes.
 */
fun handle(envelope: Envelope): Outcome {
    // `payload` is a singular message and so carries presence: an envelope that omits it is absent, not
    // an empty `Any` with a blank type URL. That is a malformed producer rather than an unknown type, and
    // the two deserve different handling — so they are told apart here instead of both falling through to
    // the dead-letter branch.
    val payload = envelope.payload ?: return Outcome.Malformed

    return when (payload.typeUrl) {
        UserRegistered.TYPE_URL -> UserRegistered.parse(payload).let {
            Outcome.Handled("send a welcome mail to ${it.email}")
        }

        OrderPlaced.TYPE_URL -> OrderPlaced.parse(payload).let {
            Outcome.Handled("reserve stock for ${it.orderId} (${it.totalCents} cents)")
        }

        PaymentFailed.TYPE_URL -> PaymentFailed.parse(payload).let {
            Outcome.Handled("retry ${it.orderId}, attempt ${it.attempt}: ${it.reason}")
        }

        // A newer producer publishing an event this build has never heard of. The envelope still parsed —
        // that is the whole point of keeping the payload opaque — so there is something to park.
        else -> Outcome.DeadLettered(payload.typeUrl)
    }
}

/**
 * Whether this build knows the type at all, from the generated registry.
 *
 * `TypeRegistry` is `typeUrl → KClass`, so it answers *which* type an envelope holds and not how to
 * decode it — there is no converter behind a `KClass`. It is worth having anyway: a router can tell a
 * payload it will never understand from one it merely does not handle, and log the difference.
 */
fun isKnownType(payload: Any?): Boolean = payload?.typeUrl in TypeRegistry.messages

/** Serializing an envelope is one call; the payload is already bytes by the time it gets here. */
fun Envelope.encode(): ByteArray = Envelope.converter.serialize(this)

fun decodeEnvelope(bytes: ByteArray): Envelope = Envelope.converter.deserialize(bytes)
