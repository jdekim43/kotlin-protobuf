package kr.jadekim.protobuf.generator.type

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.Import
import com.squareup.kotlinpoet.TypeSpec
import kr.jadekim.protobuf.generator.util.extention.addNumberAnnotation
import kr.jadekim.protobuf.generator.util.extention.outputTypeName

class EnumTypeGenerator(
    private val enumValueTypeGenerator: EnumValueTypeGenerator,
    val plugins: TypeGeneratorPlugins<Descriptors.EnumDescriptor> = emptyList(),
) : TypeGenerator<Descriptors.EnumDescriptor> {

    override fun generate(descriptor: Descriptors.EnumDescriptor): Pair<TypeSpec, Set<Import>> {
        val name = descriptor.outputTypeName
        val spec = TypeSpec.enumBuilder(name)
        val imports = mutableSetOf<Import>()

        if (descriptor.options.deprecated) {
            spec.addAnnotation(Deprecated::class)
        }

        val indexed = mutableListOf<Int>()
        for (value in descriptor.values) {
            if (!descriptor.options.allowAlias && indexed.contains(value.index)) {
                throw IllegalStateException("Duplicated enum index")
            }

            val (enumValueSpec, enumValueImports) = enumValueTypeGenerator.generate(value)
            imports.addAll(enumValueImports)
            spec.addEnumConstant(value.name, enumValueSpec)
            indexed.add(value.index)
        }

        plugins.applyTo(spec, imports, descriptor)

        return spec.build() to imports.toSet()
    }
}

class EnumValueTypeGenerator(
    val plugins: TypeGeneratorPlugins<Descriptors.EnumValueDescriptor> = emptyList(),
) : TypeGenerator<Descriptors.EnumValueDescriptor> {

    override fun generate(descriptor: Descriptors.EnumValueDescriptor): Pair<TypeSpec, Set<Import>> {
        val spec = TypeSpec.anonymousClassBuilder()
        val imports = mutableSetOf<Import>()

        spec.addNumberAnnotation(descriptor.number)

        if (descriptor.options.deprecated) {
            spec.addAnnotation(Deprecated::class)
        }

        plugins.applyTo(spec, imports, descriptor)

        return spec.build() to imports.toSet()
    }
}
