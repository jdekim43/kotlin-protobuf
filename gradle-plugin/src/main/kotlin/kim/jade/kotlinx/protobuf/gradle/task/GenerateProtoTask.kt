package kim.jade.kotlinx.protobuf.gradle.task

import javax.inject.Inject
import kim.jade.kotlinx.protobuf.gradle.GeneratorSpec
import kim.jade.kotlinx.protobuf.gradle.internal.ProtoPaths
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkerExecutor
import kim.jade.kotlinx.protobuf.gradle.worker.GenerateProtoWork

/**
 * Runs the kotlinx-protobuf generators over a descriptor set.
 *
 * Pure `descriptor.pb -> files`: no external process, no network, so it caches and relocates cleanly.
 * Each generator writes into its own subdirectory, which matters because several of them emit the same
 * relative path — the `expect` converter in commonMain and its `actual` in jvmMain, for instance.
 */
@CacheableTask
abstract class GenerateProtoTask : DefaultTask() {

    /**
     * Only the content matters; the descriptor set lives in the build directory.
     *
     * Optional because a source set with no protos never produces one — every Kotlin source set gets
     * these tasks, and most of them have nothing to generate.
     */
    @get:InputFile
    @get:Optional
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val descriptorSet: RegularFileProperty

    /**
     * The `.proto` files to generate for. Imports outside this set are resolved but not generated.
     *
     * SkipWhenEmpty, so a source set without protos reports NO-SOURCE instead of running.
     */
    @get:InputFiles
    @get:SkipWhenEmpty
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val protoFiles: ConfigurableFileCollection

    /** Roots the files in [protoFiles] are named relative to. */
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val protoRoots: ConfigurableFileCollection

    /** Options applied to every generator, beneath each generator's own options. */
    @get:Input
    abstract val commonOptions: MapProperty<String, String>

    @get:Nested
    abstract val generators: ListProperty<GeneratorSpec>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @get:Inject
    protected abstract val workerExecutor: WorkerExecutor

    @get:Inject
    protected abstract val files: FileSystemOperations

    @TaskAction
    fun generate() {
        val root = outputDirectory.get().asFile

        // Wipe first: a deleted .proto must not leave its generated file behind.
        files.delete { it.delete(root) }

        val filesToGenerate = ProtoPaths.relativeNames(protoFiles.files, protoRoots.files)
        val descriptor = descriptorSet.asFile.orNull

        if (filesToGenerate.isEmpty() || descriptor == null) {
            return
        }

        val shared = commonOptions.get()

        for (generator in generators.get()) {
            val parameter = ProtoPaths.encodeOptions(shared + generator.options.get())
            val target = root.resolve(generator.outputSubDirectory.getOrElse(generator.name))
            target.mkdirs()

            val queue = workerExecutor.processIsolation { worker ->
                // Partition worker daemons by generator. Gradle keys daemon reuse on the fork options, so
                // this keeps two generators off the same JVM while still letting each one's daemon be
                // reused between builds.
                worker.forkOptions.systemProperty("kotlinx-protobuf.worker", "$path/${generator.name}")
            }

            queue.submit(GenerateProtoWork::class.java) { parameters ->
                parameters.generatorClasspath.setFrom(generator.classpath)
                parameters.generatorClassName.set(generator.generatorClass)
                parameters.descriptorSet.set(descriptor)
                parameters.filesToGenerate.set(filesToGenerate)
                parameters.parameter.set(parameter)
                parameters.outputDirectory.set(target)
            }

            // Await per generator rather than at the end: the generators keep global state, so two of
            // them must never be in flight together even across daemons.
            queue.await()
        }
    }
}
