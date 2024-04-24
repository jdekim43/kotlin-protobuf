package kr.jadekim.protobuf.generator.file

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kr.jadekim.protobuf.generator.util.extention.addGeneratorVersionAnnotation
import kotlin.reflect.KClass

object TypeRegistryGenerator {

    private val enumRegistry = mutableMapOf<String, TypeName>()
    private val messageRegistry = mutableMapOf<String, TypeName>()

    fun generate(): FileSpec? {
        val outputClassName: ClassName = System.getProperty("kotlin-protobuf.type_registry", null)
            ?.let { ClassName.bestGuess(it) }
            ?: return null

        val spec = FileSpec.builder(outputClassName.packageName, outputClassName.simpleName + ".kt")

        spec.addGeneratorVersionAnnotation()
        spec.addType(generateType(outputClassName))

        return spec.build()
    }

    fun registerEnum(typeUrl: String, typeName: ClassName) {
        enumRegistry[typeUrl] = typeName
    }

    fun registerMessage(typeUrl: String, typeName: ClassName) {
        messageRegistry[typeUrl] = typeName
    }

    private fun generateType(className: ClassName): TypeSpec {
        val spec = TypeSpec.objectBuilder(className)

        val enumRegistryBlock = CodeBlock.builder()
        enumRegistryBlock.add("mapOf<%T, %T>(", String::class, typeNameOf<KClass<*>>())
        for ((typeUrl, typeName) in enumRegistry) {
            enumRegistryBlock.add("%S to %T::class,\n", typeUrl, typeName)
        }
        enumRegistryBlock.add(")")
        spec.addProperty(
            PropertySpec.builder("enums", MAP.parameterizedBy(STRING, typeNameOf<KClass<*>>()))
                .initializer(enumRegistryBlock.build())
                .build()
        )

        val messageRegistryBlock = CodeBlock.builder()
        messageRegistryBlock.add("mapOf<%T, %T>(", String::class, typeNameOf<KClass<*>>())
        for ((typeUrl, typeName) in messageRegistry) {
            messageRegistryBlock.add("%S to %T::class,\n", typeUrl, typeName)
        }
        messageRegistryBlock.add(")")
        spec.addProperty(
            PropertySpec.builder("messages", MAP.parameterizedBy(STRING, typeNameOf<KClass<*>>()))
                .initializer(messageRegistryBlock.build())
                .build()
        )

        return spec.build()
    }
}