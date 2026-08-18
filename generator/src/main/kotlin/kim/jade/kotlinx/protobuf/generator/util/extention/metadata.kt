package kim.jade.kotlinx.protobuf.generator.util.extention

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.BYTE_ARRAY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.joinToCode
import kim.jade.kotlinx.protobuf.annotation.ProtobufEnum
import kim.jade.kotlinx.protobuf.annotation.ProtobufEnumValue
import kim.jade.kotlinx.protobuf.annotation.ProtobufField
import kim.jade.kotlinx.protobuf.annotation.ProtobufFile
import kim.jade.kotlinx.protobuf.type.ProtobufLabel
import kim.jade.kotlinx.protobuf.annotation.ProtobufMapEntry
import kim.jade.kotlinx.protobuf.annotation.ProtobufMessage
import kim.jade.kotlinx.protobuf.annotation.ProtobufMethod
import kim.jade.kotlinx.protobuf.annotation.ProtobufOneOf
import kim.jade.kotlinx.protobuf.annotation.ProtobufService
import kim.jade.kotlinx.protobuf.type.ProtobufType
import java.util.Base64
import kotlin.reflect.KClass

private fun annotation(type: KClass<out Annotation>, members: List<Pair<String, CodeBlock>>): AnnotationSpec =
    AnnotationSpec.builder(type)
        .addMember(members.map { (name, value) -> CodeBlock.of("%L = %L", name, value) }.joinToCode(", "))
        .build()

private fun string(value: String): CodeBlock = CodeBlock.of("%S", value)

private fun ProtobufType.asCode(): CodeBlock = CodeBlock.of("%T.%N", ProtobufType::class, name)

private fun ProtobufLabel.asCode(): CodeBlock = CodeBlock.of("%T.%N", ProtobufLabel::class, name)

private val Descriptors.FieldDescriptor.protobufType: ProtobufType
    get() = when (type) {
        Descriptors.FieldDescriptor.Type.DOUBLE -> ProtobufType.DOUBLE
        Descriptors.FieldDescriptor.Type.FLOAT -> ProtobufType.FLOAT
        Descriptors.FieldDescriptor.Type.INT64 -> ProtobufType.INT64
        Descriptors.FieldDescriptor.Type.UINT64 -> ProtobufType.UINT64
        Descriptors.FieldDescriptor.Type.INT32 -> ProtobufType.INT32
        Descriptors.FieldDescriptor.Type.FIXED64 -> ProtobufType.FIXED64
        Descriptors.FieldDescriptor.Type.FIXED32 -> ProtobufType.FIXED32
        Descriptors.FieldDescriptor.Type.BOOL -> ProtobufType.BOOL
        Descriptors.FieldDescriptor.Type.STRING -> ProtobufType.STRING
        Descriptors.FieldDescriptor.Type.GROUP -> ProtobufType.GROUP
        Descriptors.FieldDescriptor.Type.MESSAGE -> ProtobufType.MESSAGE
        Descriptors.FieldDescriptor.Type.BYTES -> ProtobufType.BYTES
        Descriptors.FieldDescriptor.Type.UINT32 -> ProtobufType.UINT32
        Descriptors.FieldDescriptor.Type.ENUM -> ProtobufType.ENUM
        Descriptors.FieldDescriptor.Type.SFIXED32 -> ProtobufType.SFIXED32
        Descriptors.FieldDescriptor.Type.SFIXED64 -> ProtobufType.SFIXED64
        Descriptors.FieldDescriptor.Type.SINT32 -> ProtobufType.SINT32
        Descriptors.FieldDescriptor.Type.SINT64 -> ProtobufType.SINT64
        null -> throw NullPointerException()
    }

private val Descriptors.FieldDescriptor.protobufLabel: ProtobufLabel
    get() = when {
        isRepeated -> ProtobufLabel.REPEATED
        isRequired -> ProtobufLabel.REQUIRED
        else -> ProtobufLabel.OPTIONAL
    }

private val Descriptors.FieldDescriptor.protobufTypeName: String
    get() = when (type) {
        Descriptors.FieldDescriptor.Type.GROUP, Descriptors.FieldDescriptor.Type.MESSAGE -> messageType.fullName
        Descriptors.FieldDescriptor.Type.ENUM -> enumType.fullName
        else -> ""
    }

private val Descriptors.FieldDescriptor.declaredOneOfName: String
    get() = if (toProto().proto3Optional) "" else containingOneof?.name.orEmpty()

fun FileSpec.Builder.addProtobufFileAnnotation(descriptor: Descriptors.FileDescriptor): FileSpec.Builder = apply {
    val proto = descriptor.toProto()

    val members = mutableListOf(
        "path" to string(descriptor.name),
        "protoPackage" to string(descriptor.`package`),
        "syntax" to string(proto.syntax.ifEmpty { "proto2" }),
    )

    if (proto.hasEdition()) {
        members += "edition" to string(proto.edition.name)
    }

    if (proto.dependencyCount > 0) {
        members += "dependencies" to CodeBlock.of("[%L]", proto.dependencyList.map(::string).joinToCode(", "))
    }

    addAnnotation(annotation(ProtobufFile::class, members))
}

fun TypeSpec.Builder.addMessageAnnotation(descriptor: Descriptors.Descriptor): TypeSpec.Builder = apply {
    addAnnotation(
        annotation(
            ProtobufMessage::class,
            listOf(
                "typeUrl" to CodeBlock.of("%T.TYPE_URL", descriptor.outputTypeName),
                "name" to string(descriptor.fullName),
            ),
        )
    )
}

fun TypeSpec.Builder.addEnumAnnotation(descriptor: Descriptors.EnumDescriptor): TypeSpec.Builder = apply {
    addAnnotation(
        annotation(
            ProtobufEnum::class,
            listOf(
                "typeUrl" to CodeBlock.of("%T.TYPE_URL", descriptor.outputTypeName),
                "name" to string(descriptor.fullName),
            ),
        )
    )
}

fun TypeSpec.Builder.addEnumValueAnnotation(descriptor: Descriptors.EnumValueDescriptor): TypeSpec.Builder = apply {
    addAnnotation(
        annotation(
            ProtobufEnumValue::class,
            listOf("name" to string(descriptor.name), "number" to CodeBlock.of("%L", descriptor.number)),
        )
    )
}

fun TypeSpec.Builder.addOneOfAnnotation(descriptor: Descriptors.OneofDescriptor): TypeSpec.Builder = apply {
    addAnnotation(annotation(ProtobufOneOf::class, listOf("name" to string(descriptor.name))))
}

fun TypeSpec.Builder.addServiceAnnotation(descriptor: Descriptors.ServiceDescriptor): TypeSpec.Builder = apply {
    addAnnotation(annotation(ProtobufService::class, listOf("name" to string(descriptor.fullName))))
}

fun FunSpec.Builder.addMethodAnnotation(descriptor: Descriptors.MethodDescriptor): FunSpec.Builder = apply {
    val members = mutableListOf(
        "name" to string(descriptor.name),
        "inputType" to string(descriptor.inputType.fullName),
        "outputType" to string(descriptor.outputType.fullName),
    )

    if (descriptor.isClientStreaming) {
        members += "clientStreaming" to CodeBlock.of("%L", true)
    }

    if (descriptor.isServerStreaming) {
        members += "serverStreaming" to CodeBlock.of("%L", true)
    }

    addAnnotation(annotation(ProtobufMethod::class, members))
}

fun ParameterSpec.Builder.addFieldAnnotations(descriptor: Descriptors.FieldDescriptor): ParameterSpec.Builder =
    apply {
        addAnnotation(descriptor.fieldAnnotation())

        if (descriptor.isMapField) {
            addAnnotation(descriptor.mapEntryAnnotation())
        }
    }

private fun Descriptors.FieldDescriptor.fieldAnnotation(): AnnotationSpec {
    val members = mutableListOf(
        "name" to string(name),
        "number" to CodeBlock.of("%L", number),
        "jsonName" to string(jsonName),
        "type" to protobufType.asCode(),
        "label" to protobufLabel.asCode(),
    )

    protobufTypeName.takeIf { it.isNotEmpty() }?.let { members += "typeName" to string(it) }
    declaredOneOfName.takeIf { it.isNotEmpty() }?.let { members += "oneOf" to string(it) }

    if (toProto().proto3Optional) {
        members += "proto3Optional" to CodeBlock.of("%L", true)
    }

    return annotation(ProtobufField::class, members)
}

private fun Descriptors.FieldDescriptor.mapEntryAnnotation(): AnnotationSpec {
    val members = mutableListOf(
        "typeName" to string(messageType.fullName),
        "keyType" to mapKeyField.protobufType.asCode(),
        "valueType" to mapValueField.protobufType.asCode(),
    )

    mapValueField.protobufTypeName.takeIf { it.isNotEmpty() }?.let { members += "valueTypeName" to string(it) }

    return annotation(ProtobufMapEntry::class, members)
}

private val KOTLIN_BASE64: ClassName = ClassName("kotlin.io.encoding", "Base64")

private const val DESCRIPTOR_PAYLOAD_NAME = "DESCRIPTOR_BASE64"

fun TypeSpec.Builder.addDescriptorBytes(descriptor: Descriptors.GenericDescriptor): TypeSpec.Builder = apply {
    addProperty(
        PropertySpec.builder("descriptorBytes", BYTE_ARRAY)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return %T.decode(%N)", KOTLIN_BASE64, DESCRIPTOR_PAYLOAD_NAME)
                    .build()
            )
            .build()
    )

    addProperty(
        PropertySpec.builder(DESCRIPTOR_PAYLOAD_NAME, STRING)
            .addModifiers(KModifier.PRIVATE, KModifier.CONST)
            .initializer("%S", Base64.getEncoder().encodeToString(descriptor.toProto().toByteArray()))
            .build()
    )
}
