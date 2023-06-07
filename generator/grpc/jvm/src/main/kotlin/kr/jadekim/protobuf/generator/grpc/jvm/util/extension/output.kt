package kr.jadekim.protobuf.generator.grpc.jvm.util.extension

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kr.jadekim.protobuf.generator.util.extention.fileName
import kr.jadekim.protobuf.generator.util.extention.outputTypeName

val Descriptors.FileDescriptor.outputJvmGrpcFileName: String
    get() = "$fileName.grpc.jvm.kt"

val ClassName.jvmGrpcTypeName: ClassName
    get() = ClassName(packageName, simpleNames.map { it + "Jvm" })

val Descriptors.ServiceDescriptor.jvmGrpcTypeName: ClassName
    get() = outputTypeName.jvmGrpcTypeName
