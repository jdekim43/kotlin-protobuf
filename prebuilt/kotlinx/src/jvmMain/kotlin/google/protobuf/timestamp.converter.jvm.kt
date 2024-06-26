// Transform from google/protobuf/timestamp.proto
@file:GeneratorVersion(version = "0.5.2")

package google.protobuf

import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper

public open class TimestampJvmConverter :
    ProtobufTypeMapper<Timestamp, com.google.protobuf.Timestamp> {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.Timestamp.getDescriptor()

  override val parser: Parser<com.google.protobuf.Timestamp> =
      com.google.protobuf.Timestamp.parser()

  override val default: com.google.protobuf.Timestamp =
      com.google.protobuf.Timestamp.getDefaultInstance()

  override fun convert(obj: com.google.protobuf.Timestamp): Timestamp = Timestamp(
  	seconds = obj.getSeconds(),
  	nanos = obj.getNanos(),
  )

  override fun convert(obj: Timestamp): com.google.protobuf.Timestamp {
    val builder = com.google.protobuf.Timestamp.newBuilder()
    builder.setSeconds(obj.seconds)
    builder.setNanos(obj.nanos)
    return builder.build()
  }
}
