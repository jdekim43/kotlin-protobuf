package kim.jade.kotlinx.protobuf.gradle

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property

/**
 * The `kotlinxProtobuf { }` block: settings that apply to the whole project.
 *
 * What to generate is declared per source set instead, in `kotlin.sourceSets`:
 *
 * ```
 * kotlin.sourceSets {
 *     commonMain { proto { kotlin() } }
 * }
 * ```
 */
abstract class KotlinxProtobufExtension {

    /** protoc version resolved from `com.google.protobuf:protoc`. Ignored when [protocPath] is set. */
    abstract val protocVersion: Property<String>

    /**
     * An already-installed protoc to use instead of downloading one. The escape hatch for platforms with
     * no published protoc binary.
     */
    abstract val protocPath: RegularFileProperty

    /** Version of the `kim.jade:kotlinx-protobuf-generator-*` artifacts. Defaults to the plugin version. */
    abstract val generatorVersion: Property<String>

    /**
     * grpc version the `protoc-gen-grpc-java` plugin is taken from, for the generators that need it.
     * Defaults to the version this plugin was built against.
     */
    abstract val grpcVersion: Property<String>

    /**
     * Whether to put the well-known-type protos bundled in `protobuf-java` on protoc's include path.
     * On by default.
     *
     * The protoc binary published to Maven does not carry them, so without this an
     * `import "google/protobuf/timestamp.proto"` simply fails to resolve. They are made importable only,
     * never generated — add `kim.jade:kotlinx-protobuf-wkt` as a dependency for the Kotlin types.
     */
    abstract val includeWellKnownTypes: Property<Boolean>

    /** Whether to pass `--include_source_info`, which carries comments into the generated code. */
    abstract val includeSourceInfo: Property<Boolean>

    /**
     * Options applied to every generator, beneath per-source-set and per-generator options.
     *
     * Generator-specific options belong on the generator instead — `kotlin { typeRegistry(…) }` and
     * friends — so they cannot be handed to a generator that does not read them.
     */
    abstract val options: MapProperty<String, String>

    /**
     * Whether to put the `.proto` files carried by a source set's own dependencies on protoc's include
     * path. On by default, matching what the `com.google.protobuf` plugin does with the compile classpath.
     */
    abstract val includeProtosFromDependencies: Property<Boolean>

    /** Extra protoc `-I` roots applied to every proto source set. */
    abstract val includes: ConfigurableFileCollection

    /** Jars to mine `.proto` files from for the include path, e.g. `io.grpc:grpc-protobuf`. */
    abstract val protoPath: ConfigurableFileCollection

    /** Adds a protoc `-I` root applied to every proto source set. */
    fun include(vararg paths: Any) {
        includes.from(*paths)
    }

    fun option(key: String, value: String) {
        options.put(key, value)
    }

    /**
     * Prefix for `Any` type URLs, e.g. `type.googleapis.com`. Every generator reads it; a single
     * generator can still override it with its own `typeUrlPrefix`.
     */
    fun typeUrlPrefix(value: String) = option(OPTION_TYPE_URL_PREFIX, value)

    companion object {
        const val OPTION_TYPE_URL_PREFIX = "kotlinx-protobuf.prefix"
        const val OPTION_TYPE_REGISTRY = "kotlinx-protobuf.type_registry"
        const val OPTION_JVM_TYPE_REGISTRY = "kotlinx-protobuf.jvm_type_registry"
        const val OPTION_JS_TYPE_REGISTRY = "kotlinx-protobuf.js_type_registry"
        const val OPTION_SERIALIZERS_MODULE = "kotlinx-protobuf.serializers_module"
    }
}
