package kr.jadekim.protobuf.generator.type

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.Import
import com.squareup.kotlinpoet.TypeSpec

typealias TypeGeneratorPlugins<T> = List<TypeGenerator.Plugin<T>>

fun <T : Descriptors.GenericDescriptor> TypeGeneratorPlugins<T>.applyTo(
    spec: TypeSpec.Builder,
    imports: MutableSet<Import>,
    descriptor: T,
) {
    forEach { it.applyTo(spec, imports, descriptor) }
}

interface TypeGenerator<T : Descriptors.GenericDescriptor> {

    interface Plugin<T : Descriptors.GenericDescriptor> {

        fun applyTo(spec: TypeSpec.Builder, imports: MutableSet<Import>, descriptor: T)
    }

    fun generate(descriptor: T): Pair<TypeSpec, Set<Import>>
}
