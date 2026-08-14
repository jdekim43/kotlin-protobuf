package kim.jade.kotlinx.protobuf.gradle.task

import javax.inject.Inject
import kim.jade.kotlinx.protobuf.gradle.ProtocPluginSpec
import kim.jade.kotlinx.protobuf.gradle.ProtocBuiltinSpec
import kim.jade.kotlinx.protobuf.gradle.internal.ProtoPaths
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations

/**
 * Runs protoc once per proto source set.
 *
 * This is the only task that touches the protoc binary. It emits a `FileDescriptorSet` that the Kotlin
 * generators consume, plus whatever protoc builtins and `protoc-gen-*` plugins the build asked for.
 * Keeping the external process here means the Kotlin generation tasks are pure
 * `descriptor.pb -> files` and cache cleanly.
 */
@CacheableTask
abstract class GenerateProtoDescriptorSetTask : DefaultTask() {

    @get:InputFile
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val protocExecutable: RegularFileProperty

    /** The `.proto` files to compile. */
    @get:InputFiles
    @get:SkipWhenEmpty
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val protoFiles: ConfigurableFileCollection

    /** Roots the files in [protoFiles] are named relative to. Each becomes a `-I` argument. */
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val protoRoots: ConfigurableFileCollection

    /** Additional `-I` roots holding importable protos that are not themselves generated. */
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val includeDirectories: ConfigurableFileCollection

    @get:Input
    abstract val includeSourceInfo: Property<Boolean>

    @get:Nested
    abstract val builtins: ListProperty<ProtocBuiltinSpec>

    @get:Nested
    abstract val protocPlugins: ListProperty<ProtocPluginSpec>

    /** Where the descriptor set is written. Absent when another source set's descriptor set is reused. */
    @get:OutputFile
    @get:Optional
    abstract val descriptorSet: RegularFileProperty

    /** Root for builtin and protoc plugin output; each gets a subdirectory named after it. */
    @get:OutputDirectory
    abstract val protocOutputDirectory: DirectoryProperty

    @get:Inject
    protected abstract val execOperations: ExecOperations

    @get:Inject
    protected abstract val files: FileSystemOperations

    @TaskAction
    fun run() {
        val roots = protoRoots.files.filter { it.isDirectory }
        val sources = resolveSources(roots)

        val enabledBuiltins = builtins.get().filter { it.enabled.get() }
        val enabledPlugins = protocPlugins.get().filter { it.enabled.get() }

        // protoc rejects an invocation with no output directive, which is exactly what a source set that
        // reuses another's descriptor set and declares no outputs would produce.
        val hasWork = descriptorSet.isPresent || enabledBuiltins.isNotEmpty() || enabledPlugins.isNotEmpty()

        if (sources.isEmpty() || !hasWork) {
            return
        }

        val outputRoot = protocOutputDirectory.get().asFile
        files.delete { it.delete(outputRoot) }

        val arguments = mutableListOf<String>()

        roots.forEach { arguments += "-I=${it.absolutePath}" }
        includeDirectories.files.filter { it.isDirectory }.forEach { arguments += "-I=${it.absolutePath}" }

        descriptorSet.asFile.orNull?.let {
            it.parentFile.mkdirs()
            arguments += "--include_imports"
            if (includeSourceInfo.get()) {
                arguments += "--include_source_info"
            }
            arguments += "--descriptor_set_out=${it.absolutePath}"
        }

        enabledBuiltins.forEach { builtin ->
            val directory = outputRoot.resolve(builtin.name).apply { mkdirs() }
            arguments += "--${builtin.name}_out=${builtin.options.get().joinTo(directory)}"
        }

        enabledPlugins.forEach { plugin ->
            val executable = plugin.executable.get().asFile
            val directory = outputRoot.resolve(plugin.name).apply { mkdirs() }
            arguments += "--plugin=protoc-gen-${plugin.name}=${executable.absolutePath}"
            arguments += "--${plugin.name}_out=${plugin.options.get().joinTo(directory)}"
        }

        arguments += sources

        execOperations.exec { spec ->
            spec.executable = protocExecutable.get().asFile.absolutePath
            spec.args = arguments
        }
    }

    private fun resolveSources(roots: List<java.io.File>): List<String> =
        ProtoPaths.relativeNames(protoFiles.files, roots)

    /** protoc's `--x_out` takes `options:directory`, with the colon omitted when there are no options. */
    private fun List<String>.joinTo(directory: java.io.File): String =
        if (isEmpty()) directory.absolutePath else "${joinToString(",")}:${directory.absolutePath}"
}
