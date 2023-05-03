package kr.jadekim.protobuf.generator.converter.mapper.util.extention

import com.google.protobuf.Descriptors
import kr.jadekim.protobuf.generator.util.extention.fileName

val Descriptors.FileDescriptor.outputConverterFileName: String
    get() = "$fileName.converter.kt"