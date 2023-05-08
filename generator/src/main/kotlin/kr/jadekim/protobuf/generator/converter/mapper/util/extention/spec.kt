package kr.jadekim.protobuf.generator.converter.mapper.util.extention

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kr.jadekim.protobuf.generator.util.extention.outputPackage
import kr.jadekim.protobuf.generator.util.extention.outputTypeName

val ClassName.mapperTypeName: ClassName
    get() = ClassName(packageName, simpleNames.map { it + "Mapper" })

val Descriptors.Descriptor.mapperTypeName: ClassName
    get() = outputTypeName.mapperTypeName
