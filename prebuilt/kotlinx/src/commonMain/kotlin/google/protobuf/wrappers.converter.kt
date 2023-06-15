// Transform from google/protobuf/wrappers.proto
@file:GeneratorVersion(version = "0.2.3")

package google.protobuf

import java.lang.IllegalStateException
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.converter.parseProtobuf

public expect object DoubleValueConverter : ProtobufConverter<DoubleValue>

public fun DoubleValue.toAny(): Any = Any(DoubleValue.TYPE_URL, with(DoubleValueConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<DoubleValue>): DoubleValue {
  if (typeUrl != DoubleValue.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public expect object FloatValueConverter : ProtobufConverter<FloatValue>

public fun FloatValue.toAny(): Any = Any(FloatValue.TYPE_URL, with(FloatValueConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<FloatValue>): FloatValue {
  if (typeUrl != FloatValue.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public expect object Int64ValueConverter : ProtobufConverter<Int64Value>

public fun Int64Value.toAny(): Any = Any(Int64Value.TYPE_URL, with(Int64ValueConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Int64Value>): Int64Value {
  if (typeUrl != Int64Value.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public expect object UInt64ValueConverter : ProtobufConverter<UInt64Value>

public fun UInt64Value.toAny(): Any = Any(UInt64Value.TYPE_URL, with(UInt64ValueConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<UInt64Value>): UInt64Value {
  if (typeUrl != UInt64Value.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public expect object Int32ValueConverter : ProtobufConverter<Int32Value>

public fun Int32Value.toAny(): Any = Any(Int32Value.TYPE_URL, with(Int32ValueConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Int32Value>): Int32Value {
  if (typeUrl != Int32Value.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public expect object UInt32ValueConverter : ProtobufConverter<UInt32Value>

public fun UInt32Value.toAny(): Any = Any(UInt32Value.TYPE_URL, with(UInt32ValueConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<UInt32Value>): UInt32Value {
  if (typeUrl != UInt32Value.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public expect object BoolValueConverter : ProtobufConverter<BoolValue>

public fun BoolValue.toAny(): Any = Any(BoolValue.TYPE_URL, with(BoolValueConverter) { toByteArray()
    })

public fun Any.parse(converter: ProtobufConverter<BoolValue>): BoolValue {
  if (typeUrl != BoolValue.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public expect object StringValueConverter : ProtobufConverter<StringValue>

public fun StringValue.toAny(): Any = Any(StringValue.TYPE_URL, with(StringValueConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<StringValue>): StringValue {
  if (typeUrl != StringValue.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public expect object BytesValueConverter : ProtobufConverter<BytesValue>

public fun BytesValue.toAny(): Any = Any(BytesValue.TYPE_URL, with(BytesValueConverter) {
    toByteArray() })

public fun Any.parse(converter: ProtobufConverter<BytesValue>): BytesValue {
  if (typeUrl != BytesValue.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}
