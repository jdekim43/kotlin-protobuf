package kr.jadekim.protobuf.kotlinx

import com.google.protobuf.util.JsonFormat.Printer
import kotlinx.serialization.modules.SerializersModule
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper
import kr.jadekim.protobuf.type.ProtobufMessage

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