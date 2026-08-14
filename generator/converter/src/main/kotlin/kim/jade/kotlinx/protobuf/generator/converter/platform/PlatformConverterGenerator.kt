package kim.jade.kotlinx.protobuf.generator.converter.platform

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.TypeSpec
import kim.jade.kotlinx.protobuf.generator.ImportName

interface PlatformConverterGenerator<T : Descriptors.GenericDescriptor> {

    fun generate(descriptor: T): Pair<List<TypeSpec>, Set<ImportName>>
}