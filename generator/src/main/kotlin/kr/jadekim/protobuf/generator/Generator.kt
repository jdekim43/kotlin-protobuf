package kr.jadekim.protobuf.generator

import com.google.protobuf.Descriptors
import com.google.protobuf.compiler.PluginProtos
import kr.jadekim.protobuf.generator.file.FileGenerator
import kr.jadekim.protobuf.generator.util.extention.toResponse
import java.io.IOException
import java.io.InputStream

val PREBUILT_PROTO_FILES = listOf(
    "google/protobuf/any.proto",
    "google/protobuf/api.proto",
    "google/protobuf/duration.proto",
    "google/protobuf/empty.proto",
    "google/protobuf/field_mask.proto",
    "google/protobuf/source_context.proto",
    "google/protobuf/struct.proto",
    "google/protobuf/timestamp.proto",
    "google/protobuf/type.proto",
    "google/protobuf/wrappers.proto",
)

abstract class Generator {

    companion object {

        fun runWith(generator: Generator, input: InputStream = System.`in`) {
            val request = try {
                PluginProtos.CodeGeneratorRequest.parseFrom(input)
            } catch (e: Throwable) {
                throw IOException("Fail to parse protobuf file", e)
            }

            try {
                generator.generate(request)
            } catch (e: Throwable) {
                throw IOException("Fail to generate code from protobuf", e)
            }
        }
    }

    abstract val generators: List<FileGenerator>

    fun generate(request: PluginProtos.CodeGeneratorRequest) {
        setParameters(request)

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
            .filter {
                if (PREBUILT_PROTO_FILES.contains(it.key)) {
                    if (!System.getProperty("kotlin-protobuf.include_prebuilt", "false").equals("true", true)) {
                        return@filter false
                    }
                }

                true
            }
            .flatMap { (_, descriptor) -> generators.map { it.generate(descriptor) } }
            .filter { it.members.isNotEmpty() }
            .map { it.toResponse() }
            .forEach(outputBuilder::addFile)

        onGenerate(request, outputBuilder)

        outputBuilder.build()
            .writeTo(System.out)
    }

    protected open fun onGenerate(
        request: PluginProtos.CodeGeneratorRequest,
        builder: PluginProtos.CodeGeneratorResponse.Builder,
    ) {
        //do nothing
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