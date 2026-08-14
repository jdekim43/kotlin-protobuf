package kim.jade.kotlinx.protobuf.gradle

import org.gradle.api.Action
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

/**
 * What to generate from this source set's `.proto` files.
 *
 * The plugin adds this to every Kotlin source set, so protos are declared where the rest of the source
 * set is — the same shape the `com.google.protobuf` plugin uses for Java source sets:
 *
 * ```
 * import kim.jade.kotlinx.protobuf.gradle.proto
 *
 * kotlin.sourceSets {
 *     commonMain {
 *         proto {
 *             kotlin()
 *             converterMultiplatform()
 *         }
 *     }
 * }
 * ```
 *
 * The import is needed because Gradle's Kotlin DSL only generates accessors for extensions on its own
 * types, and `KotlinSourceSet` belongs to the Kotlin Gradle plugin.
 */
val KotlinSourceSet.proto: ProtoSourceSetSpec
    get() = (this as ExtensionAware).extensions.getByType(ProtoSourceSetSpec::class.java)

/** Configures what to generate from this source set's `.proto` files. See [proto]. */
fun KotlinSourceSet.proto(action: Action<in ProtoSourceSetSpec>) {
    action.execute(proto)
}
