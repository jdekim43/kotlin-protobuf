package kr.jadekim.protobuf.generator.converter.mapper.util.extention

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kr.jadekim.protobuf.generator.util.extention.outputPackage
import kr.jadekim.protobuf.generator.util.extention.outputTypeName

val Descriptors.Descriptor.mapperTypeName: ClassName
    get() = ClassName(outputPackage, outputTypeName.simpleNames.map { it + "Mapper" })
