package kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.mapper

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kim.jade.kotlinx.protobuf.generator.ImportName
import kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.mapper.util.extention.*
import kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.util.extention.jsConverterTypeName
import kim.jade.kotlinx.protobuf.generator.converter.util.extention.converterTypeName
import kim.jade.kotlinx.protobuf.generator.util.extention.*

class MessageJsMapperGenerator : JsMapperGenerator<Descriptors.Descriptor> {

    override fun generate(descriptor: Descriptors.Descriptor): Pair<TypeSpec, Set<ImportName>> {
        val outputTypeName = descriptor.outputTypeName
        val delegatorTypeName = descriptor.delegatorTypeName
        val name = descriptor.jsConverterTypeName
        val spec = TypeSpec.classBuilder(name).addModifiers(KModifier.OPEN)
        val imports = mutableSetOf<ImportName>()

        val parentType = JsRuntime.PROTOBUF_JS_MAPPER.parameterizedBy(outputTypeName, delegatorTypeName)
        spec.addSuperinterface(parentType)

        descriptor.writeVariablesTo(spec)
        descriptor.writeToKotlinConvertFunctionTo(spec, delegatorTypeName, outputTypeName)
        descriptor.writeToJsConvertFunctionTo(spec, delegatorTypeName, outputTypeName)

        descriptor.readChildren(spec, imports)

        return spec.build() to imports.toSet()
    }

    private fun Descriptors.Descriptor.writeVariablesTo(spec: TypeSpec.Builder) {
        spec.addProperty(
            PropertySpec.builder("typeName", STRING)
                .addModifiers(KModifier.OVERRIDE)
                .initializer("%S", fullName)
                .build()
        )

        spec.addProperty(
            PropertySpec.builder("protobufJsFile", JsRuntime.PROTOBUF_JS_FILE)
                .addModifiers(KModifier.OVERRIDE)
                .initializer("%M", file.protobufJsFileMemberName)
                .build()
        )
    }

    private fun Descriptors.FieldDescriptor.getElementCodeForToKotlin(
        variableName: String,
        variableNameArguments: Array<Any> = emptyArray(),
    ): Pair<String, Array<Any>> = when (type) {
        Descriptors.FieldDescriptor.Type.INT64,
        Descriptors.FieldDescriptor.Type.SFIXED64,
        Descriptors.FieldDescriptor.Type.SINT64 ->
            "$variableName.%M()" to arrayOf(*variableNameArguments, JsRuntime.TO_LONG)

        Descriptors.FieldDescriptor.Type.UINT64,
        Descriptors.FieldDescriptor.Type.FIXED64 ->
            "$variableName.%M()" to arrayOf(*variableNameArguments, JsRuntime.TO_U_LONG)

        Descriptors.FieldDescriptor.Type.UINT32,
        Descriptors.FieldDescriptor.Type.FIXED32 -> "$variableName.toLong().toUInt()" to variableNameArguments

        Descriptors.FieldDescriptor.Type.BYTES ->
            "$variableName.%M()" to arrayOf(*variableNameArguments, JsRuntime.TO_BYTE_ARRAY)

        Descriptors.FieldDescriptor.Type.ENUM ->
            "%T.forNumber($variableName)" to arrayOf(enumType.outputTypeName, *variableNameArguments)

        Descriptors.FieldDescriptor.Type.GROUP,
        Descriptors.FieldDescriptor.Type.MESSAGE ->
            "%T.convert($variableName)" to arrayOf(messageType.converterTypeName, *variableNameArguments)

        else -> variableName to variableNameArguments
    }

    private fun Descriptors.FieldDescriptor.getMapKeyCodeForToKotlin(variableName: String): String =
        when (type) {
            Descriptors.FieldDescriptor.Type.INT64,
            Descriptors.FieldDescriptor.Type.SFIXED64,
            Descriptors.FieldDescriptor.Type.SINT64 -> "$variableName.toLong()"

            Descriptors.FieldDescriptor.Type.UINT64,
            Descriptors.FieldDescriptor.Type.FIXED64 -> "$variableName.toULong()"

            Descriptors.FieldDescriptor.Type.UINT32,
            Descriptors.FieldDescriptor.Type.FIXED32 -> "$variableName.toUInt()"

            Descriptors.FieldDescriptor.Type.INT32,
            Descriptors.FieldDescriptor.Type.SFIXED32,
            Descriptors.FieldDescriptor.Type.SINT32 -> "$variableName.toInt()"

            Descriptors.FieldDescriptor.Type.BOOL -> "$variableName.toBooleanStrict()"

            else -> variableName
        }

    private fun Descriptors.FieldDescriptor.getCodeForToKotlin(): Pair<String, Array<Any>> {
        if (isMapField) {
            val keyCode = mapKeyField.getMapKeyCodeForToKotlin("key")
            val (valueCode, valueArguments) = mapValueField.getElementCodeForToKotlin("value")

            if (keyCode == "key" && valueCode == "value") {
                return "obj.%N.%M().toMap()" to arrayOf(delegatorPropertyName, JsRuntime.MAP_ENTRIES)
            }

            return "obj.%N.%M().map { (key, value) -> $keyCode to $valueCode }.toMap()" to arrayOf(
                delegatorPropertyName,
                JsRuntime.MAP_ENTRIES,
                *valueArguments,
            )
        }

        if (isRepeated) {
            val (code, arguments) = getElementCodeForToKotlin("it")

            if (code == "it") {
                return "obj.%N.toList()" to arrayOf<Any>(delegatorPropertyName)
            }

            return "obj.%N.map { $code }" to arrayOf(delegatorPropertyName, *arguments)
        }

        return getElementCodeForToKotlin("obj.%N", arrayOf(delegatorPropertyName))
    }

    private fun Descriptors.OneofDescriptor.getCodeForToKotlin(): Pair<String, Array<Any>> {
        val oneOfTypeName = outputTypeName
        val code = StringBuilder("when (obj.%N) {\n")
        val arguments = mutableListOf<Any>(delegatorPropertyName)

        for (field in fields) {
            val fieldTypeName = oneOfTypeName.nestedClass(field.outputOneOfItemTypeNameString)
            val (fieldCode, fieldArguments) = field.getCodeForToKotlin()
            code.appendLine("\t\t%S -> %T($fieldCode)")
            arguments.add(field.delegatorPropertyName)
            arguments.add(fieldTypeName)
            arguments.addAll(fieldArguments)
        }

        code.append("\t\telse -> null\n\t}")

        return code.toString() to arguments.toTypedArray()
    }

    private fun Descriptors.Descriptor.writeToKotlinConvertFunctionTo(
        spec: TypeSpec.Builder,
        delegatorTypeName: TypeName,
        kotlinTypeName: TypeName,
    ) {
        val toKotlinTypeFunction = FunSpec.builder("convert")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("obj", delegatorTypeName)
            .returns(kotlinTypeName)
            .addCode("return %T(\n", kotlinTypeName)

        for (field in realFields) {
            val (code, arguments) = field.getCodeForToKotlin()

            if (field.isNullable) {
                toKotlinTypeFunction.addCode(
                    "\t%N = if (obj.hasOwnProperty(%S)) $code else null,\n",
                    *arrayOf(field.outputVariableNameString, field.delegatorPropertyName, *arguments),
                )
            } else {
                toKotlinTypeFunction.addCode(
                    "\t%N = $code,\n",
                    *arrayOf(field.outputVariableNameString, *arguments),
                )
            }
        }

        for (oneOf in realOneofs) {
            val (code, arguments) = oneOf.getCodeForToKotlin()
            toKotlinTypeFunction.addCode("\t%N = $code,\n", *arrayOf(oneOf.outputVariableNameString, *arguments))
        }

        toKotlinTypeFunction.addCode(")")
        spec.addFunction(toKotlinTypeFunction.build())
    }

    private fun Descriptors.FieldDescriptor.getElementCodeForToJs(
        variableName: String,
        variableNameArguments: Array<Any> = emptyArray(),
    ): Pair<String, Array<Any>> = when (type) {
        Descriptors.FieldDescriptor.Type.INT64,
        Descriptors.FieldDescriptor.Type.SFIXED64,
        Descriptors.FieldDescriptor.Type.SINT64,
        Descriptors.FieldDescriptor.Type.UINT64,
        Descriptors.FieldDescriptor.Type.FIXED64 ->
            "$variableName.%M()" to arrayOf(*variableNameArguments, JsRuntime.TO_PROTOBUF_JS_LONG)

        Descriptors.FieldDescriptor.Type.UINT32,
        Descriptors.FieldDescriptor.Type.FIXED32 -> "$variableName.toDouble()" to variableNameArguments

        Descriptors.FieldDescriptor.Type.BYTES ->
            "$variableName.%M()" to arrayOf(*variableNameArguments, JsRuntime.TO_UINT8_ARRAY)

        Descriptors.FieldDescriptor.Type.ENUM -> "$variableName.number" to variableNameArguments

        Descriptors.FieldDescriptor.Type.GROUP,
        Descriptors.FieldDescriptor.Type.MESSAGE ->
            "%T.convert($variableName)" to arrayOf(messageType.converterTypeName, *variableNameArguments)

        else -> variableName to variableNameArguments
    }

    private fun Descriptors.Descriptor.writeToJsConvertFunctionTo(
        spec: TypeSpec.Builder,
        delegatorTypeName: TypeName,
        kotlinTypeName: TypeName,
    ) {
        val toJsTypeFunction = FunSpec.builder("convert")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("obj", kotlinTypeName)
            .returns(delegatorTypeName)
            .addStatement("val delegator = %M<%T>()", JsRuntime.NEW_MESSAGE, delegatorTypeName)

        for (field in realFields) {
            field.writeToJsAssignmentTo(toJsTypeFunction)
        }

        for (oneOf in realOneofs) {
            val oneOfTypeName = oneOf.outputTypeName
            toJsTypeFunction.beginControlFlow("when (obj.%N)", oneOf.outputVariableNameString)

            for (field in oneOf.fields) {
                val (code, arguments) = field.getElementCodeForToJs(
                    "obj.%N.value",
                    arrayOf(oneOf.outputVariableNameString),
                )
                toJsTypeFunction.addStatement(
                    "is %T -> delegator.%N = $code",
                    oneOfTypeName.nestedClass(field.outputOneOfItemTypeNameString),
                    field.delegatorPropertyName,
                    *arguments,
                )
            }

            toJsTypeFunction.addStatement("null -> Unit")
            toJsTypeFunction.endControlFlow()
        }

        toJsTypeFunction.addStatement("return delegator")
        spec.addFunction(toJsTypeFunction.build())
    }

    private fun Descriptors.FieldDescriptor.writeToJsAssignmentTo(function: FunSpec.Builder) {
        if (isMapField) {
            val (valueCode, valueArguments) = mapValueField.getElementCodeForToJs("value")
            val local = "map$index"

            function.addStatement(
                "val %N = %M<%T>()",
                local,
                JsRuntime.NEW_MAP,
                mapValueField.delegatorElementTypeName,
            )
            function.beginControlFlow("for ((key, value) in obj.%N)", outputVariableNameString)
            function.addStatement("%N.%M(key.toString(), $valueCode)", *arrayOf(local, JsRuntime.MAP_SET, *valueArguments))
            function.endControlFlow()
            function.addStatement("delegator.%N = %N", delegatorPropertyName, local)
            return
        }

        if (isRepeated) {
            val (code, arguments) = getElementCodeForToJs("it")

            if (code == "it") {
                function.addStatement(
                    "delegator.%N = obj.%N.toTypedArray()",
                    delegatorPropertyName,
                    outputVariableNameString,
                )
            } else {
                function.addStatement(
                    "delegator.%N = obj.%N.map { $code }.toTypedArray()",
                    *arrayOf(delegatorPropertyName, outputVariableNameString, *arguments),
                )
            }
            return
        }

        if (isNullable) {
            val (code, arguments) = getElementCodeForToJs("value$index")

            function.addStatement("val value$index = obj.%N", outputVariableNameString)
            function.beginControlFlow("if (value$index != null)")
            function.addStatement("delegator.%N = $code", *arrayOf(delegatorPropertyName, *arguments))
            function.endControlFlow()
            return
        }

        val (code, arguments) = getElementCodeForToJs("obj.%N", arrayOf(outputVariableNameString))
        function.addStatement("delegator.%N = $code", *arrayOf(delegatorPropertyName, *arguments))
    }

    private fun Descriptors.Descriptor.readChildren(spec: TypeSpec.Builder, imports: MutableSet<ImportName>) {
        for (nestedType in nestedTypes.filterNot { it.options.mapEntry }) {
            val (childType, childImports) = generate(nestedType)
            imports.addAll(childImports)
            spec.addType(childType)
        }
    }
}
