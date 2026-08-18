package kim.jade.kotlinx.protobuf.converter.protobufjs

external interface ProtobufJsMessage {

    fun hasOwnProperty(name: String): Boolean
}

external interface ProtobufJsMap<V> : ProtobufJsMessage

external interface ProtobufJsLong {

    val low: Int

    val high: Int

    val unsigned: Boolean
}

@JsName("Object")
private external object JsObject {

    fun entries(obj: Any): Array<Array<Any?>>
}

fun <T : ProtobufJsMessage> protobufJsMessage(): T = js("({})").unsafeCast<T>()

fun <V> protobufJsMap(): ProtobufJsMap<V> = js("({})").unsafeCast<ProtobufJsMap<V>>()

operator fun <V> ProtobufJsMap<V>.set(key: String, entry: V) {
    asDynamic()[key] = entry
}

fun <V> ProtobufJsMap<V>.entries(): List<Pair<String, V>> =
    JsObject.entries(this).map { it[0].unsafeCast<String>() to it[1].unsafeCast<V>() }

fun ProtobufJsLong.toLong(): Long = (high.toLong() shl 32) or (low.toLong() and 0xFFFFFFFFL)

fun ProtobufJsLong.toULong(): ULong = toLong().toULong()

fun Long.toProtobufJsLong(): ProtobufJsLong = protobufJsLong(this, unsigned = false)

fun ULong.toProtobufJsLong(): ProtobufJsLong = protobufJsLong(toLong(), unsigned = true)

private fun protobufJsLong(bits: Long, unsigned: Boolean): ProtobufJsLong {
    val low = (bits and 0xFFFFFFFFL).toInt()
    val high = (bits ushr 32).toInt()
    val constructor = util.Long

    if (constructor != null) {
        return constructor.fromBits(low, high, unsigned).unsafeCast<ProtobufJsLong>()
    }

    val value = js("({})")
    value.low = low
    value.high = high
    value.unsigned = unsigned

    return value.unsafeCast<ProtobufJsLong>()
}

