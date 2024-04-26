package kr.jadekim.protobuf.kotlinx

import com.google.protobuf.Descriptors
import com.google.protobuf.TypeRegistry
import com.google.protobuf.util.JsonFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

class ProtobufJsonFormat(
    types: Map<String, Descriptors.Descriptor> = emptyMap(),
    override val serializersModule: SerializersModule = EmptySerializersModule(),
) : StringFormat {

    var types: Map<String, Descriptors.Descriptor> = types
        private set

    var registry: TypeRegistry = createTypeRegistry()
        private set

    private var printer: JsonFormat.Printer = createPrinter()
    private var parser: JsonFormat.Parser = createParser()

    init {
        update()
    }

    fun addTypes(types: Map<String, Descriptors.Descriptor>) {
        this.types += types
        update()
    }

    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
        val encoder = JsonEncoder(printer, serializersModule)
        serializer.serialize(encoder, value)
        return encoder.result
    }

    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
        val decoder = JsonDecoder(string, parser, serializersModule)
        return deserializer.deserialize(decoder)
    }

    private fun createTypeRegistry(): TypeRegistry {
        return TypeRegistry.newBuilder().add(types.values).build()
    }

    private fun createPrinter(): JsonFormat.Printer {
        return JsonFormat.printer().usingTypeRegistry(registry)
    }

    private fun createParser(): JsonFormat.Parser {
        return JsonFormat.parser().usingTypeRegistry(registry)
    }

    private fun update() {
        registry = createTypeRegistry()
        printer = createPrinter()
        parser = createParser()
    }
}