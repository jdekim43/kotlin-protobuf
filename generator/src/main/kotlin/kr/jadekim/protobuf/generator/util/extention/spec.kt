package kr.jadekim.protobuf.generator.util.extention

import com.google.protobuf.Descriptors
import com.google.protobuf.compiler.PluginProtos
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kr.jadekim.protobuf.annotation.ProtobufIndex
import net.pearx.kasechange.toPascalCase
import kotlin.reflect.KClass

fun TypeSpec.Builder.addNumberAnnotation(number: Int) {
    addAnnotation(AnnotationSpec.builder(ProtobufIndex::class).addMember("index = %L", number).build())
}

fun ParameterSpec.Builder.addNumberAnnotation(number: Int) {
    addAnnotation(AnnotationSpec.builder(ProtobufIndex::class).addMember("index = %L", number).build())
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
        var typeName = when (type) {
            Descriptors.FieldDescriptor.Type.DOUBLE -> DOUBLE
            Descriptors.FieldDescriptor.Type.FLOAT -> FLOAT
            Descriptors.FieldDescriptor.Type.INT64 -> LONG
            Descriptors.FieldDescriptor.Type.UINT64 -> U_LONG
            Descriptors.FieldDescriptor.Type.INT32 -> INT
            Descriptors.FieldDescriptor.Type.FIXED64 -> U_LONG
            Descriptors.FieldDescriptor.Type.FIXED32 -> U_INT
            Descriptors.FieldDescriptor.Type.BOOL -> BOOLEAN
            Descriptors.FieldDescriptor.Type.STRING -> STRING
            Descriptors.FieldDescriptor.Type.GROUP, Descriptors.FieldDescriptor.Type.MESSAGE -> {
                if (isMapField) {
                    MAP.parameterizedBy(
                        messageType.findFieldByNumber(MAP_KEY_FIELD_NUMBER).outputTypeName.copy(nullable = false),
                        messageType.findFieldByNumber(MAP_VALUE_FIELD_NUMBER).outputTypeName.copy(nullable = false),
                    )
                } else {
                    messageType.outputTypeName
                }
            }

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

        return typeName.copy(nullable = hasOptionalKeyword())
    }

val Descriptors.GenericDescriptor.outputTypeName: ClassName
    get() = ClassName(outputPackage, simpleNames)

val Descriptors.OneofDescriptor.outputTypeName: ClassName
    get() = (this as Descriptors.GenericDescriptor).outputTypeName.peerClass(name.toPascalCase())

val Descriptors.ServiceDescriptor.outputTypeName: ClassName
    get() = (this as Descriptors.GenericDescriptor).outputTypeName.peerClass(name.toPascalCase() + "Service")

val KClass<*>.typeName: ClassName
    get() = asTypeName()
