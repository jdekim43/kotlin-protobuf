package kim.jade.kotlinx.protobuf.generator.converter.jvm

import com.google.protobuf.Descriptors
import com.google.protobuf.TypeRegistry
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kim.jade.kotlinx.protobuf.generator.converter.jvm.mapper.util.extention.delegatorTypeName
import kim.jade.kotlinx.protobuf.generator.file.RegistryFileGenerator
import kim.jade.kotlinx.protobuf.generator.util.extention.typeName

object JvmTypeRegistryFileGenerator : RegistryFileGenerator(
    "type_registry.jvm",
    "kotlinx-protobuf.jvm_type_registry",
) {

    override fun TypeSpec.Builder.write() {
        val block = CodeBlock.builder()
        block.add("mapOf<%T, %T>(", String::class, Descriptors.Descriptor::class)
        for ((typeUrl, descriptor) in descriptors) {
            block.add("%S to %T.getDescriptor(),\n", typeUrl, descriptor.delegatorTypeName)
        }
        block.add(")")

        addProperty(
            PropertySpec.builder("messages", MAP.parameterizedBy(STRING, Descriptors.Descriptor::class.typeName))
                .initializer(block.build())
                .build()
        )
        addProperty(
            PropertySpec.builder("registry", TypeRegistry::class)
                .initializer("%T.newBuilder().add(messages.values).build()", TypeRegistry::class)
                .build()
        )
    }
}