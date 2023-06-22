// Transform from google/protobuf/wrappers.proto
@file:GeneratorVersion(version = "0.3.2")

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
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.DoubleValue.getDescriptor()

  public override val parser: Parser<com.google.protobuf.DoubleValue> =
      com.google.protobuf.DoubleValue.parser()

  public override fun convert(obj: com.google.protobuf.DoubleValue): DoubleValue = DoubleValue(
  	`value` = obj.getValue(),
  )

  public override fun convert(obj: DoubleValue): com.google.protobuf.DoubleValue {
    val builder = com.google.protobuf.DoubleValue.newBuilder()
    builder.setValue(obj.`value`)
    return builder.build()
  }
}

public object FloatValueJvmConverter :
    ProtobufTypeMapper<FloatValue, com.google.protobuf.FloatValue> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.FloatValue.getDescriptor()

  public override val parser: Parser<com.google.protobuf.FloatValue> =
      com.google.protobuf.FloatValue.parser()

  public override fun convert(obj: com.google.protobuf.FloatValue): FloatValue = FloatValue(
  	`value` = obj.getValue(),
  )

  public override fun convert(obj: FloatValue): com.google.protobuf.FloatValue {
    val builder = com.google.protobuf.FloatValue.newBuilder()
    builder.setValue(obj.`value`)
    return builder.build()
  }
}

public object Int64ValueJvmConverter :
    ProtobufTypeMapper<Int64Value, com.google.protobuf.Int64Value> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.Int64Value.getDescriptor()

  public override val parser: Parser<com.google.protobuf.Int64Value> =
      com.google.protobuf.Int64Value.parser()

  public override fun convert(obj: com.google.protobuf.Int64Value): Int64Value = Int64Value(
  	`value` = obj.getValue(),
  )

  public override fun convert(obj: Int64Value): com.google.protobuf.Int64Value {
    val builder = com.google.protobuf.Int64Value.newBuilder()
    builder.setValue(obj.`value`)
    return builder.build()
  }
}

public object UInt64ValueJvmConverter :
    ProtobufTypeMapper<UInt64Value, com.google.protobuf.UInt64Value> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.UInt64Value.getDescriptor()

  public override val parser: Parser<com.google.protobuf.UInt64Value> =
      com.google.protobuf.UInt64Value.parser()

  public override fun convert(obj: com.google.protobuf.UInt64Value): UInt64Value = UInt64Value(
  	`value` = obj.getValue().asKotlinType,
  )

  public override fun convert(obj: UInt64Value): com.google.protobuf.UInt64Value {
    val builder = com.google.protobuf.UInt64Value.newBuilder()
    builder.setValue(obj.`value`.asJavaType)
    return builder.build()
  }
}

public object Int32ValueJvmConverter :
    ProtobufTypeMapper<Int32Value, com.google.protobuf.Int32Value> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.Int32Value.getDescriptor()

  public override val parser: Parser<com.google.protobuf.Int32Value> =
      com.google.protobuf.Int32Value.parser()

  public override fun convert(obj: com.google.protobuf.Int32Value): Int32Value = Int32Value(
  	`value` = obj.getValue(),
  )

  public override fun convert(obj: Int32Value): com.google.protobuf.Int32Value {
    val builder = com.google.protobuf.Int32Value.newBuilder()
    builder.setValue(obj.`value`)
    return builder.build()
  }
}

public object UInt32ValueJvmConverter :
    ProtobufTypeMapper<UInt32Value, com.google.protobuf.UInt32Value> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.UInt32Value.getDescriptor()

  public override val parser: Parser<com.google.protobuf.UInt32Value> =
      com.google.protobuf.UInt32Value.parser()

  public override fun convert(obj: com.google.protobuf.UInt32Value): UInt32Value = UInt32Value(
  	`value` = obj.getValue().asKotlinType,
  )

  public override fun convert(obj: UInt32Value): com.google.protobuf.UInt32Value {
    val builder = com.google.protobuf.UInt32Value.newBuilder()
    builder.setValue(obj.`value`.asJavaType)
    return builder.build()
  }
}

public object BoolValueJvmConverter : ProtobufTypeMapper<BoolValue, com.google.protobuf.BoolValue> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.BoolValue.getDescriptor()

  public override val parser: Parser<com.google.protobuf.BoolValue> =
      com.google.protobuf.BoolValue.parser()

  public override fun convert(obj: com.google.protobuf.BoolValue): BoolValue = BoolValue(
  	`value` = obj.getValue(),
  )

  public override fun convert(obj: BoolValue): com.google.protobuf.BoolValue {
    val builder = com.google.protobuf.BoolValue.newBuilder()
    builder.setValue(obj.`value`)
    return builder.build()
  }
}

public object StringValueJvmConverter :
    ProtobufTypeMapper<StringValue, com.google.protobuf.StringValue> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.StringValue.getDescriptor()

  public override val parser: Parser<com.google.protobuf.StringValue> =
      com.google.protobuf.StringValue.parser()

  public override fun convert(obj: com.google.protobuf.StringValue): StringValue = StringValue(
  	`value` = obj.getValue(),
  )

  public override fun convert(obj: StringValue): com.google.protobuf.StringValue {
    val builder = com.google.protobuf.StringValue.newBuilder()
    builder.setValue(obj.`value`)
    return builder.build()
  }
}

public object BytesValueJvmConverter :
    ProtobufTypeMapper<BytesValue, com.google.protobuf.BytesValue> {
  public override val descriptor: Descriptors.Descriptor =
      com.google.protobuf.BytesValue.getDescriptor()

  public override val parser: Parser<com.google.protobuf.BytesValue> =
      com.google.protobuf.BytesValue.parser()

  public override fun convert(obj: com.google.protobuf.BytesValue): BytesValue = BytesValue(
  	`value` = obj.getValue().toByteArray(),
  )

  public override fun convert(obj: BytesValue): com.google.protobuf.BytesValue {
    val builder = com.google.protobuf.BytesValue.newBuilder()
    builder.setValue(ByteString.copyFrom(obj.`value`))
    return builder.build()
  }
}
