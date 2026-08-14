package kim.jade.kotlinx.protobuf.generator.type

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kim.jade.kotlinx.protobuf.generator.ImportName

typealias TypeGeneratorPlugins<T> = List<TypeGenerator.Plugin<T>>

typealias PropertyGeneratorPlugins = List<TypeGenerator.PropertyPlugin>

fun <T : Descriptors.GenericDescriptor> TypeGeneratorPlugins<T>.applyTo(
    spec: TypeSpec.Builder,
    imports: MutableSet<ImportName>,
    descriptor: T,
) {
    forEach { it.applyTo(spec, imports, descriptor) }
}

fun PropertyGeneratorPlugins.applyToField(
    parameter: ParameterSpec.Builder,
    property: PropertySpec.Builder,
    imports: MutableSet<ImportName>,
    descriptor: Descriptors.FieldDescriptor,
) {
    forEach { it.applyToField(parameter, property, imports, descriptor) }
}

interface TypeGenerator<T : Descriptors.GenericDescriptor> {

    interface Plugin<T : Descriptors.GenericDescriptor> {

        fun applyTo(spec: TypeSpec.Builder, imports: MutableSet<ImportName>, descriptor: T)
    }

    interface PropertyPlugin {

        fun applyToField(
            parameter: ParameterSpec.Builder,
            property: PropertySpec.Builder,
            imports: MutableSet<ImportName>,
            descriptor: Descriptors.FieldDescriptor,
        )
    }

    fun generate(descriptor: T): Pair<TypeSpec, Set<ImportName>>
}
