package kr.jadekim.protobuf.generator.converter.util.extention

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kr.jadekim.protobuf.generator.util.extention.fileName
import kr.jadekim.protobuf.generator.util.extention.outputTypeName

val Descriptors.FileDescriptor.outputConverterFileName: String
    get() = "$fileName.converter.kt"

val ClassName.converterTypeName: ClassName
    get() = ClassName(packageName, simpleNames.map { it + "Converter" })

val Descriptors.Descriptor.converterTypeName: ClassName
    get() = outputTypeName.converterTypeName
