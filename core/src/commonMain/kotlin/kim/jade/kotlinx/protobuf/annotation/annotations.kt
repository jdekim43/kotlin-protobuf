package kim.jade.kotlinx.protobuf.annotation

import kim.jade.kotlinx.protobuf.type.ProtobufLabel
import kim.jade.kotlinx.protobuf.type.ProtobufType

@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class GeneratorVersion(val version: String)

@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProtobufFile(
    val path: String,
    val protoPackage: String,
    val syntax: String,
    val edition: String = "",
    val dependencies: Array<String> = [],
)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProtobufMessage(val typeUrl: String, val name: String = "")

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProtobufEnum(val typeUrl: String, val name: String)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProtobufOneOf(val name: String)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProtobufField(
    val name: String,
    val number: Int,
    val jsonName: String,
    val type: ProtobufType,
    val label: ProtobufLabel,
    val typeName: String = "",
    val oneOf: String = "",
    val proto3Optional: Boolean = false,
)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProtobufEnumValue(val name: String, val number: Int)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProtobufMapEntry(
    val typeName: String,
    val keyType: ProtobufType,
    val valueType: ProtobufType,
    val valueTypeName: String = "",
)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProtobufService(val name: String)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProtobufMethod(
    val name: String,
    val inputType: String,
    val outputType: String,
    val clientStreaming: Boolean = false,
    val serverStreaming: Boolean = false,
)

@Target(
    AnnotationTarget.FILE,
    AnnotationTarget.CLASS,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class ProtobufOption(val key: String, val value: String)

