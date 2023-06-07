package kr.jadekim.protobuf.generator.grpc.util.extension

import com.google.protobuf.Descriptors
import kr.jadekim.protobuf.generator.util.extention.outputTypeName

val Descriptors.ServiceDescriptor.interfaceTypeName
    get() = outputTypeName.nestedClass("Interface")
