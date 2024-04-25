package kr.jadekim.protobuf.generator.kotlinx

import com.google.protobuf.compiler.PluginProtos
import kr.jadekim.protobuf.generator.Generator
import kr.jadekim.protobuf.generator.file.FileGenerator
import kr.jadekim.protobuf.generator.file.TypeFileGenerator
import kr.jadekim.protobuf.generator.file.TypeRegistryGenerator
import kr.jadekim.protobuf.generator.kotlinx.plugin.type.EnumTypePlugin
import kr.jadekim.protobuf.generator.kotlinx.plugin.type.MessageTypePlugin
import kr.jadekim.protobuf.generator.kotlinx.plugin.type.OneOfItemTypePlugin
import kr.jadekim.protobuf.generator.kotlinx.plugin.type.OneOfTypePlugin
import kr.jadekim.protobuf.generator.type.EnumTypeGenerator
import kr.jadekim.protobuf.generator.type.EnumValueTypeGenerator
import kr.jadekim.protobuf.generator.type.MessageTypeGenerator
import kr.jadekim.protobuf.generator.type.ServiceTypeGenerator
import kr.jadekim.protobuf.generator.util.extention.toResponse

fun main(args: Array<String>) = Generator.runWith(KotlinxGenerator)

object KotlinxGenerator : Generator() {

    private val enumTypeGenerator = EnumTypeGenerator(
        EnumValueTypeGenerator(),
        listOf(EnumTypePlugin),
    )

    private val messageTypeGenerator = MessageTypeGenerator(
        enumTypeGenerator,
        listOf(MessageTypePlugin),
        listOf(OneOfTypePlugin),
        listOf(OneOfItemTypePlugin)
    )

    private val serviceTypeGenerator = ServiceTypeGenerator()

    private val typeGenerator = TypeFileGenerator(
        enumTypeGenerator,
        messageTypeGenerator,
        serviceTypeGenerator,
    )

    override val generators: List<FileGenerator> = listOf(
        typeGenerator,
        TypeRegistryGenerator.collector(),
        SerializationModuleFileGenerator.collector(),
    )

    override fun onGenerate(
        request: PluginProtos.CodeGeneratorRequest,
        builder: PluginProtos.CodeGeneratorResponse.Builder
    ) {
        TypeRegistryGenerator.generate()?.let { builder.addFile(it.toResponse()) }
        SerializationModuleFileGenerator.generate()?.let { builder.addFile(it.toResponse()) }
    }
}