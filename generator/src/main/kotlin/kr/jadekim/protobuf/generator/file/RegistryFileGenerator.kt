package kr.jadekim.protobuf.generator.file

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import kr.jadekim.protobuf.generator.util.extention.addGeneratorVersionAnnotation
import kr.jadekim.protobuf.generator.util.extention.fileName
import kr.jadekim.protobuf.generator.util.extention.outputPackage
import kr.jadekim.protobuf.generator.util.extention.typeUrl

abstract class RegistryFileGenerator(val seperatedFileNameSuffix: String, val outputClassNameProperty: String) {

    protected val descriptors = mutableMapOf<String, Descriptors.Descriptor>()

    protected abstract fun TypeSpec.Builder.write()

    fun collector(): FileGenerator = object : FileGenerator {
        override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
            descriptor.messageTypes.forEach(::register)

            return FileSpec.builder(descriptor.outputPackage, "${descriptor.fileName}.$seperatedFileNameSuffix.kt").build()
        }
    }

    fun register(descriptor: Descriptors.Descriptor) {
        descriptors[descriptor.typeUrl] = descriptor

        for (nestedType in descriptor.nestedTypes.filterNot { it.options.mapEntry }) {
            register(nestedType)
        }
    }

    fun generate(): FileSpec? {
        val outputClassName: ClassName = System.getProperty(outputClassNameProperty, null)
            ?.let { ClassName.bestGuess(it) }
            ?: return null

        val spec = FileSpec.builder(outputClassName.packageName, outputClassName.simpleName + ".kt")

        spec.addGeneratorVersionAnnotation()

        val typeSpec = TypeSpec.objectBuilder(outputClassName)
        typeSpec.write()
        spec.addType(typeSpec.build())

        return spec.build()
    }
}