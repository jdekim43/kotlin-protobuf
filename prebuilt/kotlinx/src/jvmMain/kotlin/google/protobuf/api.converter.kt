// Transform from google/protobuf/api.proto
@file:GeneratorVersion(version = "0.3.3")

package google.protobuf

import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter

public actual object ApiConverter : ProtobufConverter<Api> by ApiJvmConverter

public actual object MethodConverter : ProtobufConverter<Method> by MethodJvmConverter

public actual object MixinConverter : ProtobufConverter<Mixin> by MixinJvmConverter
