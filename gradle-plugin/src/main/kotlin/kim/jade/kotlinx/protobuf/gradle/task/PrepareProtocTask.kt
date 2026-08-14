package kim.jade.kotlinx.protobuf.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

/**
 * Puts a runnable protoc at a known path.
 *
 * The `@exe` artifact lands read-only in the Gradle module cache, so it is copied out and made
 * executable. Caching this remotely would cost more than redoing the copy.
 */
@DisableCachingByDefault(because = "Copying a local file is cheaper than a cache round-trip")
abstract class PrepareProtocTask : DefaultTask() {

    /** The resolved `com.google.protobuf:protoc:<version>:<classifier>@exe` artifact. Empty when [override] is set. */
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val protocArtifact: ConfigurableFileCollection

    /** A protoc supplied by the build, taking precedence over [protocArtifact]. */
    @get:InputFile
    @get:Optional
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val override: RegularFileProperty

    @get:OutputFile
    abstract val executable: RegularFileProperty

    @TaskAction
    fun prepare() {
        val source = override.asFile.orNull
            ?: protocArtifact.files.singleOrNull()
            ?: throw IllegalStateException(
                "Expected exactly one protoc artifact but resolved ${protocArtifact.files.size}. " +
                    "Set kotlinxProtobuf { protocPath = file(\"…\") } to use a local protoc instead."
            )

        val target = executable.get().asFile
        target.parentFile.mkdirs()
        source.copyTo(target, overwrite = true)
        target.setExecutable(true)
    }
}
