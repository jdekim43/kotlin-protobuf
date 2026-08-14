package kim.jade.kotlinx.protobuf.generator.converter.jvm.util.extention

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kim.jade.kotlinx.protobuf.generator.util.extention.fileName
import kim.jade.kotlinx.protobuf.generator.util.extention.outputTypeName

val Descriptors.FileDescriptor.outputJvmConverterFileName: String
    get() = "$fileName.converter.jvm.kt"

val ClassName.jvmConverterTypeName: ClassName
    get() = ClassName(packageName, simpleNames.map { it + "JvmConverter" })

val Descriptors.Descriptor.jvmConverterTypeName: ClassName
    get() = outputTypeName.jvmConverterTypeName
