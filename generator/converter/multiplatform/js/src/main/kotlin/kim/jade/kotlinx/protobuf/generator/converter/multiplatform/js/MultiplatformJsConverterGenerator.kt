package kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js

import kim.jade.kotlinx.protobuf.generator.Generator
import kim.jade.kotlinx.protobuf.generator.converter.ConverterFileGenerator
import kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.mapper.MessageJsMapperGenerator
import kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.util.extention.jsConverterTypeName
import kim.jade.kotlinx.protobuf.generator.converter.platform.SinglePlatformGenerator
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.util.extention.toResponse
import com.google.protobuf.compiler.PluginProtos

fun main(args: Array<String>) = Generator.runWith(MultiplatformJsConverterGenerator)

object MultiplatformJsConverterGenerator : Generator() {

    override val generators: List<FileGenerator> = listOf(
        JsDelegatorFileGenerator,
        JsConverterFileGenerator(MessageJsMapperGenerator()),
        ConverterFileGenerator(SinglePlatformGenerator(isActual = true) { jsConverterTypeName }),
        JsTypeRegistryFileGenerator.collector(),
    )

    override fun onGenerate(
        request: PluginProtos.CodeGeneratorRequest,
        builder: PluginProtos.CodeGeneratorResponse.Builder,
    ) {
        JsTypeRegistryFileGenerator.generate()?.let { builder.addFile(it.toResponse()) }
    }
}
