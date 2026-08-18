package kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import kim.jade.kotlinx.protobuf.generator.addTo
import kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.mapper.MessageJsMapperGenerator
import kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.mapper.util.extention.JsRuntime
import kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.mapper.util.extention.protobufJsFileMemberName
import kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.util.extention.outputJsConverterFileName
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.util.extention.addGeneratorVersionAnnotation
import kim.jade.kotlinx.protobuf.generator.util.extention.outputPackage
import java.util.Base64

class JsConverterFileGenerator(
    val messageJsMapperGenerator: MessageJsMapperGenerator,
) : FileGenerator {

    override fun generate(descriptor: Descriptors.FileDescriptor): FileSpec {
        val spec = FileSpec.builder(descriptor.outputPackage, descriptor.outputJsConverterFileName)
        spec.addFileComment("Transform from %L", descriptor.name)

        spec.addGeneratorVersionAnnotation()

        for (messageDescriptor in descriptor.messageTypes) {
            val (mapperSpec, imports) = messageJsMapperGenerator.generate(messageDescriptor)
            imports.addTo(spec)
            spec.addType(mapperSpec)
        }

        descriptor.writeFileDescriptorTo(spec)

        return spec.build()
    }

    private fun Descriptors.FileDescriptor.writeFileDescriptorTo(spec: FileSpec.Builder) {
        val bytes = toProto().toBuilder().clearSourceCodeInfo().build().toByteArray()

        val initializer = CodeBlock.builder()
        initializer.add("%T(\n", JsRuntime.PROTOBUF_JS_FILE)
        initializer.indent()
        initializer.add("name = %S,\n", name)
        initializer.add("descriptor = %S,\n", Base64.getEncoder().encodeToString(bytes))

        val imports = schemaDependencies()

        if (imports.isNotEmpty()) {
            initializer.add("dependencies = listOf(\n")
            initializer.indent()
            for (dependency in imports) {
                initializer.add("%M,\n", dependency.protobufJsFileMemberName)
            }
            initializer.unindent()
            initializer.add("),\n")
        }

        initializer.unindent()
        initializer.add(")")

        spec.addProperty(
            PropertySpec.builder(protobufJsFileMemberName.simpleName, JsRuntime.PROTOBUF_JS_FILE)
                .initializer(initializer.build())
                .build()
        )
    }

    private fun Descriptors.FileDescriptor.schemaDependencies(): List<Descriptors.FileDescriptor> {
        val collected = LinkedHashMap<String, Descriptors.FileDescriptor>()

        fun collect(file: Descriptors.FileDescriptor) {
            if (file.messageTypes.isNotEmpty()) {
                collected.putIfAbsent(file.name, file)
                return
            }

            file.publicDependencies.forEach(::collect)
        }

        dependencies.forEach(::collect)

        return collected.values.toList()
    }
}
