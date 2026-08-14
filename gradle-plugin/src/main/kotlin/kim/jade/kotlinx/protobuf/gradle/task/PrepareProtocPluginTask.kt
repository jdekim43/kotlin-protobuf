package kim.jade.kotlinx.protobuf.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

/**
 * Turns a resolved protoc plugin artifact into something protoc can actually execute.
 *
 * protoc runs a plugin by `exec`ing the path it is handed, so a jar cannot be passed directly — that is
 * why the old hand-written setup in this repository, which pointed protoc straight at a shadow jar,
 * never worked. Resolving the plugin from a repository and generating the launcher here means a build
 * does not depend on anything being installed on the machine.
 */
@DisableCachingByDefault(because = "Writes a launcher holding absolute local paths")
abstract class PrepareProtocPluginTask : DefaultTask() {

    /** The resolved artifact: either a self-contained jar, or a native binary. */
    @get:Classpath
    abstract val artifact: ConfigurableFileCollection

    /** `java` to run a jar with. Unused for a native binary. */
    @get:Input
    abstract val javaExecutable: Property<String>

    @get:OutputFile
    abstract val executable: RegularFileProperty

    @TaskAction
    fun prepare() {
        val resolved = artifact.files.filter { it.isFile }

        val source = resolved.singleOrNull() ?: throw IllegalStateException(
            "Expected the protoc plugin to resolve to exactly one file but got ${resolved.size}: " +
                resolved.joinToString { it.name } +
                ". A jar-based protoc plugin has to be self-contained, because the launcher runs " +
                "`java -jar`. Use a shadow/fat jar artifact, a native @exe artifact, or set " +
                "`executable` to a binary you install yourself."
        )

        val target = executable.get().asFile
        target.parentFile.mkdirs()

        if (source.extension.equals("jar", ignoreCase = true)) {
            target.writeText(launcherFor(source.absolutePath))
        } else {
            source.copyTo(target, overwrite = true)
        }

        target.setExecutable(true)
    }

    private fun launcherFor(jarPath: String): String {
        val java = javaExecutable.get()

        return if (isWindows) {
            // protoc passes no arguments, but %* keeps the launcher honest if that ever changes.
            "@echo off\r\n\"$java\" -jar \"$jarPath\" %*\r\n"
        } else {
            // exec, so protoc talks to the JVM directly rather than through a surviving shell.
            "#!/bin/sh\nexec \"$java\" -jar \"$jarPath\" \"$@\"\n"
        }
    }

    internal companion object {
        val isWindows: Boolean
            get() = System.getProperty("os.name").lowercase().contains("win")

        /** protoc derives the plugin name from the file name, so it has to be exactly this. */
        fun executableName(pluginName: String): String =
            if (isWindows) "protoc-gen-$pluginName.bat" else "protoc-gen-$pluginName"
    }
}
