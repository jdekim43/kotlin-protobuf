package kim.jade.kotlinx.protobuf.generator

import com.google.protobuf.compiler.PluginProtos
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.file.TypeFileGenerator
import kim.jade.kotlinx.protobuf.generator.file.TypeRegistryGenerator
import kim.jade.kotlinx.protobuf.generator.type.EnumTypeGenerator
import kim.jade.kotlinx.protobuf.generator.type.EnumValueTypeGenerator
import kim.jade.kotlinx.protobuf.generator.type.MessageTypeGenerator
import kim.jade.kotlinx.protobuf.generator.type.ServiceTypeGenerator
import kim.jade.kotlinx.protobuf.generator.util.extention.toResponse

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
        TypeRegistryGenerator.collector(),
    )

    override fun onGenerate(
        request: PluginProtos.CodeGeneratorRequest,
        builder: PluginProtos.CodeGeneratorResponse.Builder
    ) {
        TypeRegistryGenerator.generate()?.let { builder.addFile(it.toResponse()) }
    }
}