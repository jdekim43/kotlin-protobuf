package kr.jadekim.protobuf.generator.kotlinx.util

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kr.jadekim.protobuf.generator.util.extention.outputTypeName

val Descriptors.Descriptor.reflectSerializerTypeName: ClassName
    get() = outputTypeName.nestedClass("ReflectSerializer")

val Descriptors.Descriptor.serializerTypeName: ClassName
    get() = outputTypeName.nestedClass("KotlinxSerializer")