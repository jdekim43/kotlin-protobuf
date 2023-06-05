package kr.jadekim.protobuf.generator.converter.jvm.util.extention

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kr.jadekim.protobuf.generator.util.extention.fileName
import kr.jadekim.protobuf.generator.util.extention.outputTypeName

val Descriptors.FileDescriptor.outputConverterFileName: String
    get() = "$fileName.converter.jvm.kt"

val ClassName.jvmConverterTypeName: ClassName
    get() = ClassName(packageName, simpleNames.map { it + "JvmConverter" })

val Descriptors.Descriptor.jvmConverterTypeName: ClassName
    get() = outputTypeName.jvmConverterTypeName
