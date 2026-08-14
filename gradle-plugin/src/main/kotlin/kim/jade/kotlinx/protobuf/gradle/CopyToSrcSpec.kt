package kim.jade.kotlinx.protobuf.gradle

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

/**
 * Copies generated code into the project's source tree instead of leaving it under `build/`.
 *
 * Intended for the rare module that commits its generated code — this project's `wkt` is the
 * motivating case. Ordinary builds should leave this off and let the plugin add the build directory as a
 * source directory.
 *
 * The copy tasks are never wired into compilation. Run them explicitly (`copyProtoToSrc`) so a normal
 * build never rewrites the source tree behind the developer's back.
 */
abstract class CopyToSrcSpec {

    /** Off by default. */
    abstract val enabled: Property<Boolean>

    /** Destination for generator output. Defaults to `src/<sourceSet>/kotlin`. */
    abstract val kotlinDirectory: DirectoryProperty

    /**
     * The copy is a `Sync`: anything in a destination directory that this run did not produce is
     * deleted, so removing a `.proto` cannot leave a stale `.kt` behind.
     *
     * That makes the destinations deliberately narrow — `src/<sourceSet>/kotlin` for the generators and
     * `src/<sourceSet>/<builtin>` for each protoc builtin — rather than the source set root. Point
     * [kotlinDirectory] at a directory that holds hand-written code and this task will delete it.
     */
    abstract val sync: Property<Boolean>
}
