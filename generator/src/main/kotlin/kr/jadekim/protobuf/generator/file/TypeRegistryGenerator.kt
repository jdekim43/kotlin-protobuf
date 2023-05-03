package kr.jadekim.protobuf.generator.file

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kr.jadekim.protobuf.converter.mapper.ProtobufTypeMapper
import java.util.*

@Deprecated("", level = DeprecationLevel.ERROR)
class TypeRegistryGenerator(
    val packageName: String = "kr.jadekim.protobuf.registry.${UUID.randomUUID().toString().replace("-", "")}",
    val fileName: String = "type_registry.kt",
) {

    private val enumRegistry = mutableMapOf<String, TypeName>()
    private val messageRegistry = mutableMapOf<String, TypeName>()
    private val messageMapperRegistry = mutableMapOf<String, TypeName>()

    fun generate(): FileSpec {
        val spec = FileSpec.builder(packageName, fileName)

        val enumRegistryBlock = CodeBlock.builder()
        enumRegistryBlock.add("mapOf<%T, %T>(", String::class, Descriptors.EnumDescriptor::class)
        for ((typeUrl, typeName) in enumRegistry) {
            enumRegistryBlock.add("%S to %T.getDescriptor(),\n", typeUrl, typeName)
        }
        enumRegistryBlock.add(")")
        spec.addProperty(
            PropertySpec.builder("ENUM_REGISTRY", MAP.parameterizedBy(STRING, typeNameOf<Descriptors.EnumDescriptor>()))
                .initializer(enumRegistryBlock.build())
                .build()
        )

        val messageRegistryBlock = CodeBlock.builder()
        messageRegistryBlock.add("mapOf<%T, %T>(", String::class, Descriptors.Descriptor::class)
        for ((typeUrl, typeName) in messageRegistry) {
            messageRegistryBlock.add("%S to %T.getDescriptor(),\n", typeUrl, typeName)
        }
        messageRegistryBlock.add(")")
        spec.addProperty(
            PropertySpec.builder("MESSAGE_REGISTRY", MAP.parameterizedBy(STRING, typeNameOf<Descriptors.Descriptor>()))
                .initializer(messageRegistryBlock.build())
                .build()
        )

        val messageMapperRegistryBlock = CodeBlock.builder()
        messageMapperRegistryBlock.add("mapOf<%T, %T>(", STRING, typeNameOf<ProtobufTypeMapper<*, *>>())
        for ((typeUrl, typeName) in messageMapperRegistry) {
            messageMapperRegistryBlock.add("%S to %T,\n", typeUrl, typeName)
        }
        messageMapperRegistryBlock.add(")")
        spec.addProperty(
            PropertySpec.builder(
                "MESSAGE_MAPPER_REGISTRY",
                MAP.parameterizedBy(STRING, typeNameOf<ProtobufTypeMapper<*, *>>())
            )
                .initializer(messageMapperRegistryBlock.build())
                .build()
        )

        return spec.build()
    }

    fun registerEnum(typeUrl: String, typeName: ClassName) {
        enumRegistry[typeUrl] = typeName
    }

    fun registerMessage(typeUrl: String, typeName: ClassName) {
        messageRegistry[typeUrl] = typeName
    }

    fun registerMessageMapper(typeUrl: String, typeName: ClassName) {
        messageMapperRegistry[typeUrl] = typeName
    }
}