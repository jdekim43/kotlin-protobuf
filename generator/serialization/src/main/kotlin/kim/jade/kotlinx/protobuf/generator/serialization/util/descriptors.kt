package kim.jade.kotlinx.protobuf.generator.serialization.util

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import kim.jade.kotlinx.protobuf.generator.util.extention.outputTypeName

val Descriptors.Descriptor.reflectSerializerTypeName: ClassName
    get() = outputTypeName.nestedClass("ReflectSerializer")

val Descriptors.Descriptor.serializerTypeName: ClassName
    get() = outputTypeName.nestedClass("KotlinxSerializer")