package kr.jadekim.protobuf.generator.converter.mapper

import com.google.protobuf.ByteString
import com.google.protobuf.Descriptors
import com.google.protobuf.Parser
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper
import kr.jadekim.protobuf.generator.converter.mapper.util.extention.delegatorTypeName
import kr.jadekim.protobuf.generator.converter.mapper.util.extention.mapperTypeName
import kr.jadekim.protobuf.generator.util.extention.*
import net.pearx.kasechange.toPascalCase

class MessageMapperGenerator : MapperGenerator<Descriptors.Descriptor> {

    override fun generate(descriptor: Descriptors.Descriptor): Pair<TypeSpec, Set<Import>> {
        val outputTypeName = descriptor.outputTypeName
        val delegatorTypeName = descriptor.delegatorTypeName
        val name = descriptor.mapperTypeName
        val spec = TypeSpec.objectBuilder(name)
        val imports = mutableSetOf<Import>()

        val parentType = ProtobufTypeMapper::class.typeName.parameterizedBy(outputTypeName, delegatorTypeName)
        spec.addSuperinterface(parentType)

        descriptor.writeVariablesTo(spec, delegatorTypeName)
        descriptor.writeToKotlinConvertFunctionTo(spec, delegatorTypeName, outputTypeName)
        descriptor.writeToProtobufConvertFunctionTo(spec, delegatorTypeName, outputTypeName)

        descriptor.readChildren(spec, imports)

        return spec.build() to imports.toSet()
    }

    private fun Descriptors.Descriptor.writeVariablesTo(spec: TypeSpec.Builder, protobufTypeName: TypeName) {
        spec.addProperty(
            PropertySpec.builder("descriptor", Descriptors.Descriptor::class.typeName)
                .addModifiers(KModifier.OVERRIDE)
                .initializer("%T.getDescriptor()", protobufTypeName)
                .build()
        )
        spec.addProperty(
            PropertySpec.builder("parser", Parser::class.typeName.parameterizedBy(protobufTypeName))
                .addModifiers(KModifier.OVERRIDE)
                .initializer("%T.parser()", protobufTypeName)
                .build()
        )
    }

    private fun Descriptors.FieldDescriptor.getCodeForToKotlin(
        typeName: TypeName = outputTypeName,
        variableName: String = "obj.%L",
        variableNameArguments: Array<Any> = arrayOf(outputVariableNameString),
        isRepeated: Boolean = this.isRepeated,
    ): Pair<String, Array<Any>> = when (typeName.copy(nullable = false)) { //Make non-null for compare only type
        BYTE_ARRAY -> "$variableName.toByteArray()" to variableNameArguments
        DOUBLE, FLOAT, LONG, INT, BOOLEAN, STRING -> variableName to variableNameArguments
        U_LONG, U_INT -> "$variableName.%M" to arrayOf(
            *variableNameArguments,
            MemberName("kr.jadekim.protobuf.util", "asKotlinType"),
        )

        else -> {
            if (type == Descriptors.FieldDescriptor.Type.ENUM) {
                "%T.values()[${variableName}.ordinal]" to arrayOf(typeName, *variableNameArguments)
            } else if (isMapField) {
                val (keyCode, keyArguments) = getCodeForToKotlin(
                    (typeName as ParameterizedTypeName).typeArguments[0],
                    "it.key",
                    emptyArray(),
                    false,
                )
                val (valueCode, valueArguments) = getCodeForToKotlin(
                    (typeName as ParameterizedTypeName).typeArguments[1],
                    "it.value",
                    emptyArray(),
                    false,
                )
                "${variableName}Map.map { $keyCode to $valueCode }.toMap()" to arrayOf(
                    *variableNameArguments,
                    *keyArguments,
                    *valueArguments,
                )
            } else if (isRepeated) {
                val (code, arguments) = getCodeForToKotlin(
                    (typeName as ParameterizedTypeName).typeArguments[0],
                    "it",
                    emptyArray(),
                    false,
                )
                "${variableName}List.map { $code }" to arrayOf(*variableNameArguments, *arguments)
            } else {
                "%T.convert($variableName)" to arrayOf(
                    messageType.mapperTypeName,
                    *variableNameArguments,
                )
            }
        }
    }

    private fun Descriptors.OneofDescriptor.getCodeForToKotlin(): Pair<String, Array<Any>> {
        val oneOfTypeName = outputTypeName
        val code = StringBuilder("mapOf(\n")
        val arguments = mutableListOf<Any>()
        for (field in fields) {
            val fieldTypeName = oneOfTypeName.nestedClass(field.outputOneOfItemTypeNameString)
            val (fieldCode, fieldArguments) = field.getCodeForToKotlin()
            code.appendLine("${field.number} to { %T($fieldCode) },")
            arguments.add(fieldTypeName)
            arguments.addAll(fieldArguments)
        }
        code.append(").getValue(obj.%LCase.number)()")
        arguments.add(outputVariableNameString)

        return code.toString() to arguments.toTypedArray()
    }

    private fun Descriptors.Descriptor.writeToKotlinConvertFunctionTo(
        spec: TypeSpec.Builder,
        protobufTypeName: TypeName,
        kotlinTypeName: TypeName,
    ) {
        val toKotlinTypeFunction = FunSpec.builder("convert")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("obj", protobufTypeName)
            .returns(kotlinTypeName)
            .addCode("return %T(\n", kotlinTypeName)

        for (field in realFields) {
            val (code, arguments) = field.getCodeForToKotlin()
            toKotlinTypeFunction.addCode("\t%L = $code,\n", *arrayOf(field.outputVariableNameString, *arguments))
        }
        for (oneOfs in realOneofs) {
            val (code, arguments) = oneOfs.getCodeForToKotlin()
            toKotlinTypeFunction.addCode("\t%L = $code,\n", *arrayOf(oneOfs.outputVariableNameString, *arguments))
        }

        toKotlinTypeFunction.addCode(")")
        spec.addFunction(toKotlinTypeFunction.build())
    }

    private fun Descriptors.FieldDescriptor.getCodeForToProtobuf(
        typeName: TypeName = outputTypeName,
        variableName: String = "obj.%L",
        variableNameArguments: Array<Any> = arrayOf(outputVariableNameString),
        isRepeated: Boolean = this.isRepeated,
    ): Pair<String, Array<Any>> = when (typeName.copy(nullable = false)) {
        BYTE_ARRAY -> "%T.copyFrom($variableName)" to arrayOf(ByteString::class, *variableNameArguments)
        DOUBLE, FLOAT, LONG, INT, BOOLEAN, STRING -> variableName to variableNameArguments
        U_LONG, U_INT -> "$variableName.%M" to arrayOf(
            *variableNameArguments,
            MemberName("kr.jadekim.protobuf.util", "asJavaType"),
        )

        else -> {
            if (type == Descriptors.FieldDescriptor.Type.ENUM) {
                "${variableName}.ordinal" to variableNameArguments
            } else if (isMapField) {
                val (keyCode, keyArguments) = getCodeForToProtobuf(
                    (typeName as ParameterizedTypeName).typeArguments[0],
                    "it.key",
                    emptyArray(),
                    false,
                )
                val (valueCode, valueArguments) = getCodeForToProtobuf(
                    (typeName as ParameterizedTypeName).typeArguments[1],
                    "it.value",
                    emptyArray(),
                    false,
                )
                "$variableName.map { $keyCode to $valueCode }.toMap()" to arrayOf(
                    *variableNameArguments,
                    *keyArguments,
                    *valueArguments,
                )
            } else if (isRepeated) {
                val (code, arguments) = getCodeForToProtobuf(
                    (typeName as ParameterizedTypeName).typeArguments[0],
                    "it",
                    emptyArray(),
                    false,
                )
                "$variableName.map { $code }" to arrayOf(*variableNameArguments, *arguments)
            } else {
                "%T.convert($variableName)" to arrayOf(messageType.mapperTypeName, *variableNameArguments)
            }
        }
    }

    private fun Descriptors.Descriptor.writeToProtobufConvertFunctionTo(
        spec: TypeSpec.Builder,
        protobufTypeName: TypeName,
        kotlinTypeName: TypeName
    ) {
        val toKotlinTypeFunction = FunSpec.builder("convert")
            .addParameter("obj", kotlinTypeName)
            .addModifiers(KModifier.OVERRIDE)
            .returns(protobufTypeName)
            .addStatement("val builder = %T.newBuilder()", protobufTypeName)

        for (field in realFields) {
            val (code, arguments) = field.getCodeForToProtobuf()
            val function = if (field.type == Descriptors.FieldDescriptor.Type.ENUM) {
                "set%LValue"
            } else if (field.isMapField) {
                "putAll%L"
            } else if (field.isRepeated) {
                "addAll%L"
            } else {
                "set%L"
            }

            if (field.hasOptionalKeyword()) {
                toKotlinTypeFunction.addStatement("val value${field.index} = $code", *arguments)
                toKotlinTypeFunction.beginControlFlow("if (value${field.index} != null)")
                toKotlinTypeFunction.addStatement("builder.$function(value${field.index})", field.name.toPascalCase())
                toKotlinTypeFunction.endControlFlow()
            } else {
                toKotlinTypeFunction.addStatement(
                    "builder.$function($code)",
                    *arrayOf(field.name.toPascalCase(), *arguments)
                )
            }
        }
        for (oneOf in realOneofs) {
            val oneOfTypeName = oneOf.outputTypeName
            toKotlinTypeFunction.beginControlFlow("when (obj.%L)", oneOf.outputVariableNameString)

            for (field in oneOf.fields) {
                val (fieldCode, fieldVariable) = field.getCodeForToProtobuf(
                    variableName = "obj.%L.value",
                    variableNameArguments = arrayOf(oneOf.outputVariableNameString),
                )
                toKotlinTypeFunction.addStatement(
                    "is %T -> builder.set%L($fieldCode)",
                    oneOfTypeName.nestedClass(field.outputOneOfItemTypeNameString),
                    field.name.toPascalCase(),
                    *fieldVariable,
                )
            }

            toKotlinTypeFunction.endControlFlow()
        }

        toKotlinTypeFunction.addStatement("return builder.build()")
        spec.addFunction(toKotlinTypeFunction.build())
    }

    private fun Descriptors.Descriptor.readChildren(spec: TypeSpec.Builder, imports: MutableSet<Import>) {
        for (nestedType in nestedTypes.filterNot { it.options.mapEntry }) {
            val (childType, childImports) = generate(nestedType)
            imports.addAll(childImports)
            spec.addType(childType)
        }
    }
}