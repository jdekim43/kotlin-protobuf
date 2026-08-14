package kim.jade.kotlinx.protobuf.serialization

import com.google.protobuf.util.JsonFormat.Printer
import kim.jade.kotlinx.protobuf.converter.ProtobufConverter
import kim.jade.kotlinx.protobuf.converter.mapper.ProtobufTypeMapper
import kim.jade.kotlinx.protobuf.type.ProtobufMessage
import kotlinx.serialization.modules.SerializersModule

class JsonEncoder(
    private val printer: Printer,
    serializersModule: SerializersModule,
) : ProtobufConverterEncoder(serializersModule) {

    var result: String = ""
        private set

    override fun <T : ProtobufMessage> serialize(converter: ProtobufConverter<T>, value: T) {
        if (converter !is ProtobufTypeMapper<T, *>) {
            throw IllegalArgumentException("Only acceptable ProtobufTypeMapper")
        }

        result = printer.print(converter.convert(value))
    }
}