package kr.jadekim.protobuf.generator.grpc.jvm.util.extension

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kr.jadekim.protobuf.generator.grpc.util.extension.grpcTypeName
import kr.jadekim.protobuf.generator.grpc.util.extension.outputGrpcPackageName
import kr.jadekim.protobuf.generator.util.extention.fileName

val Descriptors.FileDescriptor.outputJvmGrpcPackageName: String
    get() = "$outputGrpcPackageName.jvm"

val Descriptors.FileDescriptor.outputJvmGrpcFileName: String
    get() = "$fileName.kt"

val Descriptors.ServiceDescriptor.jvmGrpcTypeName: ClassName
    get() = ClassName(file.outputJvmGrpcPackageName, grpcTypeName.simpleNames.map { it + "Jvm" })
