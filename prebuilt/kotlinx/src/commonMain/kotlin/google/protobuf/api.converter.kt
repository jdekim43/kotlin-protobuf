// Transform from google/protobuf/api.proto
@file:GeneratorVersion(version = "0.5.2")

package google.protobuf

import java.lang.IllegalStateException
import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.converter.parseProtobuf

public expect object ApiConverter : ProtobufConverter<Api>

public fun Api.toAny(): Any = Any(Api.TYPE_URL, with(ApiConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Api> = ApiConverter): Api {
  if (typeUrl != Api.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val Api.Companion.converter: ApiConverter
  get() = ApiConverter

public expect object MethodConverter : ProtobufConverter<Method>

public fun Method.toAny(): Any = Any(Method.TYPE_URL, with(MethodConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Method> = MethodConverter): Method {
  if (typeUrl != Method.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val Method.Companion.converter: MethodConverter
  get() = MethodConverter

public expect object MixinConverter : ProtobufConverter<Mixin>

public fun Mixin.toAny(): Any = Any(Mixin.TYPE_URL, with(MixinConverter) { toByteArray() })

public fun Any.parse(converter: ProtobufConverter<Mixin> = MixinConverter): Mixin {
  if (typeUrl != Mixin.TYPE_URL) throw IllegalStateException("Please check the type_url")
  return value.parseProtobuf(converter)
}

public val Mixin.Companion.converter: MixinConverter
  get() = MixinConverter
