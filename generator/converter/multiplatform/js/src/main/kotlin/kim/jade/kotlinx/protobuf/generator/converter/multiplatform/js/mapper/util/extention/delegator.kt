package kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.mapper.util.extention

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kim.jade.kotlinx.protobuf.generator.util.ProtobufWordSplitter
import kim.jade.kotlinx.protobuf.generator.util.extention.fileName
import kim.jade.kotlinx.protobuf.generator.util.extention.outputPackage
import kim.jade.kotlinx.protobuf.generator.util.extention.simpleNames
import net.pearx.kasechange.toCamelCase

private const val DELEGATOR_PACKAGE = "delegator-protobufjs"

object JsRuntime {

    private const val CONVERTER_PACKAGE = "kim.jade.kotlinx.protobuf.converter.mapper"
    private const val PROTOBUF_JS_PACKAGE = "kim.jade.kotlinx.protobuf.converter.protobufjs"
    private const val UTIL_PACKAGE = "kim.jade.kotlinx.protobuf.util"

    val UINT8_ARRAY: ClassName = ClassName("org.khronos.webgl", "Uint8Array")

    val PROTOBUF_JS_MAPPER: ClassName = ClassName(CONVERTER_PACKAGE, "ProtobufJsMapper")

    val PROTOBUF_JS_FILE: ClassName = ClassName(PROTOBUF_JS_PACKAGE, "ProtobufJsFile")

    val PROTOBUF_JS_MESSAGE: ClassName = ClassName(PROTOBUF_JS_PACKAGE, "ProtobufJsMessage")

    val PROTOBUF_JS_MAP: ClassName = ClassName(PROTOBUF_JS_PACKAGE, "ProtobufJsMap")

    val PROTOBUF_JS_LONG: ClassName = ClassName(PROTOBUF_JS_PACKAGE, "ProtobufJsLong")

    val NEW_MESSAGE: MemberName = MemberName(PROTOBUF_JS_PACKAGE, "protobufJsMessage")

    val NEW_MAP: MemberName = MemberName(PROTOBUF_JS_PACKAGE, "protobufJsMap")

    val MAP_ENTRIES: MemberName = MemberName(PROTOBUF_JS_PACKAGE, "entries", true)

    val MAP_SET: MemberName = MemberName(PROTOBUF_JS_PACKAGE, "set", true)

    val TO_LONG: MemberName = MemberName(PROTOBUF_JS_PACKAGE, "toLong", true)

    val TO_U_LONG: MemberName = MemberName(PROTOBUF_JS_PACKAGE, "toULong", true)

    val TO_PROTOBUF_JS_LONG: MemberName = MemberName(PROTOBUF_JS_PACKAGE, "toProtobufJsLong", true)

    val TO_BYTE_ARRAY: MemberName = MemberName(UTIL_PACKAGE, "toByteArray", true)

    val TO_UINT8_ARRAY: MemberName = MemberName(UTIL_PACKAGE, "toUint8Array", true)
}

val Descriptors.GenericDescriptor.delegatorPackage: String
    get() = outputPackage.let { if (it.isBlank()) DELEGATOR_PACKAGE else "$it.$DELEGATOR_PACKAGE" }

val Descriptors.GenericDescriptor.delegatorTypeName: ClassName
    get() = ClassName(delegatorPackage, simpleNames)

val Descriptors.FileDescriptor.protobufJsFileMemberName: MemberName
    get() = MemberName(outputPackage, fileName.toCamelCase(ProtobufWordSplitter) + "ProtobufJsFile")

val Descriptors.FieldDescriptor.delegatorPropertyName: String
    get() = jsonName.ifBlank { name }

val Descriptors.OneofDescriptor.delegatorPropertyName: String
    get() = name

private const val MAP_KEY_FIELD_NUMBER = 1
private const val MAP_VALUE_FIELD_NUMBER = 2

val Descriptors.FieldDescriptor.mapKeyField: Descriptors.FieldDescriptor
    get() = messageType.findFieldByNumber(MAP_KEY_FIELD_NUMBER)

val Descriptors.FieldDescriptor.mapValueField: Descriptors.FieldDescriptor
    get() = messageType.findFieldByNumber(MAP_VALUE_FIELD_NUMBER)

val Descriptors.FieldDescriptor.delegatorElementTypeName: TypeName
    get() = when (type) {
        Descriptors.FieldDescriptor.Type.DOUBLE -> DOUBLE
        Descriptors.FieldDescriptor.Type.FLOAT -> FLOAT
        Descriptors.FieldDescriptor.Type.INT64,
        Descriptors.FieldDescriptor.Type.SFIXED64,
        Descriptors.FieldDescriptor.Type.SINT64,
        Descriptors.FieldDescriptor.Type.UINT64,
        Descriptors.FieldDescriptor.Type.FIXED64 -> JsRuntime.PROTOBUF_JS_LONG

        Descriptors.FieldDescriptor.Type.INT32,
        Descriptors.FieldDescriptor.Type.SFIXED32,
        Descriptors.FieldDescriptor.Type.SINT32,
        Descriptors.FieldDescriptor.Type.ENUM -> INT

        // Not Int: protobuf.js decodes an unsigned 32-bit field to a JS number in [0, 2^32), and the top
        // half of that range does not fit in a Kotlin Int.
        Descriptors.FieldDescriptor.Type.UINT32,
        Descriptors.FieldDescriptor.Type.FIXED32 -> DOUBLE

        Descriptors.FieldDescriptor.Type.BOOL -> BOOLEAN
        Descriptors.FieldDescriptor.Type.STRING -> STRING
        Descriptors.FieldDescriptor.Type.BYTES -> JsRuntime.UINT8_ARRAY
        Descriptors.FieldDescriptor.Type.GROUP,
        Descriptors.FieldDescriptor.Type.MESSAGE -> messageType.delegatorTypeName

        null -> throw NullPointerException()
    }

val Descriptors.FieldDescriptor.delegatorFieldTypeName: TypeName
    get() = when {
        isMapField -> JsRuntime.PROTOBUF_JS_MAP.parameterizedBy(mapValueField.delegatorElementTypeName)
        isRepeated -> ARRAY.parameterizedBy(delegatorElementTypeName)
        else -> delegatorElementTypeName
    }
