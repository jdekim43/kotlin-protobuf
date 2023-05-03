package kr.jadekim.protobuf.generator.kotlinx

import kr.jadekim.protobuf.generator.Generator
import kr.jadekim.protobuf.generator.converter.mapper.MessageMapperGenerator
import kr.jadekim.protobuf.generator.file.ConverterFileGenerator
import kr.jadekim.protobuf.generator.file.FileGenerator
import kr.jadekim.protobuf.generator.file.TypeFileGenerator
import kr.jadekim.protobuf.generator.kotlinx.plugin.type.EnumTypePlugin
import kr.jadekim.protobuf.generator.kotlinx.plugin.type.MessageTypePlugin
import kr.jadekim.protobuf.generator.kotlinx.plugin.type.OneOfItemTypePlugin
import kr.jadekim.protobuf.generator.kotlinx.plugin.type.OneOfTypePlugin
import kr.jadekim.protobuf.generator.type.EnumTypeGenerator
import kr.jadekim.protobuf.generator.type.EnumValueTypeGenerator
import kr.jadekim.protobuf.generator.type.MessageTypeGenerator

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

    private val typeGenerator = TypeFileGenerator(
        enumTypeGenerator,
        messageTypeGenerator,
    )

    private val messageMapperGenerator = MessageMapperGenerator()

    private val converterGenerator = ConverterFileGenerator(
        messageMapperGenerator,
    )

    override val generators: List<FileGenerator> = listOf(
        typeGenerator,
        converterGenerator,
    )
}