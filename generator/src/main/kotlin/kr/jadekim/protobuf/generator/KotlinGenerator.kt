package kr.jadekim.protobuf.generator

import kr.jadekim.protobuf.generator.file.FileGenerator
import kr.jadekim.protobuf.generator.file.TypeFileGenerator
import kr.jadekim.protobuf.generator.type.EnumTypeGenerator
import kr.jadekim.protobuf.generator.type.EnumValueTypeGenerator
import kr.jadekim.protobuf.generator.type.MessageTypeGenerator

fun main(args: Array<String>) = Generator.runWith(KotlinGenerator)

object KotlinGenerator : Generator() {

    private val enumTypeGenerator = EnumTypeGenerator(
        EnumValueTypeGenerator(),
    )

    private val messageTypeGenerator = MessageTypeGenerator(
        enumTypeGenerator,
    )

    private val typeGenerator = TypeFileGenerator(
        enumTypeGenerator,
        messageTypeGenerator,
    )

    override val generators: List<FileGenerator> = listOf(
        typeGenerator,
    )
}