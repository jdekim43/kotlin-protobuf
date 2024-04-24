package kr.jadekim.protobuf.generator.grpc.util.extension

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kr.jadekim.protobuf.generator.util.extention.fileName
import kr.jadekim.protobuf.generator.util.extention.outputPackage
import kr.jadekim.protobuf.generator.util.extention.outputTypeName

val Descriptors.FileDescriptor.outputGrpcPackageName: String
    get() = "$outputPackage.grpc"

val Descriptors.FileDescriptor.outputGrpcFileName: String
    get() = "$fileName.kt"

val Descriptors.ServiceDescriptor.interfaceTypeName
    get() = outputTypeName

val Descriptors.ServiceDescriptor.grpcTypeName
    get() = ClassName(file.outputGrpcPackageName, outputTypeName.simpleNames.map { it + "Grpc" })

val Descriptors.ServiceDescriptor.grpcClientTypeName
    get() = grpcTypeName.nestedClass("Client")

val Descriptors.ServiceDescriptor.grpcServerTypeName
    get() = grpcTypeName.nestedClass("Server")
