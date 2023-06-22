// Transform from google/protobuf/wrappers.proto
@file:ProtobufSyntax(syntax = "PROTO3")
@file:GeneratorVersion(version = "0.3.2")

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
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.`annotation`.ProtobufIndex
import kr.jadekim.protobuf.`annotation`.ProtobufSyntax
import kr.jadekim.protobuf.type.ProtobufMessage

public data class DoubleValue(
  @ProtobufIndex(index = 1)
  public val `value`: Double,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.DoubleValue"
  }
}

public data class FloatValue(
  @ProtobufIndex(index = 1)
  public val `value`: Float,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.FloatValue"
  }
}

public data class Int64Value(
  @ProtobufIndex(index = 1)
  public val `value`: Long,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Int64Value"
  }
}

public data class UInt64Value(
  @ProtobufIndex(index = 1)
  public val `value`: ULong,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.UInt64Value"
  }
}

public data class Int32Value(
  @ProtobufIndex(index = 1)
  public val `value`: Int,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.Int32Value"
  }
}

public data class UInt32Value(
  @ProtobufIndex(index = 1)
  public val `value`: UInt,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.UInt32Value"
  }
}

public data class BoolValue(
  @ProtobufIndex(index = 1)
  public val `value`: Boolean,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.BoolValue"
  }
}

public data class StringValue(
  @ProtobufIndex(index = 1)
  public val `value`: String,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.StringValue"
  }
}

public data class BytesValue(
  @ProtobufIndex(index = 1)
  public val `value`: ByteArray,
) : ProtobufMessage {
  public companion object {
    public const val TYPE_URL: String = "type.googleapis.com/google.protobuf.BytesValue"
  }
}
