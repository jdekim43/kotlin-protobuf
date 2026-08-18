package kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.mapper

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.TypeSpec
import kim.jade.kotlinx.protobuf.generator.ImportName

interface JsMapperGenerator<T : Descriptors.GenericDescriptor> {

    fun generate(descriptor: T): Pair<TypeSpec, Set<ImportName>>
}
