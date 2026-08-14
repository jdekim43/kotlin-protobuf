# event-envelope

**An event stream whose payload type is not known until it arrives.**

One topic carries `UserRegistered`, `OrderPlaced` and `PaymentFailed`, and the schema language has no
supertype to give them. `google.protobuf.Any` — a type URL and the encoded bytes — is what stands in for
the polymorphism, so the envelope stays one message and the consumer decides what to decode.

- [`events.proto`](src/main/proto/example/events/v1/events.proto) — the envelope and the three events.
- [`Bus.kt`](src/main/kotlin/example/events/Bus.kt) — the consumer: dispatch on the type URL, dead-letter
  what this build does not know, reject what carries no payload at all.
- [`Demo.kt`](src/main/kotlin/example/events/Demo.kt) — a producer writing four envelopes, one of them
  holding a type this build has never heard of.
- [`EnvelopeTest.kt`](src/test/kotlin/example/events/EnvelopeTest.kt) — each of those outcomes as an
  assertion, including the payload decoded as the wrong type.

```bash
./gradlew :examples:event-envelope:run
./gradlew :examples:event-envelope:test
```

## What it shows

**Packing and unpacking.** `event.toAny()` takes the type URL from the message's own `TYPE_URL`, so the
publisher does not name the type twice. `OrderPlaced.parse(any)` checks the URL before decoding and
throws if the envelope holds something else — the bytes of a `PaymentFailed` would decode as an
`OrderPlaced` perfectly happily, and the type URL is what stops that.

**Routing without decoding.** The envelope's own fields — subject, headers, timestamp — are readable while
the payload stays opaque bytes. That is what lets a broker, a fan-out worker or an audit log handle events
whose schemas it was never compiled against.

**An unknown payload is not a poison message.** The envelope still parses; only its payload is unreadable,
so it can be parked instead of failing the stream for everything behind it.

**No payload is a third state, not an unknown type.** `payload` is a singular message, so it has presence
and maps to `Any?`. An envelope that omits it is absent rather than an empty `Any` with a blank type URL,
which is why `handle` can answer `Malformed` for a broken producer and `DeadLettered` for a newer one —
two different operational problems that a non-null mapping would have collapsed into one.

**The generated registry.** `typeRegistry("example.events.v1.TypeRegistry")` emits `typeUrl → KClass` for
every message in the compilation. It answers *which* type an envelope holds — useful for telling "I will
never understand this" from "I do not handle this" — but there is no converter behind a `KClass`, so it
does not decode anything.

To write an envelope as JSON, the payload needs a descriptor: add `jvmTypeRegistry(…)` to `converterJvm()`
and swap `kotlin()` for `kotlinxSerialization()`, and `ProtobufJsonFormat(JvmTypeRegistry.messages)` prints
the payload inline with an `"@type"` member beside it. That is the only thing here that would need the
serializers, so this module does without both. See [examples/serialization](../serialization) for the JSON
side.

## One thing worth knowing

`typeUrlPrefix` is part of the value both ends compare. These protos are generated with
`type.googleapis.com`, so a producer generated without a prefix writes `/example.events.v1.OrderPlaced`
and every `when` branch here misses. Pick one for the whole system; see the note in the
[root README](../../README.md#generator-options).
