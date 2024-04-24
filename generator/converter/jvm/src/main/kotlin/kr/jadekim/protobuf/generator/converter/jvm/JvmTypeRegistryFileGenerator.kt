package kr.jadekim.protobuf.generator.converter.jvm

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kr.jadekim.protobuf.generator.converter.jvm.mapper.util.extention.delegatorTypeName
import kr.jadekim.protobuf.generator.file.FileGenerator
import kr.jadekim.protobuf.generator.util.extention.*

object JvmTypeRegistryFileGenerator {

    private val descriptors = mutableMapOf<String, Descriptors.GenericDescriptor>()

    fun collector(): FileGenerator = object : FileGenerator {
        override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
            descriptor.enumTypes.forEach(::register)
            descriptor.messageTypes.forEach(::register)

            return FileSpec.builder(descriptor.outputPackage, descriptor.fileName + ".jvm_type_registry.kt").build()
        }
    }

    fun generate(): FileSpec? {
        val outputClassName: ClassName = System.getProperty("kotlin-protobuf.jvm_type_registry", null)
            ?.let { ClassName.bestGuess(it) }
            ?: return null

        val spec = FileSpec.builder(outputClassName.packageName, outputClassName.simpleName + ".kt")

        spec.addGeneratorVersionAnnotation()
        spec.addType(generateType(outputClassName))

        return spec.build()
    }

    fun register(descriptor: Descriptors.GenericDescriptor) {
        descriptors[descriptor.typeUrl] = descriptor
    }

    fun register(descriptor: Descriptors.Descriptor) {
        descriptors[descriptor.typeUrl] = descriptor

        for (nestedType in descriptor.nestedTypes.filterNot { it.options.mapEntry }) {
            register(nestedType)
        }
    }

    private fun generateType(className: ClassName): TypeSpec {
        val spec = TypeSpec.objectBuilder(className)

        val registryBlock = CodeBlock.builder()
        registryBlock.add("mapOf<%T, %T>(", String::class, Descriptors.GenericDescriptor::class)
        for ((typeUrl, descriptor) in descriptors) {
            registryBlock.add("%S to %T.getDescriptor(),\n", typeUrl, descriptor.delegatorTypeName)
        }
        registryBlock.add(")")
        spec.addProperty(
            PropertySpec.builder("registry", MAP.parameterizedBy(STRING, Descriptors.GenericDescriptor::class.typeName))
                .initializer(registryBlock.build())
                .build()
        )

        return spec.build()
    }
}