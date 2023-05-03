package kr.jadekim.protobuf.generator.type

import com.google.protobuf.Descriptors.Descriptor
import kotlin.reflect.KClass

sealed interface PreDefinedType {

    val descriptor: Descriptor
    val kotlinType: KClass<*>
    val defaultValue: String

    object Empty : PreDefinedType {

        override val descriptor: Descriptor = com.google.protobuf.Empty.getDescriptor()
        override val kotlinType: KClass<*> = Unit::class
        override val defaultValue: String = "Unit"
    }
}