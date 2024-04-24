package kr.jadekim.protobuf.generator.grpc.gateway.util.extension

import com.google.api.HttpRule
import io.grpc.MethodDescriptor
import io.grpc.protobuf.ProtoMethodDescriptorSupplier

val MethodDescriptor<*, *>.httpExtension: HttpRule?
    get() = (schemaDescriptor as? ProtoMethodDescriptorSupplier)?.methodDescriptor?.options
        ?.getExtension(com.google.api.AnnotationsProto.http)
