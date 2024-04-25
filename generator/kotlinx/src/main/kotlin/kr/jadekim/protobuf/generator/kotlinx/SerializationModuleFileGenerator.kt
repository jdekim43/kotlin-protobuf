package kr.jadekim.protobuf.generator.kotlinx

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.modules.SerializersModule
import kr.jadekim.protobuf.generator.file.RegistryFileGenerator
import kr.jadekim.protobuf.generator.kotlinx.util.serializerTypeName
import kr.jadekim.protobuf.generator.util.extention.outputTypeName

object SerializationModuleFileGenerator : RegistryFileGenerator(
    "serializers_module",
    "kotlin-protobuf.serializers_module",
) {

    override fun TypeSpec.Builder.write() {
        val block = CodeBlock.builder()
        block.add("SerializersModule {\n")

        for ((_, descriptor) in descriptors) {
            block.add("\tcontextual(%T::class, %T)\n", descriptor.outputTypeName, descriptor.serializerTypeName)
        }

        block.add("}")

        addProperty(
            PropertySpec.builder("messages", SerializersModule::class)
                .initializer(block.build())
                .build()
        )
    }
}