package kim.jade.kotlinx.protobuf.generator.type

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import kim.jade.kotlinx.protobuf.generator.ImportName
import kim.jade.kotlinx.protobuf.generator.util.extention.*
import kim.jade.kotlinx.protobuf.type.ProtobufMessage

class MessageTypeGenerator(
    private val enumTypeGenerator: EnumTypeGenerator,
    val plugins: TypeGeneratorPlugins<Descriptors.Descriptor> = emptyList(),
    val oneOfPlugins: TypeGeneratorPlugins<Descriptors.OneofDescriptor> = emptyList(),
    val oneOfItemPlugins: TypeGeneratorPlugins<Descriptors.FieldDescriptor> = emptyList(),
    val propertyPlugins: PropertyGeneratorPlugins = emptyList(),
) : TypeGenerator<Descriptors.Descriptor> {

    override fun generate(descriptor: Descriptors.Descriptor): Pair<TypeSpec, Set<ImportName>> {
        val name = descriptor.outputTypeName
        val spec = TypeSpec.classBuilder(name)
        val imports = mutableSetOf<ImportName>()

        if (descriptor.fields.isNotEmpty()) {
            spec.addModifiers(KModifier.DATA)
        }

        spec.addMessageAnnotation(descriptor)
        spec.addOptionAnnotations(descriptor.options)
        spec.addSuperinterface(ProtobufMessage::class)

        if (descriptor.options.deprecated) {
            spec.addDeprecatedAnnotation("")
        }

        val constructor = FunSpec.constructorBuilder()
        val properties = mutableListOf<EqualityProperty>()

        descriptor.writeMetadataTo(spec)
        descriptor.readFields(spec, constructor, imports, properties)
        descriptor.readOneOf(spec, constructor, imports, properties)
        descriptor.readChildren(spec, imports)

        spec.primaryConstructor(constructor.build())

        spec.addContentEquality(name, properties)

        plugins.applyTo(spec, imports, descriptor)

        return spec.build() to imports.toSet()
    }

    private fun Descriptors.Descriptor.writeMetadataTo(spec: TypeSpec.Builder) {
        TypeSpec.companionObjectBuilder()
            .addProperty(
                PropertySpec.builder("TYPE_URL", String::class)
                    .addModifiers(KModifier.CONST)
                    .initializer("%S", typeUrl)
                    .build()
            )
            .addDescriptorBytes(this)
            .let { spec.addType(it.build()) }
    }

    private fun Descriptors.Descriptor.readChildren(spec: TypeSpec.Builder, imports: MutableSet<ImportName>) {
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
        imports: MutableSet<ImportName>,
        properties: MutableList<EqualityProperty>,
    ) {
        realFields.forEach { properties += it.addTo(spec, constructor, imports) }
    }

    private fun Descriptors.Descriptor.readOneOf(
        spec: TypeSpec.Builder,
        constructor: FunSpec.Builder,
        imports: MutableSet<ImportName>,
        properties: MutableList<EqualityProperty>,
    ) {
        realOneofs.forEach {
            val typeName = it.addTo(spec, imports).copy(nullable = true)
            val fieldName = it.outputVariableNameString

            constructor.addParameter(
                ParameterSpec.builder(fieldName, typeName)
                    .defaultValue("null")
                    .build()
            )
            spec.addProperty(PropertySpec.builder(fieldName, typeName).initializer(fieldName).build())

            properties += EqualityProperty(fieldName, EqualityStrategy.PLAIN, nullable = true)
        }
    }

    private fun Descriptors.FieldDescriptor.addTo(
        spec: TypeSpec.Builder,
        constructor: FunSpec.Builder,
        imports: MutableSet<ImportName>,
        overrideName: String? = null,
        applyPropertyPlugins: Boolean = true,
    ): EqualityProperty {
        val typeName = outputTypeName
        val fieldName = overrideName ?: outputVariableNameString
        val parameter = ParameterSpec.builder(fieldName, typeName)
        val property = PropertySpec.builder(fieldName, typeName).initializer(fieldName)

        if (!isRequired) {
            parameter.defaultValue(kotlinDefaultValue)
        }

        parameter.addFieldAnnotations(this)
        parameter.addOptionAnnotations(options, defaultValueOptionEntries)

        if (options.deprecated) {
            property.addDeprecatedAnnotation("")
        }

        if (applyPropertyPlugins) {
            propertyPlugins.applyToField(parameter, property, imports, this)
        }

        constructor.addParameter(parameter.build())
        spec.addProperty(property.build())

        return equalityProperty(fieldName)
    }

    private fun Descriptors.OneofDescriptor.addTo(spec: TypeSpec.Builder, imports: MutableSet<ImportName>): ClassName {
        val oneOfTypeName = outputTypeName
        val oneOfSpec = TypeSpec.interfaceBuilder(oneOfTypeName)

        oneOfSpec.addModifiers(KModifier.SEALED)
        oneOfSpec.addOneOfAnnotation(this)
        oneOfSpec.addOptionAnnotations(options)

        for (field in fields) {
            val itemTypeName = oneOfTypeName.nestedClass(field.outputOneOfItemTypeNameString)
            val itemSpec = TypeSpec.classBuilder(field.outputOneOfItemTypeNameString)
            val itemConstructor = FunSpec.constructorBuilder()

            if (field.equalityStrategy == EqualityStrategy.PLAIN) {
                itemSpec.addModifiers(KModifier.VALUE)
                itemSpec.addAnnotation(JvmInline::class)
            } else {
                itemSpec.addModifiers(KModifier.DATA)
            }

            val itemProperty = field.addTo(itemSpec, itemConstructor, imports, "value", applyPropertyPlugins = false)

            itemSpec.primaryConstructor(itemConstructor.build())
            itemSpec.addSuperinterface(oneOfTypeName)

            itemSpec.addContentEquality(itemTypeName, listOf(itemProperty))

            oneOfItemPlugins.applyTo(itemSpec, imports, field)

            oneOfSpec.addType(itemSpec.build())
        }

        oneOfPlugins.applyTo(oneOfSpec, imports, this)

        spec.addType(oneOfSpec.build())

        return oneOfTypeName
    }
}
