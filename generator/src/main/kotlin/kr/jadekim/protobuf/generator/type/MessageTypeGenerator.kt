package kr.jadekim.protobuf.generator.type

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import kr.jadekim.protobuf.generator.util.extention.*

class MessageTypeGenerator(
    private val enumTypeGenerator: EnumTypeGenerator,
    val plugins: TypeGeneratorPlugins<Descriptors.Descriptor> = emptyList(),
    val oneOfPlugins: TypeGeneratorPlugins<Descriptors.OneofDescriptor> = emptyList(),
    val oneOfItemPlugins: TypeGeneratorPlugins<Descriptors.FieldDescriptor> = emptyList(),
) : TypeGenerator<Descriptors.Descriptor> {

    override fun generate(descriptor: Descriptors.Descriptor): Pair<TypeSpec, Set<Import>> {
        val name = descriptor.outputTypeName
        val spec = TypeSpec.classBuilder(name)
        val imports = mutableSetOf<Import>()

        if (descriptor.fields.isNotEmpty()) {
            spec.addModifiers(KModifier.DATA)
        }

        val constructor = FunSpec.constructorBuilder()

        descriptor.readFields(spec, constructor, imports)
        descriptor.readOneOf(spec, constructor, imports)
        descriptor.readChildren(spec, imports)

        spec.primaryConstructor(constructor.build())

        plugins.applyTo(spec, imports, descriptor)

        return spec.build() to imports.toSet()
    }

    private fun Descriptors.Descriptor.readChildren(spec: TypeSpec.Builder, imports: MutableSet<Import>) {
        for (nestedType in enumTypes) {
            val (childType, childImports) = enumTypeGenerator.generate(nestedType)
            imports.addAll(childImports)
            spec.addType(childType)
        }

        for (nestedType in nestedTypes.filterNot { it.options.mapEntry }) {
            val (childType, childImports) = generate(nestedType)
            imports.addAll(childImports)
            spec.addType(childType)
        }
    }

    private fun Descriptors.Descriptor.readFields(
        spec: TypeSpec.Builder,
        constructor: FunSpec.Builder,
        imports: MutableSet<Import>
    ) {
        realFields.forEach { it.addTo(spec, constructor) }
    }

    private fun Descriptors.Descriptor.readOneOf(
        spec: TypeSpec.Builder,
        constructor: FunSpec.Builder,
        imports: MutableSet<Import>
    ) {
        realOneofs.forEach {
            val typeName = it.addTo(spec, imports)
            val fieldName = it.outputVariableNameString
            constructor.addParameter(ParameterSpec.builder(fieldName, typeName).build())
            spec.addProperty(PropertySpec.builder(fieldName, typeName).initializer(fieldName).build())
        }
    }

    private fun Descriptors.FieldDescriptor.addTo(
        spec: TypeSpec.Builder,
        constructor: FunSpec.Builder,
        overrideName: String? = null
    ) {
        val isOptional = hasOptionalKeyword()
        val typeName = outputTypeName
        val fieldName = overrideName ?: outputVariableNameString
        val parameter = ParameterSpec.builder(fieldName, typeName)

        if (isOptional) {
            parameter.defaultValue("null")
        }

        parameter.addNumberAnnotation(number)

        if (options.deprecated) {
            parameter.addAnnotation(Deprecated::class)
        }

        constructor.addParameter(parameter.build())
        spec.addProperty(PropertySpec.builder(fieldName, typeName).initializer(fieldName).build())
    }

    private fun Descriptors.OneofDescriptor.addTo(spec: TypeSpec.Builder, imports: MutableSet<Import>): TypeName {
        val oneOfTypeName = outputTypeName
        val oneOfSpec = TypeSpec.interfaceBuilder(oneOfTypeName)

        oneOfSpec.addModifiers(KModifier.SEALED)

        for (field in fields) {
            val itemSpec = TypeSpec.valueClassBuilder(field.outputOneOfItemTypeNameString)
            val itemConstructor = FunSpec.constructorBuilder()

            itemSpec.addAnnotation(JvmInline::class)
            field.addTo(itemSpec, itemConstructor, "value")

            itemSpec.primaryConstructor(itemConstructor.build())
            itemSpec.addSuperinterface(oneOfTypeName)

            oneOfItemPlugins.applyTo(itemSpec, imports, field)

            oneOfSpec.addType(itemSpec.build())
        }

        oneOfPlugins.applyTo(oneOfSpec, imports, this)

        spec.addType(oneOfSpec.build())

        return oneOfTypeName
    }
}
