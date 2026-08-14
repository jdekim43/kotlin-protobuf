package example.order

import example.order.v1.LineItem
import example.order.v1.Order
import example.order.v1.Shipping
import example.order.v1.Status
import example.order.v1.converter
import google.protobuf.Timestamp
import kim.jade.kotlinx.protobuf.serialization.ProtobufJsonFormat

/**
 * Writes an order out and reads it back.
 *
 * Run it with `./gradlew :examples:serialization:run`. Nothing here mentions a field name twice: the
 * schema names them, the generator turns them into a `data class`, and the converter is what puts them
 * on the wire.
 */
fun main() {
    val order = Order(
        id = "ord_01H8XG",
        customerId = "cus_4412",
        status = Status.STATUS_PAID,
        items = listOf(
            LineItem(sku = "ESPRESSO-1KG", quantity = 2u, unitPriceCents = 2_190uL),
            LineItem(sku = "FILTER-V60", quantity = 1u, unitPriceCents = 1_450uL),
        ),
        // Absent, not empty. See below for why that distinction is the field's whole point.
        couponCode = null,
        labels = mapOf("channel" to "web", "locale" to "en-GB"),
        placedAt = Timestamp(seconds = 1_755_100_800L),
        idempotencyKey = byteArrayOf(0x4a, 0x1f, 0x00, 0x7e.toByte()),
        totalCents = 5_830uL,
        fulfilment = Order.FulfilmentOneOf.Shipping(
            Shipping(address = "12 Rue Oberkampf, Paris", carrier = "colissimo"),
        ),
    )

    // The binary encoding. This is what goes on a queue, into a cache, or into a bytes column.
    val bytes = Order.converter.serialize(order)
    val decoded = Order.converter.deserialize(bytes)

    // protobuf's JSON mapping — the same message when a human, a log or a REST client has to read it.
    // Note what it does that a plain Kotlin JSON serializer would not: camelCase field names from the
    // schema's snake_case, uint64 as a string so a JavaScript reader cannot lose precision, bytes as
    // base64, the enum by name.
    val json = ProtobufJsonFormat().encodeToString(Order.KotlinxSerializer, order)

    println("binary : ${bytes.size} bytes")
    println("json   : ${json.length} bytes")
    println("json   : $json")
    println()
    println("round-trips unchanged : ${decoded == order}")
    println("coupon after decoding : ${decoded.couponCode}")

    // Presence, the reason `optional` is in the schema. An implicit-presence field cannot tell "unset"
    // from "set to the type's default", so protobuf writes neither — and the two encodings differ only
    // for the field that has presence.
    val withEmptyCoupon = order.copy(couponCode = "")
    println()
    println("no coupon    : ${Order.converter.serialize(order).size} bytes, couponCode=${order.couponCode}")
    println("empty coupon : ${Order.converter.serialize(withEmptyCoupon).size} bytes, couponCode=\"${withEmptyCoupon.couponCode}\"")

    // A message whose every field is at its default is (almost) nothing on the wire: absent and default
    // are the same fact for a field without presence, so there is nothing to write down.
    println()
    println("empty line item : ${LineItem.converter.serialize(LineItem()).size} bytes")
    // Zero as well, for a different reason. `placed_at` is a *message* field, so it has presence and maps
    // to `Timestamp?` — an unset one is absent, and absence is not written down.
    println("empty order     : ${Order.converter.serialize(Order()).size} bytes")
    // Which is what makes the other state sayable. A `placed_at` that is present and entirely at its own
    // defaults is two bytes the absent one does not spend: one tag byte and a zero length.
    println("epoch placed_at : ${Order.converter.serialize(Order(placedAt = Timestamp())).size} bytes")
}
