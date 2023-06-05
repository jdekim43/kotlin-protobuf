package kr.jadekim.protobuf.generator.converter.multiplatform

import kr.jadekim.protobuf.generator.Generator
import kr.jadekim.protobuf.generator.converter.ConverterFileGenerator
import kr.jadekim.protobuf.generator.converter.platform.MultiplePlatformGenerator
import kr.jadekim.protobuf.generator.file.FileGenerator

fun main(args: Array<String>) = Generator.runWith(MultiplatformConverterGenerator)

object MultiplatformConverterGenerator : Generator() {

    override val generators: List<FileGenerator> = listOf(
        ConverterFileGenerator(MultiplePlatformGenerator()),
    )
}