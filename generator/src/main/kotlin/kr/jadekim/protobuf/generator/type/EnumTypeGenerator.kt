package kr.jadekim.protobuf.generator.type

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kr.jadekim.protobuf.generator.ImportName
import kr.jadekim.protobuf.generator.file.TypeRegistryGenerator
import kr.jadekim.protobuf.generator.util.extention.addDeprecatedAnnotation
import kr.jadekim.protobuf.generator.util.extention.addNumberAnnotation
import kr.jadekim.protobuf.generator.util.extention.outputTypeName
import kr.jadekim.protobuf.generator.util.extention.typeUrl

class EnumTypeGenerator(
    private val enumValueTypeGenerator: EnumValueTypeGenerator,
    val plugins: TypeGeneratorPlugins<Descriptors.EnumDescriptor> = emptyList(),
) : TypeGenerator<Descriptors.EnumDescriptor> {

    override fun generate(descriptor: Descriptors.EnumDescriptor): Pair<TypeSpec, Set<ImportName>> {
        val name = descriptor.outputTypeName
        val spec = TypeSpec.enumBuilder(name)
        val imports = mutableSetOf<ImportName>()

        if (descriptor.options.deprecated) {
            spec.addDeprecatedAnnotation("")
        }

        spec.primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("number", Int::class)
                .build()
        )

        spec.addProperty(
            PropertySpec.builder("number", Int::class)
                .initializer("number")
                .build()
        )

        spec.addType(
            TypeSpec.companionObjectBuilder()
                .addFunction(
                    FunSpec.builder("forNumber")
                        .addParameter("number", Int::class)
                        .returns(name)
                        .addStatement("return %T.values()\n\t.first { it.number == number }", name)
                        .build()
                )
                .build()
        )

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
        TypeRegistryGenerator.registerEnum(descriptor.typeUrl, name)

        return spec.build() to imports.toSet()
    }
}

class EnumValueTypeGenerator(
    val plugins: TypeGeneratorPlugins<Descriptors.EnumValueDescriptor> = emptyList(),
) : TypeGenerator<Descriptors.EnumValueDescriptor> {

    override fun generate(descriptor: Descriptors.EnumValueDescriptor): Pair<TypeSpec, Set<ImportName>> {
        val spec = TypeSpec.anonymousClassBuilder()
        val imports = mutableSetOf<ImportName>()

        spec.addNumberAnnotation(descriptor.number)

        if (descriptor.options.deprecated) {
            spec.addDeprecatedAnnotation("")
        }

        spec.addSuperclassConstructorParameter("%L", descriptor.number)

        plugins.applyTo(spec, imports, descriptor)

        return spec.build() to imports.toSet()
    }
}
