package kim.jade.kotlinx.protobuf.gradle.worker

import java.net.URLClassLoader
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters

interface GenerateProtoWorkParameters : WorkParameters {
    val generatorClasspath: ConfigurableFileCollection
    val generatorClassName: Property<String>
    val descriptorSet: RegularFileProperty
    val filesToGenerate: ListProperty<String>
    val parameter: Property<String>
    val outputDirectory: DirectoryProperty
}

/**
 * Runs one kotlinx-protobuf generator against a descriptor set.
 *
 * The generators keep state that outlives a single run: options arrive as JVM system properties, and the
 * registry generators accumulate descriptors in Kotlin `object`s. Two defences keep runs independent:
 *
 *  - a throwaway [URLClassLoader] per invocation, so every `object` really is fresh. Its parent is the
 *    platform loader, which also keeps the generator's bundled protobuf away from whatever the build
 *    script has on its own classpath.
 *  - [lock], which serialises invocations that share a worker daemon. It deliberately lives on this
 *    class — held in the plugin's own loader — because a lock inside the throwaway loader would be a
 *    different object every time and would guard nothing.
 */
abstract class GenerateProtoWork : WorkAction<GenerateProtoWorkParameters> {

    override fun execute() {
        val urls = parameters.generatorClasspath.files.map { it.toURI().toURL() }.toTypedArray()

        lock.withLock {
            URLClassLoader(urls, ClassLoader.getPlatformClassLoader()).use { loader ->
                val entryPoint = loader.loadClass(ENTRY_POINT_CLASS)
                val instance = entryPoint.getField("INSTANCE").get(null)
                val generate = entryPoint.getMethod(
                    "generate",
                    String::class.java,
                    java.io.File::class.java,
                    List::class.java,
                    String::class.java,
                    java.io.File::class.java,
                )

                generate.invoke(
                    instance,
                    parameters.generatorClassName.get(),
                    parameters.descriptorSet.get().asFile,
                    parameters.filesToGenerate.get(),
                    parameters.parameter.get(),
                    parameters.outputDirectory.get().asFile,
                )
            }
        }
    }

    private companion object {
        const val ENTRY_POINT_CLASS = "kim.jade.kotlinx.protobuf.generator.embedded.EmbeddedGenerator"

        val lock = ReentrantLock()
    }
}
