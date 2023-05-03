package kr.jadekim.protobuf.generator.converter.mapper.util.extention

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kr.jadekim.protobuf.generator.util.extention.outputPackage
import kr.jadekim.protobuf.generator.util.extention.outputTypeName

val Descriptors.GenericDescriptor.mapperOutputPackage: String
    get() = "kr.jadekim.protobuf.mapper.mappers.$outputPackage"

val Descriptors.Descriptor.mapperTypeName: ClassName
    get() = ClassName(mapperOutputPackage, outputTypeName.simpleNames.map { it + "Mapper" })
