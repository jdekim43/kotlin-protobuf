package kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.mapper.util.extention.JsRuntime
import kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.mapper.util.extention.protobufJsFileMemberName
import kim.jade.kotlinx.protobuf.generator.file.RegistryFileGenerator

object JsTypeRegistryFileGenerator : RegistryFileGenerator(
    "type_registry.js",
    "kotlinx-protobuf.js_type_registry",
) {

    override fun TypeSpec.Builder.write() {
        val messages = CodeBlock.builder()
        messages.add("mapOf<%T, %T>(\n", STRING, JsRuntime.PROTOBUF_JS_FILE)
        messages.indent()
        for ((typeUrl, descriptor) in descriptors) {
            messages.add("%S to %M,\n", typeUrl, descriptor.file.protobufJsFileMemberName)
        }
        messages.unindent()
        messages.add(")")

        addProperty(
            PropertySpec.builder(
                "messages",
                MAP.parameterizedBy(STRING, JsRuntime.PROTOBUF_JS_FILE),
            )
                .initializer(messages.build())
                .build()
        )

        val files = CodeBlock.builder()
        files.add("listOf<%T>(\n", JsRuntime.PROTOBUF_JS_FILE)
        files.indent()
        for (file in registeredFiles()) {
            files.add("%M,\n", file.protobufJsFileMemberName)
        }
        files.unindent()
        files.add(")")

        addProperty(
            PropertySpec.builder("files", LIST.parameterizedBy(JsRuntime.PROTOBUF_JS_FILE))
                .initializer(files.build())
                .build()
        )
    }

    private fun registeredFiles(): List<Descriptors.FileDescriptor> =
        descriptors.values.map { it.file }.distinctBy { it.name }
}
