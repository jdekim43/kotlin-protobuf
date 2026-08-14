package kim.jade.kotlinx.protobuf.gradle.task

import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Unpacks the `.proto` files carried inside jars so protoc can import them.
 *
 * The protoc binary published to Maven has no well-known types bundled, so importing
 * `google/protobuf/timestamp.proto` fails unless those files are on the include path. `protobuf-java`
 * ships them; the same mechanism serves third-party protos such as `google/api/annotations.proto` from
 * `grpc-protobuf`.
 *
 * The result is an include root only. Nothing extracted here is ever passed to protoc as a file to
 * generate.
 */
@CacheableTask
abstract class ExtractProtoIncludesTask : DefaultTask() {

    @get:Classpath
    abstract val jars: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @get:Inject
    protected abstract val archives: ArchiveOperations

    @get:Inject
    protected abstract val files: FileSystemOperations

    @TaskAction
    fun extract() {
        val sources = jars.files.filter { it.isFile }

        files.sync { spec ->
            // Sync with no source still clears the directory, which is what we want when protoPath
            // shrinks to nothing.
            sources.forEach { jar ->
                spec.from(archives.zipTree(jar)) { it.include("**/*.proto") }
            }
            spec.into(outputDirectory)
            // Several jars can carry the same well-known type; first one wins.
            spec.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
    }
}
