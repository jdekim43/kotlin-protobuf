package kr.jadekim.protobuf.generator.grpc.gateway.util.extension

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kr.jadekim.protobuf.generator.util.extention.fileName
import kr.jadekim.protobuf.generator.util.extention.outputPackage
import kr.jadekim.protobuf.generator.util.extention.outputTypeName

val Descriptors.FileDescriptor.outputGrpcGatewayPackageName: String
    get() = "$outputPackage.grpc.gateway"

val Descriptors.FileDescriptor.outputGrpcGatewayFileName: String
    get() = "$fileName.kt"

val Descriptors.ServiceDescriptor.interfaceTypeName
    get() = outputTypeName

val Descriptors.ServiceDescriptor.grpcGatewayTypeName
    get() = ClassName(file.outputGrpcGatewayPackageName, outputTypeName.simpleNames.map { it + "GrpcGateway" })

val Descriptors.ServiceDescriptor.grpcGatewayClientTypeName
    get() = grpcGatewayTypeName.nestedClass("Client")
