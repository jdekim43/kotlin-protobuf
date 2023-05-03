package kr.jadekim.protobuf.generator.converter.mapper

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.Import
import com.squareup.kotlinpoet.TypeSpec

interface MapperGenerator<T : Descriptors.GenericDescriptor> {

    fun generate(descriptor: T): Pair<TypeSpec, Set<Import>>
}