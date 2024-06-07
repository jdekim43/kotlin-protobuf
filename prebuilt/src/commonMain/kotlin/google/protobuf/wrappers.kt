// Transform from google/protobuf/wrappers.proto
@file:GeneratorVersion(version = "0.5.2")

package google.protobuf

import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.UInt
import kotlin.ULong
import kotlin.byteArrayOf
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.`annotation`.ProtobufMessage as AnnotationProtobufMessage
import kr.jadekim.protobuf.type.ProtobufMessage as TypeProtobufMessage

@AnnotationProtobufMessage(typeUrl = DoubleValue.TYPE_URL)
public data class DoubleValue(
  @ProtobufIndex(index = 1)
  public val `value`: Double = 0.0,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.DoubleValue"
  }
}

@AnnotationProtobufMessage(typeUrl = FloatValue.TYPE_URL)
public data class FloatValue(
  @ProtobufIndex(index = 1)
  public val `value`: Float = 0.0f,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FloatValue"
  }
}

@AnnotationProtobufMessage(typeUrl = Int64Value.TYPE_URL)
public data class Int64Value(
  @ProtobufIndex(index = 1)
  public val `value`: Long = 0L,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Int64Value"
  }
}

@AnnotationProtobufMessage(typeUrl = UInt64Value.TYPE_URL)
public data class UInt64Value(
  @ProtobufIndex(index = 1)
  public val `value`: ULong = 0uL,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.UInt64Value"
  }
}

@AnnotationProtobufMessage(typeUrl = Int32Value.TYPE_URL)
public data class Int32Value(
  @ProtobufIndex(index = 1)
  public val `value`: Int = 0,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Int32Value"
  }
}

@AnnotationProtobufMessage(typeUrl = UInt32Value.TYPE_URL)
public data class UInt32Value(
  @ProtobufIndex(index = 1)
  public val `value`: UInt = 0u,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.UInt32Value"
  }
}

@AnnotationProtobufMessage(typeUrl = BoolValue.TYPE_URL)
public data class BoolValue(
  @ProtobufIndex(index = 1)
  public val `value`: Boolean = false,
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.BoolValue"
  }
}

@AnnotationProtobufMessage(typeUrl = StringValue.TYPE_URL)
public data class StringValue(
  @ProtobufIndex(index = 1)
  public val `value`: String = "",
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.StringValue"
  }
}

@AnnotationProtobufMessage(typeUrl = BytesValue.TYPE_URL)
public data class BytesValue(
  @ProtobufIndex(index = 1)
  public val `value`: ByteArray = byteArrayOf(),
) : TypeProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.BytesValue"
  }
}
