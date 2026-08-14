package kim.jade.kotlinx.protobuf.generator.type

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kim.jade.kotlinx.protobuf.generator.ImportName
import kim.jade.kotlinx.protobuf.generator.util.extention.addDeprecatedAnnotation
import kim.jade.kotlinx.protobuf.generator.util.extention.addNumberAnnotation
import kim.jade.kotlinx.protobuf.generator.util.extention.addOptionAnnotations
import kim.jade.kotlinx.protobuf.generator.util.extention.outputTypeName
import kim.jade.kotlinx.protobuf.generator.util.extention.typeUrl

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

        spec.addOptionAnnotations(descriptor.options)

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
                .addProperty(
                    PropertySpec.builder("TYPE_URL", String::class)
                        .addModifiers(KModifier.CONST)
                        .initializer("%S", descriptor.typeUrl)
                        .build()
                )
                .addFunction(
                    FunSpec.builder("forNumber")
                        .addParameter("number", Int::class)
                        .returns(name)
                        .addStatement("return %T.entries\n\t.first { it.number == number }", name)
                        .build()
                )
                .build()
        )

        val numbers = mutableSetOf<Int>()
        for (value in descriptor.values) {
            if (!numbers.add(value.number) && !descriptor.options.allowAlias) {
                throw IllegalStateException(
                    "Duplicated value ${value.number} for ${value.name} in ${descriptor.fullName}. " +
                        "Set option allow_alias = true on the enum to declare it as an alias."
                )
            }

            val (enumValueSpec, enumValueImports) = enumValueTypeGenerator.generate(value)
            imports.addAll(enumValueImports)
            spec.addEnumConstant(value.name, enumValueSpec)
        }

        plugins.applyTo(spec, imports, descriptor)

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
        spec.addOptionAnnotations(descriptor.options)

        if (descriptor.options.deprecated) {
            spec.addDeprecatedAnnotation("")
        }

        spec.addSuperclassConstructorParameter("%L", descriptor.number)

        plugins.applyTo(spec, imports, descriptor)

        return spec.build() to imports.toSet()
    }
}
