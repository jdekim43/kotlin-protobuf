package kr.jadekim.protobuf.generator.util.extention

import com.google.protobuf.Descriptors
import com.google.protobuf.compiler.PluginProtos
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kr.jadekim.protobuf.annotation.GeneratorVersion
import kr.jadekim.protobuf.annotation.ProtobufIndex
import kr.jadekim.protobuf.annotation.ProtobufSyntax
import kr.jadekim.protobuf.generator.BUILD_VERSION
import kr.jadekim.protobuf.generator.util.ProtobufWordSplitter
import net.pearx.kasechange.toPascalCase
import kotlin.reflect.KClass

fun TypeSpec.Builder.addNumberAnnotation(number: Int) {
    addAnnotation(AnnotationSpec.builder(ProtobufIndex::class).addMember("index = %L", number).build())
}

fun ParameterSpec.Builder.addNumberAnnotation(number: Int) {
    addAnnotation(AnnotationSpec.builder(ProtobufIndex::class).addMember("index = %L", number).build())
}

fun TypeSpec.Builder.addDeprecatedAnnotation(
    message: String,
    replaceWith: String = "",
    level: DeprecationLevel = DeprecationLevel.WARNING,
) {
    addAnnotation(
        AnnotationSpec.builder(Deprecated::class)
            .addMember("message = %S", message)
            .addMember("replaceWith = %T(%S)", ReplaceWith::class, replaceWith)
            .addMember("level = %T.%N", DeprecationLevel::class, level.name)
            .build()
    )
}

fun PropertySpec.Builder.addDeprecatedAnnotation(
    message: String,
    replaceWith: String = "",
    level: DeprecationLevel = DeprecationLevel.WARNING,
) {
    addAnnotation(
        AnnotationSpec.builder(Deprecated::class)
            .addMember("message = %S", message)
            .addMember("replaceWith = %T(%S)", ReplaceWith::class, replaceWith)
            .addMember("level = %T.%N", DeprecationLevel::class, level.name)
            .build()
    )
}

fun FileSpec.Builder.addGeneratorVersionAnnotation() {
    addAnnotation(
        AnnotationSpec.builder(GeneratorVersion::class)
            .addMember("version = %S", BUILD_VERSION)
            .build()
    )
}

fun FileSpec.toResponse(): PluginProtos.CodeGeneratorResponse.File =
    PluginProtos.CodeGeneratorResponse.File.newBuilder()
        .setName(packageName.replace('.', '/') + '/' + name)
        .setContent(toString())
        .build()

private const val MAP_KEY_FIELD_NUMBER = 1
private const val MAP_VALUE_FIELD_NUMBER = 2

@Suppress("RecursivePropertyAccessor")
val Descriptors.FieldDescriptor.outputTypeName: TypeName
    get() {
        if (isMapField) {
            return MAP.parameterizedBy(
                messageType.findFieldByNumber(MAP_KEY_FIELD_NUMBER).outputTypeName.copy(nullable = false),
                messageType.findFieldByNumber(MAP_VALUE_FIELD_NUMBER).outputTypeName.copy(nullable = false),
            )
        }

        var typeName: TypeName = when (type) {
            Descriptors.FieldDescriptor.Type.DOUBLE -> DOUBLE
            Descriptors.FieldDescriptor.Type.FLOAT -> FLOAT
            Descriptors.FieldDescriptor.Type.INT64 -> LONG
            Descriptors.FieldDescriptor.Type.UINT64 -> U_LONG
            Descriptors.FieldDescriptor.Type.INT32 -> INT
            Descriptors.FieldDescriptor.Type.FIXED64 -> U_LONG
            Descriptors.FieldDescriptor.Type.FIXED32 -> U_INT
            Descriptors.FieldDescriptor.Type.BOOL -> BOOLEAN
            Descriptors.FieldDescriptor.Type.STRING -> STRING
            Descriptors.FieldDescriptor.Type.GROUP, Descriptors.FieldDescriptor.Type.MESSAGE -> messageType.outputTypeName
            Descriptors.FieldDescriptor.Type.ENUM -> enumType.outputTypeName
            Descriptors.FieldDescriptor.Type.BYTES -> BYTE_ARRAY
            Descriptors.FieldDescriptor.Type.UINT32 -> U_INT
            Descriptors.FieldDescriptor.Type.SFIXED32 -> INT
            Descriptors.FieldDescriptor.Type.SFIXED64 -> LONG
            Descriptors.FieldDescriptor.Type.SINT32 -> INT
            Descriptors.FieldDescriptor.Type.SINT64 -> LONG
            null -> throw NullPointerException()
        }

        if (isRepeated && !isMapField) {
            typeName = LIST.parameterizedBy(typeName.copy(nullable = false))
        }

        return typeName.copy(nullable = isNullable)
    }

val Descriptors.GenericDescriptor.outputTypeName: ClassName
    get() = ClassName(outputPackage, simpleNames)

val Descriptors.OneofDescriptor.outputTypeName: ClassName
    get() = (this as Descriptors.GenericDescriptor).outputTypeName.peerClass(name.toPascalCase(ProtobufWordSplitter) + "OneOf")

val Descriptors.ServiceDescriptor.outputTypeName: ClassName
    get() = (this as Descriptors.GenericDescriptor).outputTypeName.peerClass(name.toPascalCase(ProtobufWordSplitter))

val KClass<*>.typeName: ClassName
    get() = asTypeName()

val Descriptors.FieldDescriptor.kotlinDefaultValue: CodeBlock
    get() {
        if (isNullable) {
            return CodeBlock.of("null")
        }

        if (isMapField) {
            return CodeBlock.of("%M()", MemberName("kotlin.collections", "emptyMap"))
        }

        if (isRepeated && !isMapField) {
            return CodeBlock.of("%M()", MemberName("kotlin.collections", "emptyList"))
        }

        return when (type) {
            Descriptors.FieldDescriptor.Type.DOUBLE -> CodeBlock.of("0.0")
            Descriptors.FieldDescriptor.Type.FLOAT -> CodeBlock.of("0.0f")
            Descriptors.FieldDescriptor.Type.INT64 -> CodeBlock.of("0L")
            Descriptors.FieldDescriptor.Type.UINT64 -> CodeBlock.of("0uL")
            Descriptors.FieldDescriptor.Type.INT32 -> CodeBlock.of("0")
            Descriptors.FieldDescriptor.Type.FIXED64 -> CodeBlock.of("0uL")
            Descriptors.FieldDescriptor.Type.FIXED32 -> CodeBlock.of("0u")
            Descriptors.FieldDescriptor.Type.BOOL -> CodeBlock.of("false")
            Descriptors.FieldDescriptor.Type.STRING -> CodeBlock.of("%S", "")
            Descriptors.FieldDescriptor.Type.GROUP, Descriptors.FieldDescriptor.Type.MESSAGE -> CodeBlock.of("%T()", messageType.outputTypeName)
            Descriptors.FieldDescriptor.Type.ENUM -> CodeBlock.of("%T.values()[0]", enumType.outputTypeName)
            Descriptors.FieldDescriptor.Type.BYTES -> CodeBlock.of("%M()", MemberName("kotlin", "byteArrayOf"))
            Descriptors.FieldDescriptor.Type.UINT32 -> CodeBlock.of("0u")
            Descriptors.FieldDescriptor.Type.SFIXED32 -> CodeBlock.of("0")
            Descriptors.FieldDescriptor.Type.SFIXED64 -> CodeBlock.of("0L")
            Descriptors.FieldDescriptor.Type.SINT32 -> CodeBlock.of("0")
            Descriptors.FieldDescriptor.Type.SINT64 -> CodeBlock.of("0L")
            null -> throw NullPointerException()
        }
    }
