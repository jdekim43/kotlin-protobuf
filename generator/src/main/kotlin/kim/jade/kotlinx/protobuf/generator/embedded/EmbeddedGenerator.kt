package kim.jade.kotlinx.protobuf.generator.embedded

import com.google.protobuf.DescriptorProtos
import com.google.protobuf.compiler.PluginProtos
import kim.jade.kotlinx.protobuf.generator.Generator
import java.io.File

/**
 * Entry point for running a generator in-process, from a descriptor set produced by
 * `protoc --descriptor_set_out`, instead of over the protoc plugin's stdin/stdout protocol.
 *
 * This lives in the base generator module so it ends up inside every generator's shadow jar — a caller
 * only ever needs that one jar on the classpath.
 *
 * Every parameter and the return value is a JDK type on purpose. The caller loads this class in a
 * throwaway class loader and invokes it reflectively, so nothing protobuf-shaped may cross that
 * boundary; the caller needs no protobuf dependency of its own and cannot hit a cross-loader
 * `ClassCastException`.
 */
object EmbeddedGenerator {

    private const val MANAGED_PROPERTY_PREFIX = "kotlinx-protobuf."

    /**
     * Runs [generatorClassName] over [descriptorSetFile] and writes the generated files under
     * [outputDirectory].
     *
     * @param generatorClassName fully qualified name of a `Generator` object, e.g.
     *   `kim.jade.kotlinx.protobuf.generator.KotlinGenerator`.
     * @param descriptorSetFile a `FileDescriptorSet` written with `--include_imports`.
     * @param filesToGenerate proto file names — as they appear in the descriptor set — to generate for.
     *   Imports outside this list are still resolved but not generated.
     * @param parameter protoc-style option string, `key=value` pairs separated by commas.
     * @return the generated file paths, relative to [outputDirectory].
     */
    @JvmStatic
    fun generate(
        generatorClassName: String,
        descriptorSetFile: File,
        filesToGenerate: List<String>,
        parameter: String,
        outputDirectory: File,
    ): List<String> {
        val generator = loadGenerator(generatorClassName)

        val descriptorSet = descriptorSetFile.inputStream().buffered().use {
            DescriptorProtos.FileDescriptorSet.parseFrom(it, generator.newExtensionRegistry())
        }

        val request = PluginProtos.CodeGeneratorRequest.newBuilder()
            .addAllProtoFile(descriptorSet.fileList.sortedByDependency())
            .addAllFileToGenerate(filesToGenerate)
            .setParameter(parameter)
            .build()

        val response = withIsolatedProperties { generator.generateResponse(request) }

        if (response.hasError()) {
            throw IllegalStateException("$generatorClassName failed: ${response.error}")
        }

        return response.fileList.map { it.write(outputDirectory) }
    }

    private fun loadGenerator(className: String): Generator {
        val type = try {
            Class.forName(className, true, EmbeddedGenerator::class.java.classLoader)
        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException("Generator class not found: $className", e)
        }

        val instance = try {
            type.getField("INSTANCE").get(null)
        } catch (e: NoSuchFieldException) {
            throw IllegalArgumentException("$className is not a Kotlin object", e)
        }

        return instance as? Generator
            ?: throw IllegalArgumentException("$className is not a ${Generator::class.java.name}")
    }

    /**
     * Generators read their options from JVM system properties, and the registry generators accumulate
     * state across calls. Clearing the managed properties before the run and restoring them after keeps
     * one invocation from inheriting another's `type_registry` and friends.
     */
    private fun <T> withIsolatedProperties(block: () -> T): T {
        val saved = System.getProperties()
            .stringPropertyNames()
            .filter { it.startsWith(MANAGED_PROPERTY_PREFIX) }
            .associateWith { System.getProperty(it) }

        saved.keys.forEach(System::clearProperty)

        try {
            return block()
        } finally {
            saved.keys.forEach(System::clearProperty)
            saved.forEach { (key, value) -> System.setProperty(key, value) }
        }
    }

    /**
     * `Generator` resolves each file's dependencies against files it has already seen, so the request
     * has to be in topological order. `protoc --include_imports` already emits that order; this only
     * guards against a descriptor set assembled some other way.
     */
    private fun List<DescriptorProtos.FileDescriptorProto>.sortedByDependency(): List<DescriptorProtos.FileDescriptorProto> {
        val byName = associateBy { it.name }
        val sorted = LinkedHashMap<String, DescriptorProtos.FileDescriptorProto>(size)
        val visiting = mutableSetOf<String>()

        fun visit(file: DescriptorProtos.FileDescriptorProto) {
            if (sorted.containsKey(file.name)) return
            if (!visiting.add(file.name)) {
                throw IllegalStateException("Circular proto import involving ${file.name}")
            }

            for (dependency in file.dependencyList) {
                val dependent = byName[dependency]
                    ?: throw IllegalStateException("Not found dependent file ($dependency) for ${file.name}")
                visit(dependent)
            }

            visiting.remove(file.name)
            sorted[file.name] = file
        }

        forEach(::visit)

        return sorted.values.toList()
    }

    private fun PluginProtos.CodeGeneratorResponse.File.write(outputDirectory: File): String {
        if (hasInsertionPoint()) {
            throw IllegalStateException("Insertion points are not supported (${insertionPoint} in $name)")
        }

        // Files whose proto has no package come back as "/Foo.kt"; ".." can never be legitimate.
        val relativePath = name.trimStart('/')
        if (relativePath.isEmpty() || relativePath.split('/').any { it == ".." }) {
            throw IllegalStateException("Refusing to write generated file outside the output directory: $name")
        }

        val target = File(outputDirectory, relativePath)
        target.parentFile?.mkdirs()
        target.writeText(content)

        return relativePath
    }
}
