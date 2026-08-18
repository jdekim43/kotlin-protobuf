package kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.util.extention

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kim.jade.kotlinx.protobuf.generator.util.extention.fileName
import kim.jade.kotlinx.protobuf.generator.util.extention.outputTypeName

val Descriptors.FileDescriptor.outputJsConverterFileName: String
    get() = "$fileName.converter.js.kt"

val Descriptors.FileDescriptor.outputJsDelegatorFileName: String
    get() = "$fileName.kt"

val ClassName.jsConverterTypeName: ClassName
    get() = ClassName(packageName, simpleNames.map { it + "JsConverter" })

val Descriptors.Descriptor.jsConverterTypeName: ClassName
    get() = outputTypeName.jsConverterTypeName
