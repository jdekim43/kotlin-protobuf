package kim.jade.kotlinx.protobuf.generator

import com.google.protobuf.DescriptorProtos
import com.google.protobuf.Descriptors
import com.google.protobuf.ExtensionRegistry
import com.google.protobuf.compiler.PluginProtos
import kim.jade.kotlinx.protobuf.generator.file.FileGenerator
import kim.jade.kotlinx.protobuf.generator.util.extention.ProtobufOptionResolver
import kim.jade.kotlinx.protobuf.generator.util.extention.toResponse
import java.io.IOException
import java.io.InputStream

abstract class Generator {

    companion object {

        private val MAXIMUM_EDITION: DescriptorProtos.Edition = DescriptorProtos.Edition.values()
            .filter { !it.name.endsWith("_TEST_ONLY") && it.name != "EDITION_MAX" }
            .maxBy { it.number }

        @Deprecated("Moved instance function", ReplaceWith("Generator.generate"))
        fun runWith(generator: Generator, input: InputStream = System.`in`) {
            generator.generate(input)
        }
    }

    abstract val generators: List<FileGenerator>

    /**
     * The registry this generator needs to see its own custom options. Callers that parse a descriptor
     * set themselves must use it — without it an extension such as `google.api.http` is silently
     * demoted to an unknown field and the generator produces nothing.
     */
    fun newExtensionRegistry(): ExtensionRegistry = ExtensionRegistry.newInstance().also(::onRegisterExtension)

    fun generate(input: InputStream = System.`in`) {
        val registry = newExtensionRegistry()

        val request = try {
            PluginProtos.CodeGeneratorRequest.parseFrom(input, registry)
        } catch (e: Throwable) {
            throw IOException("Fail to parse protobuf file", e)
        }

        try {
            generate(request)
        } catch (e: Throwable) {
            throw IOException("Fail to generate code from protobuf", e)
        }
    }

    fun generate(request: PluginProtos.CodeGeneratorRequest) {
        generateResponse(request).writeTo(System.out)
    }

    fun generateResponse(request: PluginProtos.CodeGeneratorRequest): PluginProtos.CodeGeneratorResponse {
        setParameters(request)

        val descriptors = buildDescriptors(request.protoFileList)

        ProtobufOptionResolver.configure(descriptors.values, newExtensionRegistry())

        val outputBuilder = PluginProtos.CodeGeneratorResponse.newBuilder()
            .setSupportedFeatures(
                (
                    PluginProtos.CodeGeneratorResponse.Feature.FEATURE_PROTO3_OPTIONAL_VALUE or
                        PluginProtos.CodeGeneratorResponse.Feature.FEATURE_SUPPORTS_EDITIONS_VALUE
                    ).toLong()
            )
            .setMinimumEdition(DescriptorProtos.Edition.EDITION_PROTO2.number)
            .setMaximumEdition(MAXIMUM_EDITION.number)

        request.fileToGenerateList
            .asSequence()
            .map { descriptors[it] ?: throw IllegalStateException("Not found descriptor $it") }
            .flatMap { descriptor -> generators.map { it.generate(descriptor) } }
            .filter { it.members.isNotEmpty() }
            .map { it.toResponse() }
            .toList()
            .forEach(outputBuilder::addFile)

        onGenerate(request, outputBuilder)

        return outputBuilder.build()
    }

    private fun buildDescriptors(
        files: List<DescriptorProtos.FileDescriptorProto>,
    ): Map<String, Descriptors.FileDescriptor> {
        val descriptors = mutableMapOf<String, Descriptors.FileDescriptor>()

        for (file in files) {
            val dependencies = file.dependencyList.map {
                descriptors[it] ?: throw IllegalStateException("Not found dependent file ($it) for ${file.name}")
            }

            descriptors[file.name] = Descriptors.FileDescriptor.buildFrom(file, dependencies.toTypedArray())
        }

        return descriptors
    }

    protected open fun onGenerate(
        request: PluginProtos.CodeGeneratorRequest,
        builder: PluginProtos.CodeGeneratorResponse.Builder,
    ) {
        //do nothing
    }

    protected open fun onRegisterExtension(registry: ExtensionRegistry) {
    }

    protected open fun parseParameter(input: String?): Map<String, String> {
        if (input.isNullOrBlank()) {
            return emptyMap()
        }

        val result = mutableMapOf<String, String>()

        val pairs = input.split(',')

        for (str in pairs) {
            val pair = str.split('=', limit = 2)

            if (pair.isEmpty()) {
                continue
            } else if (pair.size == 1) {
                result[pair[0].trim()] = ""
            } else if (pair.size == 2) {
                result[pair[0].trim()] = pair[1].trim()
            } else {
                result[pair[0].trim()] = pairs.drop(1).joinToString("=").trim()
            }
        }

        return result
    }

    private fun setParameters(request: PluginProtos.CodeGeneratorRequest) {
        parseParameter(request.parameter).forEach { (k, v) -> System.setProperty(k, v) }
    }
}