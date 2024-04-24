package kr.jadekim.protobuf.generator.converter.jvm

import com.google.protobuf.compiler.PluginProtos
import kr.jadekim.protobuf.generator.Generator
import kr.jadekim.protobuf.generator.converter.ConverterFileGenerator
import kr.jadekim.protobuf.generator.converter.jvm.mapper.MessageMapperGenerator
import kr.jadekim.protobuf.generator.converter.jvm.util.extention.jvmConverterTypeName
import kr.jadekim.protobuf.generator.converter.platform.SinglePlatformGenerator
import kr.jadekim.protobuf.generator.file.FileGenerator
import kr.jadekim.protobuf.generator.util.extention.toResponse

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
