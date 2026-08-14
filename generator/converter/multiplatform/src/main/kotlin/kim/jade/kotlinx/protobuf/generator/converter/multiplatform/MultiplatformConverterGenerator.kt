package kim.jade.kotlinx.protobuf.generator.converter.multiplatform

import kim.jade.kotlinx.protobuf.generator.Generator
import kim.jade.kotlinx.protobuf.generator.converter.ConverterFileGenerator
import kim.jade.kotlinx.protobuf.generator.converter.platform.MultiplePlatformGenerator
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator

fun main(args: Array<String>) = Generator.runWith(MultiplatformConverterGenerator)

object MultiplatformConverterGenerator : Generator() {

    override val generators: List<FileGenerator> = listOf(
        ConverterFileGenerator(MultiplePlatformGenerator()),
    )
}