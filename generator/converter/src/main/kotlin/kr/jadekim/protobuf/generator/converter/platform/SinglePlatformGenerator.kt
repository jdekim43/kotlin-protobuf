package kr.jadekim.protobuf.generator.converter.platform

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import kr.jadekim.protobuf.converter.ProtobufConverter
import kr.jadekim.protobuf.generator.ImportName
import kr.jadekim.protobuf.generator.converter.util.extention.converterTypeName
import kr.jadekim.protobuf.generator.util.extention.outputTypeName
import kr.jadekim.protobuf.generator.util.extention.typeName

class SinglePlatformGenerator(
    val isActual: Boolean = false,
    val resolvePlatformTypeName: Descriptors.Descriptor.() -> ClassName,
) : PlatformConverterGenerator<Descriptors.Descriptor> {

    override fun generate(descriptor: Descriptors.Descriptor): Pair<List<TypeSpec>, Set<ImportName>> {
        val outputTypeName = descriptor.outputTypeName
        val platformTypeName = descriptor.resolvePlatformTypeName()
        val name = descriptor.converterTypeName
        val spec = TypeSpec.objectBuilder(name)
        val imports = mutableSetOf<ImportName>()

        if (isActual) {
            spec.addModifiers(KModifier.ACTUAL)
        }

        spec.addSuperinterface(
            ProtobufConverter::class.typeName.parameterizedBy(outputTypeName),
            CodeBlock.of("%T", platformTypeName),
        )

        descriptor.readChildren(spec, imports)

        return listOf(spec.build()) to imports.toSet()
    }

    private fun Descriptors.Descriptor.readChildren(spec: TypeSpec.Builder, imports: MutableSet<ImportName>) {
        for (nestedType in nestedTypes.filterNot { it.options.mapEntry }) {
            val (childType, childImports) = generate(nestedType)
            imports.addAll(childImports)
            spec.addTypes(childType)
        }
    }
}