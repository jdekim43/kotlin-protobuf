# serialization

**One schema, compact bytes, and JSON when a human has to read it.**

The oldest reason to reach for protobuf: a record that outlives the process that wrote it — put on a
queue, cached, written to a column, sent to a service in another language — and needs a definition both
ends agree on.

- [`order.proto`](src/main/proto/example/order/v1/order.proto) is the definition: an enum, a repeated
  message, a `map`, a `oneof`, `bytes`, `uint64`, a `Timestamp`, and one `optional` field.
- [`Demo.kt`](src/main/kotlin/example/order/Demo.kt) writes an order out and reads it back.
- [`OrderCodecTest.kt`](src/test/kotlin/example/order/OrderCodecTest.kt) asserts what the demo prints.

```bash
./gradlew :examples:serialization:run
./gradlew :examples:serialization:test
```

## What it shows

**The mapping.** `uint64` is `ULong`, `bytes` is `ByteArray` compared by content, a `oneof` is a nullable
`sealed interface`, `map` is `Map`. A message with a `bytes` field carries generated `equals`/`hashCode`,
so two orders with identical bytes are equal and usable as `Map` keys.

**Presence.** `coupon_code` is `optional string`, so it maps to `String?` and null means the sender did
not set it. Every other *scalar* has implicit presence: absent and default are the same fact, they cost no
bytes, and `""` is what a reader sees either way. This is the field-by-field decision that protobuf makes
you take, and the demo prints the two-byte difference it makes on the wire.

**Both formats from one type.** `kotlinxSerialization()` annotates each message with a serializer that
delegates to the converter, so `Order.converter.serialize(…)` and
`ProtobufFormat().encodeToByteArray(Order.KotlinxSerializer, …)` produce the same bytes — and
`ProtobufJsonFormat` produces protobuf's JSON mapping rather than kotlinx's defaults: camelCase names,
`uint64` as a string, bytes as base64, enums by name, absent optionals omitted.

**What the sizes look like.** The order in the demo is 152 bytes of protobuf and 501 of JSON.

## One thing worth knowing

Presence is not only a scalar question. `placed_at` is a *message* field, and a singular message has
presence in every syntax, so it maps to `Timestamp?` — `Order()` is zero bytes, and a `placed_at` set to
`Timestamp()` is two. The demo prints both. "Never set it" and "set it to the epoch" are different facts
here, the same way `coupon_code` keeps absent and `""` apart.
