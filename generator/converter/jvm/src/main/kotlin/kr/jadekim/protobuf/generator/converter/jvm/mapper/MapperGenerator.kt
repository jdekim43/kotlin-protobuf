package kr.jadekim.protobuf.generator.converter.jvm.mapper

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.TypeSpec
import kr.jadekim.protobuf.generator.ImportName

interface MapperGenerator<T : Descriptors.GenericDescriptor> {

    fun generate(descriptor: T): Pair<TypeSpec, Set<ImportName>>
}