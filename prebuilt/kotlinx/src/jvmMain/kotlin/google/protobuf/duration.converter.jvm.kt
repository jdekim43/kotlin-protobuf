// Transform from google/protobuf/duration.proto
@file:GeneratorVersion(version = "0.5.1")

package google.protobuf

import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper

public open class DurationJvmConverter : ProtobufTypeMapper<Duration, com.google.protobuf.Duration>
    {
  override val descriptor: Descriptors.Descriptor = com.google.protobuf.Duration.getDescriptor()

  override val parser: Parser<com.google.protobuf.Duration> = com.google.protobuf.Duration.parser()

  override val default: com.google.protobuf.Duration =
      com.google.protobuf.Duration.getDefaultInstance()

  override fun convert(obj: com.google.protobuf.Duration): Duration = Duration(
  	seconds = obj.getSeconds(),
  	nanos = obj.getNanos(),
  )

  override fun convert(obj: Duration): com.google.protobuf.Duration {
    val builder = com.google.protobuf.Duration.newBuilder()
    builder.setSeconds(obj.seconds)
    builder.setNanos(obj.nanos)
    return builder.build()
  }
}
