package kr.jadekim.protobuf.generator.kotlinx

import com.google.protobuf.Descriptors
import com.google.protobuf.compiler.PluginProtos
import kr.jadekim.protobuf.generator.converter.mapper.MessageMapperGenerator
import kr.jadekim.protobuf.generator.file.KotlinFileGenerator
import kr.jadekim.protobuf.generator.file.MapperFileGenerator
import kr.jadekim.protobuf.generator.kotlinx.plugin.type.EnumTypePlugin
import kr.jadekim.protobuf.generator.kotlinx.plugin.type.MessageTypePlugin
import kr.jadekim.protobuf.generator.kotlinx.plugin.type.OneOfItemTypePlugin
import kr.jadekim.protobuf.generator.kotlinx.plugin.type.OneOfTypePlugin
import kr.jadekim.protobuf.generator.type.EnumTypeGenerator
import kr.jadekim.protobuf.generator.type.EnumValueTypeGenerator
import kr.jadekim.protobuf.generator.type.MessageTypeGenerator
import kr.jadekim.protobuf.generator.util.extention.toResponse
import java.io.IOException

fun main(args: Array<String>) {
    val request = try {
        PluginProtos.CodeGeneratorRequest.parseFrom(System.`in`)
    } catch (e: Throwable) {
        throw IOException("Fail to parse protobuf file", e)
    }

    try {
        KotlinxGenerator.generate(request)
    } catch (e: Throwable) {
        throw IOException("Fail to generate code from protobuf", e)
    }
}

object KotlinxGenerator {

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

    private val kotlinGenerator = KotlinFileGenerator(
        enumTypeGenerator,
        messageTypeGenerator,
    )

    private val messageMapperGenerator = MessageMapperGenerator()

    private val mapperGenerator = MapperFileGenerator(
        messageMapperGenerator,
    )

    fun generate(request: PluginProtos.CodeGeneratorRequest) {
        val descriptors = mutableMapOf<String, Descriptors.FileDescriptor>()

        for (file in request.protoFileList) {
            val dependencies = file.dependencyList.map {
                descriptors[it] ?: throw IllegalStateException("Not found dependent file ($it) for ${file.name}")
            }

            descriptors[file.name] = Descriptors.FileDescriptor.buildFrom(file, dependencies.toTypedArray())
        }

        val outputBuilder = PluginProtos.CodeGeneratorResponse.newBuilder()
            .setSupportedFeatures(PluginProtos.CodeGeneratorResponse.Feature.FEATURE_PROTO3_OPTIONAL_VALUE.toLong())

        descriptors
            .flatMap { (_, descriptor) ->
                listOf(
                    kotlinGenerator.generate(descriptor),
                    mapperGenerator.generate(descriptor),
                )
            }
            .map { it.toResponse() }
            .forEach(outputBuilder::addFile)

        outputBuilder.build()
            .writeTo(System.out)
    }
}