package kim.jade.kotlinx.protobuf.generator.serialization

import com.google.protobuf.compiler.PluginProtos
import kim.jade.kotlinx.protobuf.generator.Generator
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.file.TypeFileGenerator
import kim.jade.kotlinx.protobuf.generator.file.TypeRegistryGenerator
import kim.jade.kotlinx.protobuf.generator.serialization.plugin.property.SerialNamePropertyPlugin
import kim.jade.kotlinx.protobuf.generator.serialization.plugin.type.EnumTypePlugin
import kim.jade.kotlinx.protobuf.generator.serialization.plugin.type.MessageTypePlugin
import kim.jade.kotlinx.protobuf.generator.serialization.plugin.type.OneOfItemTypePlugin
import kim.jade.kotlinx.protobuf.generator.serialization.plugin.type.OneOfTypePlugin
import kim.jade.kotlinx.protobuf.generator.type.EnumTypeGenerator
import kim.jade.kotlinx.protobuf.generator.type.EnumValueTypeGenerator
import kim.jade.kotlinx.protobuf.generator.type.MessageTypeGenerator
import kim.jade.kotlinx.protobuf.generator.type.ServiceTypeGenerator
import kim.jade.kotlinx.protobuf.generator.util.extention.toResponse

fun main(args: Array<String>) = Generator.runWith(KotlinxSerializationGenerator)

object KotlinxSerializationGenerator : Generator() {

    private val enumTypeGenerator = EnumTypeGenerator(
        EnumValueTypeGenerator(),
        listOf(EnumTypePlugin),
    )

    private val messageTypeGenerator = MessageTypeGenerator(
        enumTypeGenerator,
        listOf(MessageTypePlugin),
        listOf(OneOfTypePlugin),
        listOf(OneOfItemTypePlugin),
        listOf(SerialNamePropertyPlugin),
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