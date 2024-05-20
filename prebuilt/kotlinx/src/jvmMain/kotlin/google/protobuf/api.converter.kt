// Transform from google/protobuf/api.proto
@file:GeneratorVersion(version = "0.4.1")

package google.protobuf

import kr.jadekim.protobuf.`annotation`.GeneratorVersion
import kr.jadekim.protobuf.converter.ProtobufConverter

public actual object ApiConverter : ApiJvmConverter(), ProtobufConverter<Api>

public actual object MethodConverter : MethodJvmConverter(), ProtobufConverter<Method>

public actual object MixinConverter : MixinJvmConverter(), ProtobufConverter<Mixin>
