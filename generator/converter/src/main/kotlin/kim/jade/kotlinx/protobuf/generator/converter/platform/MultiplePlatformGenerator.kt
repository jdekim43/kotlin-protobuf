package kim.jade.kotlinx.protobuf.generator.converter.platform

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.BYTE_ARRAY
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import kim.jade.kotlinx.protobuf.converter.ProtobufConverter
import kim.jade.kotlinx.protobuf.generator.ImportName
import kim.jade.kotlinx.protobuf.generator.converter.util.extention.converterTypeName
import kim.jade.kotlinx.protobuf.generator.util.extention.outputTypeName
import kim.jade.kotlinx.protobuf.generator.util.extention.typeName

class MultiplePlatformGenerator : PlatformConverterGenerator<Descriptors.Descriptor> {

    override fun generate(descriptor: Descriptors.Descriptor): Pair<List<TypeSpec>, Set<ImportName>> = generate(descriptor, false)

    private fun generate(descriptor: Descriptors.Descriptor, isNested: Boolean): Pair<List<TypeSpec>, Set<ImportName>> {
        val outputTypeName = descriptor.outputTypeName
        val name = descriptor.converterTypeName
        val spec = TypeSpec.objectBuilder(name)
        val imports = mutableSetOf<ImportName>()

        if (!isNested) {
            spec.addModifiers(KModifier.EXPECT)
        }
        spec.addSuperinterface(ProtobufConverter::class.typeName.parameterizedBy(outputTypeName))

        spec.writeConverterMembers(outputTypeName)

        descriptor.readChildren(spec, imports)

        return listOf(spec.build()) to imports.toSet()
    }

    private fun TypeSpec.Builder.writeConverterMembers(outputTypeName: TypeName) {
        addFunction(
            FunSpec.builder("serialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("obj", outputTypeName)
                .returns(BYTE_ARRAY)
                .build()
        )

        addFunction(
            FunSpec.builder("deserialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("bytes", BYTE_ARRAY)
                .returns(outputTypeName)
                .build()
        )
    }

    private fun Descriptors.Descriptor.readChildren(spec: TypeSpec.Builder, imports: MutableSet<ImportName>) {
        for (nestedType in nestedTypes.filterNot { it.options.mapEntry }) {
            val (childType, childImports) = generate(nestedType, true)
            imports.addAll(childImports)
            spec.addTypes(childType)
        }
    }
}