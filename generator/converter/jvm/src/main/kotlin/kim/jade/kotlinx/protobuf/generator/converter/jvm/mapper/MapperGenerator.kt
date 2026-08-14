package kim.jade.kotlinx.protobuf.generator.converter.jvm.mapper

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.TypeSpec
import kim.jade.kotlinx.protobuf.generator.ImportName

interface MapperGenerator<T : Descriptors.GenericDescriptor> {

    fun generate(descriptor: T): Pair<TypeSpec, Set<ImportName>>
}