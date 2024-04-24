package kr.jadekim.protobuf.generator

import com.google.protobuf.compiler.PluginProtos
import kr.jadekim.protobuf.generator.file.FileGenerator
import kr.jadekim.protobuf.generator.file.TypeFileGenerator
import kr.jadekim.protobuf.generator.file.TypeRegistryGenerator
import kr.jadekim.protobuf.generator.type.EnumTypeGenerator
import kr.jadekim.protobuf.generator.type.EnumValueTypeGenerator
import kr.jadekim.protobuf.generator.type.MessageTypeGenerator
import kr.jadekim.protobuf.generator.type.ServiceTypeGenerator
import kr.jadekim.protobuf.generator.util.extention.toResponse

fun main(args: Array<String>) = Generator.runWith(KotlinGenerator)

object KotlinGenerator : Generator() {

    private val enumTypeGenerator = EnumTypeGenerator(
        EnumValueTypeGenerator(),
    )

    private val messageTypeGenerator = MessageTypeGenerator(
        enumTypeGenerator,
    )

    private val serviceTypeGenerator = ServiceTypeGenerator()

    private val typeGenerator = TypeFileGenerator(
        enumTypeGenerator,
        messageTypeGenerator,
        serviceTypeGenerator,
    )

    override val generators: List<FileGenerator> = listOf(
        typeGenerator,
    )

    override fun onGenerate(
        request: PluginProtos.CodeGeneratorRequest,
        builder: PluginProtos.CodeGeneratorResponse.Builder
    ) {
        TypeRegistryGenerator.generate()?.let { builder.addFile(it.toResponse()) }
    }
}