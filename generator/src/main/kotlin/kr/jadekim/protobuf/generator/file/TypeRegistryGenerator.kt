package kr.jadekim.protobuf.generator.file

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kr.jadekim.protobuf.generator.util.extention.outputTypeName
import kotlin.reflect.KClass

object TypeRegistryGenerator : RegistryFileGenerator(
    "type_registry",
    "kotlin-protobuf.type_registry",
) {

    override fun TypeSpec.Builder.write() {
//        val enumRegistryBlock = CodeBlock.builder()
//        enumRegistryBlock.add("mapOf<%T, %T>(", String::class, typeNameOf<KClass<*>>())
//        for ((typeUrl, typeName) in enumRegistry) {
//            enumRegistryBlock.add("%S to %T::class,\n", typeUrl, typeName)
//        }
//        enumRegistryBlock.add(")")
//        spec.addProperty(
//            PropertySpec.builder("enums", MAP.parameterizedBy(STRING, typeNameOf<KClass<*>>()))
//                .initializer(enumRegistryBlock.build())
//                .build()
//        )

        val messageRegistryBlock = CodeBlock.builder()
        messageRegistryBlock.add("mapOf<%T, %T>(", String::class, typeNameOf<KClass<*>>())
        for ((typeUrl, descriptor) in descriptors) {
            messageRegistryBlock.add("%S to %T::class,\n", typeUrl, descriptor.outputTypeName)
        }
        messageRegistryBlock.add(")")

        addProperty(
            PropertySpec.builder("messages", MAP.parameterizedBy(STRING, typeNameOf<KClass<*>>()))
                .initializer(messageRegistryBlock.build())
                .build()
        )
    }
}