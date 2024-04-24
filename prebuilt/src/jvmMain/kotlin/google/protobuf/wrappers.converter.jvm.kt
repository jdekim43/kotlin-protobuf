// Transform from google/protobuf/wrappers.proto
@file:GeneratorVersion(version = "0.4.0")

package google.protobuf

import com.google.protobuf.ByteString
import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper
import kr.jadekim.protobuf.util.asJavaType
import kr.jadekim.protobuf.util.asKotlinType

public object DoubleValueJvmConverter :
    ProtobufTypeMapper<DoubleValue, com.google.protobuf.DoubleValue> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.DoubleValue.getDescriptor()

  override val parser: Parser<com.google.protobuf.DoubleValue> =
      com.google.protobuf.DoubleValue.parser()

  override fun convert(obj: com.google.protobuf.DoubleValue): DoubleValue = DoubleValue(
  	`value` = obj.getValue(),
  )

  override fun convert(obj: DoubleValue): com.google.protobuf.DoubleValue {
    val builder = com.google.protobuf.DoubleValue.newBuilder()
    builder.setValue(obj.`value`)
    return builder.build()
  }
}

public object FloatValueJvmConverter :
    ProtobufTypeMapper<FloatValue, com.google.protobuf.FloatValue> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.FloatValue.getDescriptor()

  override val parser: Parser<com.google.protobuf.FloatValue> =
      com.google.protobuf.FloatValue.parser()

  override fun convert(obj: com.google.protobuf.FloatValue): FloatValue = FloatValue(
  	`value` = obj.getValue(),
  )

  override fun convert(obj: FloatValue): com.google.protobuf.FloatValue {
    val builder = com.google.protobuf.FloatValue.newBuilder()
    builder.setValue(obj.`value`)
    return builder.build()
  }
}

public object Int64ValueJvmConverter :
    ProtobufTypeMapper<Int64Value, com.google.protobuf.Int64Value> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.Int64Value.getDescriptor()

  override val parser: Parser<com.google.protobuf.Int64Value> =
      com.google.protobuf.Int64Value.parser()

  override fun convert(obj: com.google.protobuf.Int64Value): Int64Value = Int64Value(
  	`value` = obj.getValue(),
  )

  override fun convert(obj: Int64Value): com.google.protobuf.Int64Value {
    val builder = com.google.protobuf.Int64Value.newBuilder()
    builder.setValue(obj.`value`)
    return builder.build()
  }
}

public object UInt64ValueJvmConverter :
    ProtobufTypeMapper<UInt64Value, com.google.protobuf.UInt64Value> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.UInt64Value.getDescriptor()

  override val parser: Parser<com.google.protobuf.UInt64Value> =
      com.google.protobuf.UInt64Value.parser()

  override fun convert(obj: com.google.protobuf.UInt64Value): UInt64Value = UInt64Value(
  	`value` = obj.getValue().asKotlinType,
  )

  override fun convert(obj: UInt64Value): com.google.protobuf.UInt64Value {
    val builder = com.google.protobuf.UInt64Value.newBuilder()
    builder.setValue(obj.`value`.asJavaType)
    return builder.build()
  }
}

public object Int32ValueJvmConverter :
    ProtobufTypeMapper<Int32Value, com.google.protobuf.Int32Value> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.Int32Value.getDescriptor()

  override val parser: Parser<com.google.protobuf.Int32Value> =
      com.google.protobuf.Int32Value.parser()

  override fun convert(obj: com.google.protobuf.Int32Value): Int32Value = Int32Value(
  	`value` = obj.getValue(),
  )

  override fun convert(obj: Int32Value): com.google.protobuf.Int32Value {
    val builder = com.google.protobuf.Int32Value.newBuilder()
    builder.setValue(obj.`value`)
    return builder.build()
  }
}

public object UInt32ValueJvmConverter :
    ProtobufTypeMapper<UInt32Value, com.google.protobuf.UInt32Value> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.UInt32Value.getDescriptor()

  override val parser: Parser<com.google.protobuf.UInt32Value> =
      com.google.protobuf.UInt32Value.parser()

  override fun convert(obj: com.google.protobuf.UInt32Value): UInt32Value = UInt32Value(
  	`value` = obj.getValue().asKotlinType,
  )

  override fun convert(obj: UInt32Value): com.google.protobuf.UInt32Value {
    val builder = com.google.protobuf.UInt32Value.newBuilder()
    builder.setValue(obj.`value`.asJavaType)
    return builder.build()
  }
}

public object BoolValueJvmConverter : ProtobufTypeMapper<BoolValue, com.google.protobuf.BoolValue> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.BoolValue.getDescriptor()

  override val parser: Parser<com.google.protobuf.BoolValue> =
      com.google.protobuf.BoolValue.parser()

  override fun convert(obj: com.google.protobuf.BoolValue): BoolValue = BoolValue(
  	`value` = obj.getValue(),
  )

  override fun convert(obj: BoolValue): com.google.protobuf.BoolValue {
    val builder = com.google.protobuf.BoolValue.newBuilder()
    builder.setValue(obj.`value`)
    return builder.build()
  }
}

public object StringValueJvmConverter :
    ProtobufTypeMapper<StringValue, com.google.protobuf.StringValue> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.StringValue.getDescriptor()

  override val parser: Parser<com.google.protobuf.StringValue> =
      com.google.protobuf.StringValue.parser()

  override fun convert(obj: com.google.protobuf.StringValue): StringValue = StringValue(
  	`value` = obj.getValue(),
  )

  override fun convert(obj: StringValue): com.google.protobuf.StringValue {
    val builder = com.google.protobuf.StringValue.newBuilder()
    builder.setValue(obj.`value`)
    return builder.build()
  }
}

public object BytesValueJvmConverter :
    ProtobufTypeMapper<BytesValue, com.google.protobuf.BytesValue> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.BytesValue.getDescriptor()

  override val parser: Parser<com.google.protobuf.BytesValue> =
      com.google.protobuf.BytesValue.parser()

  override fun convert(obj: com.google.protobuf.BytesValue): BytesValue = BytesValue(
  	`value` = obj.getValue().toByteArray(),
  )

  override fun convert(obj: BytesValue): com.google.protobuf.BytesValue {
    val builder = com.google.protobuf.BytesValue.newBuilder()
    builder.setValue(ByteString.copyFrom(obj.`value`))
    return builder.build()
  }
}
