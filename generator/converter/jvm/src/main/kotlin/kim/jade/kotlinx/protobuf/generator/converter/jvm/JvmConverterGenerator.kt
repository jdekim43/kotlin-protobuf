package kim.jade.kotlinx.protobuf.generator.converter.jvm

import com.google.protobuf.compiler.PluginProtos
import kim.jade.kotlinx.protobuf.generator.Generator
import kim.jade.kotlinx.protobuf.generator.converter.ConverterFileGenerator
import kim.jade.kotlinx.protobuf.generator.converter.jvm.mapper.MessageMapperGenerator
import kim.jade.kotlinx.protobuf.generator.converter.jvm.util.extention.jvmConverterTypeName
import kim.jade.kotlinx.protobuf.generator.converter.platform.SinglePlatformGenerator
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.util.extention.toResponse

fun main(args: Array<String>) = Generator.runWith(JvmConverterGenerator)

object JvmConverterGenerator : Generator() {

    override val generators: List<FileGenerator> = listOf(
        JvmConverterFileGenerator(MessageMapperGenerator()),
        ConverterFileGenerator(SinglePlatformGenerator { jvmConverterTypeName }),
        JvmTypeRegistryFileGenerator.collector(),
    )

    override fun onGenerate(
        request: PluginProtos.CodeGeneratorRequest,
        builder: PluginProtos.CodeGeneratorResponse.Builder
    ) {
        JvmTypeRegistryFileGenerator.generate()?.let { builder.addFile(it.toResponse()) }
    }
}
