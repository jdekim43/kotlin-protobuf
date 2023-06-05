package kr.jadekim.protobuf.generator.converter.platform

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.TypeSpec
import kr.jadekim.protobuf.generator.ImportName

interface PlatformConverterGenerator<T : Descriptors.GenericDescriptor> {

    fun generate(descriptor: T): Pair<List<TypeSpec>, Set<ImportName>>
}