package kim.jade.kotlinx.protobuf.converter.protobufjs

import kim.jade.kotlinx.protobuf.util.toByteArray
import kim.jade.kotlinx.protobuf.util.toUint8Array
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.abs
import kotlin.math.floor

internal object WellKnownJson {

    private const val NANOS_PER_SECOND = 1_000_000_000
    private const val NANOS_PER_MILLI = 1_000_000
    private const val NANOS_PER_MICRO = 1_000

    private var registered = false

    fun register() {
        if (registered) {
            return
        }

        registered = true

        put(".google.protobuf.Timestamp", ::timestampFromJson, ::timestampToJson)
        put(".google.protobuf.Duration", ::durationFromJson, ::durationToJson)
        put(".google.protobuf.FieldMask", ::fieldMaskFromJson, ::fieldMaskToJson)
        put(".google.protobuf.Struct", ::structFromJson, ::structToJson)
        put(".google.protobuf.Value", ::valueFromJson, ::valueToJson)
        put(".google.protobuf.ListValue", ::listValueFromJson, ::listValueToJson)

        putWrapper(".google.protobuf.DoubleValue", ::identity, ::identity)
        putWrapper(".google.protobuf.FloatValue", ::identity, ::identity)
        putWrapper(".google.protobuf.Int64Value", ::int64FromJson, ::int64ToJson)
        putWrapper(".google.protobuf.UInt64Value", ::uint64FromJson, ::uint64ToJson)
        putWrapper(".google.protobuf.Int32Value", ::identity, ::identity)
        putWrapper(".google.protobuf.UInt32Value", ::identity, ::identity)
        putWrapper(".google.protobuf.BoolValue", ::identity, ::identity)
        putWrapper(".google.protobuf.StringValue", ::identity, ::identity)
        putWrapper(".google.protobuf.BytesValue", ::bytesFromJson, ::bytesToJson)

        putAny()
    }

    private const val ANY = ".google.protobuf.Any"

    private fun putAny() {
        val wrapper: dynamic = js("({})")

        wrapper.toObject = RECEIVING_TYPE { type, message, options -> anyToObject(type, message, options) }
        wrapper.fromObject = PASS_THROUGH_FROM_OBJECT

        wrappers[ANY] = wrapper
    }

    private val RECEIVING_TYPE: ((dynamic, dynamic, dynamic) -> dynamic) -> dynamic =
        js("(function (convert) { return function (message, options) { return convert(this, message, options); }; })")
            .unsafeCast<((dynamic, dynamic, dynamic) -> dynamic) -> dynamic>()

    private fun hasSpecialForm(fullName: String): Boolean = fullName == ANY || fullName in structural

    private fun lookupPacked(anyType: dynamic, typeUrl: String): dynamic {
        val fullName = typeUrl.substringAfterLast('/')
        val fromClosure: dynamic = anyType.lookup(fullName)

        if (fromClosure != null && jsTypeOf(fromClosure.fieldsArray) != "undefined") {
            return fromClosure
        }

        return ProtobufJsTypeRegistry.lookup(fullName)
            ?: throw IllegalStateException("Cannot find type for url: $typeUrl")
    }

    private fun anyToObject(anyType: dynamic, message: dynamic, options: dynamic): dynamic {
        val typeUrl = message.typeUrl as? String

        if (typeUrl.isNullOrEmpty()) {
            return message
        }

        val packed = lookupPacked(anyType, typeUrl)
        val json = packed.toObject(packed.decode(message.value), options)

        val result: dynamic = js("({})")
        result["@type"] = typeUrl

        if (hasSpecialForm(packed.fullName as String)) {
            result["value"] = json
        } else {
            for ((key, value) in json.unsafeCast<ProtobufJsMap<dynamic>>().entries()) {
                result[key] = value
            }
        }

        return result
    }

    private fun anyFromJson(anyType: dynamic, json: dynamic): dynamic {
        val typeUrl = json["@type"] as? String ?: return json
        val packed = lookupPacked(anyType, typeUrl)

        val payload: dynamic = if (hasSpecialForm(packed.fullName as String)) {
            json["value"]
        } else {
            val fields: dynamic = js("({})")
            for ((key, value) in json.unsafeCast<ProtobufJsMap<dynamic>>().entries()) {
                if (key != "@type") {
                    fields[key] = value
                }
            }
            fields
        }

        val message: dynamic = js("({})")
        message.typeUrl = typeUrl
        message.value = packed.encode(packed.fromObject(structuralMessage(packed, payload))).finish()

        return message
    }

    private val structural = mutableMapOf<String, (dynamic) -> dynamic>()

    private fun put(name: String, fromJson: (dynamic) -> dynamic, toJson: (dynamic) -> dynamic) {
        structural[name] = fromJson

        val wrapper: dynamic = js("({})")

        wrapper.toObject = { message: dynamic, _: dynamic -> toJson(message) }

        wrapper.fromObject = PASS_THROUGH_FROM_OBJECT

        wrappers[name] = wrapper
    }

    fun structuralJson(type: Type, json: dynamic): dynamic {
        register()
        type.resolveAll()

        return structuralMessage(type, json)
    }

    private fun structuralMessage(type: dynamic, json: dynamic): dynamic {
        if (json == null) {
            return null
        }

        if (type.fullName == ANY) {
            return anyFromJson(type, json)
        }

        structural[type.fullName as String]?.let { return it(json) }

        val result: dynamic = js("({})")

        for (field in type.fieldsArray.unsafeCast<Array<Field>>()) {
            val value = json[field.name]

            if (value != null) {
                result[field.name] = structuralField(field, value)
            }
        }

        return result
    }

    private fun structuralField(field: Field, value: dynamic): dynamic {
        val type: dynamic = field.resolvedType

        if (type == null || jsTypeOf(type.fieldsArray) == "undefined") {
            return value
        }

        return when {
            field.map -> {
                val entries: dynamic = js("({})")
                for ((key, entry) in value.unsafeCast<ProtobufJsMap<dynamic>>().entries()) {
                    entries[key] = structuralMessage(type, entry)
                }
                entries
            }

            field.repeated ->
                value.unsafeCast<Array<dynamic>>().map { structuralMessage(type, it) }.toTypedArray()

            else -> structuralMessage(type, value)
        }
    }

    private val PASS_THROUGH_FROM_OBJECT: dynamic =
        js("(function(object) { return this.fromObject(object); })")

    private fun putWrapper(name: String, fromJson: (dynamic) -> dynamic, toJson: (dynamic) -> dynamic) {
        put(
            name,
            fromJson = { json ->
                val message: dynamic = js("({})")
                message.value = fromJson(json)
                message
            },
            toJson = { message -> toJson(message.value) },
        )
    }

    private fun identity(value: dynamic): dynamic = value

    private fun timestampToJson(message: dynamic): dynamic {
        val seconds = readInt64(message.seconds)
        val nanos = readInt32(message.nanos)
        val printed = JsDate(seconds.toDouble() * 1000.0).toISOString()

        return printed.substring(0, printed.lastIndexOf('.')) + fractionOf(nanos) + "Z"
    }

    private fun timestampFromJson(json: dynamic): dynamic {
        val text = json as String
        val milliseconds = JsDate.parse(text)

        require(!milliseconds.isNaN()) { "Not an RFC 3339 timestamp: $text" }

        val message: dynamic = js("({})")
        message.seconds = floor(milliseconds / 1000.0).toLong().toProtobufJsLong()
        message.nanos = nanosOf(FRACTION.find(text)?.groupValues?.get(1).orEmpty())

        return message
    }

    private fun durationToJson(message: dynamic): dynamic {
        val seconds = readInt64(message.seconds)
        val nanos = readInt32(message.nanos)
        val sign = if (seconds < 0 || nanos < 0) "-" else ""

        return sign + abs(seconds).toString() + fractionOf(abs(nanos)) + "s"
    }

    private fun durationFromJson(json: dynamic): dynamic {
        val text = (json as String).removeSuffix("s")
        val negative = text.startsWith("-")
        val digits = text.removePrefix("-").removePrefix("+")

        val seconds = digits.substringBefore('.').ifEmpty { "0" }.toLong()
        val nanos = nanosOf(digits.substringAfter('.', ""))

        val message: dynamic = js("({})")
        message.seconds = (if (negative) -seconds else seconds).toProtobufJsLong()
        message.nanos = if (negative) -nanos else nanos

        return message
    }

    private val FRACTION = Regex("""\.(\d+)""")

    private fun fractionOf(nanos: Int): String = when {
        nanos == 0 -> ""
        nanos % NANOS_PER_MILLI == 0 -> "." + (nanos / NANOS_PER_MILLI).toString().padStart(3, '0')
        nanos % NANOS_PER_MICRO == 0 -> "." + (nanos / NANOS_PER_MICRO).toString().padStart(6, '0')
        else -> "." + nanos.toString().padStart(9, '0')
    }

    private fun nanosOf(fraction: String): Int =
        if (fraction.isEmpty()) 0 else fraction.padEnd(9, '0').substring(0, 9).toInt()

    private fun fieldMaskToJson(message: dynamic): dynamic {
        val paths = message.paths.unsafeCast<Array<String>?>().orEmpty()

        return paths.joinToString(",") { path -> path.replace(SNAKE) { it.groupValues[1].uppercase() } }
    }

    private fun fieldMaskFromJson(json: dynamic): dynamic {
        val text = json as String

        val message: dynamic = js("({})")
        message.paths = text
            .split(",")
            .filter { it.isNotEmpty() }
            .map { path -> path.replace(CAMEL) { "_" + it.value.lowercase() } }
            .toTypedArray()

        return message
    }

    private val SNAKE = Regex("""_([a-z])""")

    private val CAMEL = Regex("""[A-Z]""")

    private fun structToJson(message: dynamic): dynamic {
        val json: dynamic = js("({})")
        val fields = message.fields

        if (fields != null) {
            for ((key, value) in fields.unsafeCast<ProtobufJsMap<dynamic>>().entries()) {
                json[key] = valueToJson(value)
            }
        }

        return json
    }

    private fun structFromJson(json: dynamic): dynamic {
        val fields: dynamic = js("({})")

        for (entry in json.unsafeCast<ProtobufJsMap<dynamic>>().entries()) {
            fields[entry.first] = valueFromJson(entry.second)
        }

        val message: dynamic = js("({})")
        message.fields = fields

        return message
    }

    private fun listValueToJson(message: dynamic): dynamic =
        message.values.unsafeCast<Array<dynamic>?>().orEmpty().map(::valueToJson).toTypedArray()

    private fun listValueFromJson(json: dynamic): dynamic {
        val message: dynamic = js("({})")
        message.values = json.unsafeCast<Array<dynamic>>().map(::valueFromJson).toTypedArray()

        return message
    }

    private fun valueToJson(message: dynamic): dynamic = when {
        has(message, "structValue") -> structToJson(message.structValue)
        has(message, "listValue") -> listValueToJson(message.listValue)
        has(message, "stringValue") -> message.stringValue
        has(message, "numberValue") -> message.numberValue
        has(message, "boolValue") -> message.boolValue
        else -> null
    }

    private fun valueFromJson(json: dynamic): dynamic {
        val message: dynamic = js("({})")

        when {
            json == null -> message.nullValue = 0
            jsTypeOf(json) == "string" -> message.stringValue = json
            jsTypeOf(json) == "number" -> message.numberValue = json
            jsTypeOf(json) == "boolean" -> message.boolValue = json
            isArray(json) -> message.listValue = listValueFromJson(json)
            else -> message.structValue = structFromJson(json)
        }

        return message
    }

    private fun int64ToJson(value: dynamic): dynamic = readInt64(value).toString()

    private fun int64FromJson(json: dynamic): dynamic = "$json".toLong().toProtobufJsLong()

    private fun uint64ToJson(value: dynamic): dynamic = readInt64(value).toULong().toString()

    private fun uint64FromJson(json: dynamic): dynamic = "$json".toULong().toProtobufJsLong()

    @OptIn(ExperimentalEncodingApi::class)
    private fun bytesToJson(value: dynamic): dynamic =
        Base64.encode(value.unsafeCast<org.khronos.webgl.Uint8Array>().toByteArray())

    @OptIn(ExperimentalEncodingApi::class)
    private fun bytesFromJson(json: dynamic): dynamic = Base64.decode(json as String).toUint8Array()

    private fun readInt64(value: dynamic): Long = when {
        value == null -> 0L
        jsTypeOf(value) == "number" -> (value as Double).toLong()
        jsTypeOf(value) == "string" -> (value as String).toLong()
        else -> value.unsafeCast<ProtobufJsLong>().toLong()
    }

    private fun readInt32(value: dynamic): Int = when {
        value == null -> 0
        jsTypeOf(value) == "string" -> (value as String).toInt()
        else -> (value as Number).toInt()
    }

    private fun has(message: dynamic, name: String): Boolean =
        message != null && message[name] != null

    private fun isArray(value: dynamic): Boolean = js("Array").isArray(value).unsafeCast<Boolean>()
}

@JsName("Date")
private external class JsDate(milliseconds: Double) {

    fun toISOString(): String

    companion object {

        fun parse(dateString: String): Double
    }
}

fun structuralJson(type: Type, json: dynamic): dynamic = WellKnownJson.structuralJson(type, json)
