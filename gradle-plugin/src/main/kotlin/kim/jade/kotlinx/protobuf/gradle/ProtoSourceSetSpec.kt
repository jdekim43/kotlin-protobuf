package kim.jade.kotlinx.protobuf.gradle

import kim.jade.kotlinx.protobuf.gradle.internal.GeneratorCatalog
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Named
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

/**
 * The protos belonging to one Kotlin source set, and what to generate from them.
 *
 * A spec is created automatically for every Kotlin source set that has a `src/<name>/proto` directory.
 * Register one by hand to read protos from somewhere else — typically a `jvmMain` spec that generates the
 * JVM `actual`s from the same protos `commonMain` generated the `expect`s from.
 */
abstract class ProtoSourceSetSpec(private val specName: String) : Named {

    override fun getName(): String = specName

    /**
     * Directories searched for `.proto` files, named after the `com.google.protobuf` plugin's
     * `srcDirs`. Starts at `src/<name>/proto`; [srcDir] adds to it, `setFrom` replaces it.
     */
    abstract val srcDirs: ConfigurableFileCollection

    /**
     * Extra protoc `-I` roots: protos that can be imported but are not themselves generated. On top of
     * what the plugin already puts there — the well-known types and the protos found in dependencies.
     */
    abstract val includes: ConfigurableFileCollection

    /**
     * Reuse another spec's descriptor set instead of running protoc again.
     *
     * Two specs over the same protos — `commonMain` for the `expect`s, `jvmMain` for the `actual`s —
     * otherwise invoke protoc twice for identical output.
     */
    abstract val descriptorSetFrom: Property<String>

    /** Options merged over [KotlinxProtobufExtension.options] and under each generator's own options. */
    abstract val options: MapProperty<String, String>

    /**
     * Root for generator output; each generator gets a `<root>/<generatorName>` subdirectory of its own.
     * Defaults to `build/generated/sources/kotlinx-protobuf/<name>`.
     *
     * The subdirectories matter: several generators emit the same relative path — the `expect` converter
     * and its `actual`, for instance — and would otherwise overwrite each other.
     */
    abstract val outputDirectory: DirectoryProperty

    /**
     * Root for protoc builtin and protoc plugin output, one `<root>/<name>` subdirectory each.
     * Defaults to `build/generated/sources/kotlinx-protobuf-protoc/<name>`.
     */
    abstract val protocOutputDirectory: DirectoryProperty

    /** The descriptor set protoc produces. Defaults to `build/kotlinx-protobuf/descriptors/<name>.pb`. */
    abstract val descriptorSetFile: RegularFileProperty

    /** Kotlin source set the generated code is attached to. Defaults to [getName]. */
    abstract val kotlinSourceSetName: Property<String>

    /**
     * Whether to add the generated directories as source directories. Defaults to the opposite of
     * [copyToSrc]`.enabled` — with copy-to-src on, the same classes would otherwise be compiled twice,
     * once from `build/` and once from `src/`.
     */
    abstract val wireGeneratedSources: Property<Boolean>

    abstract val generators: NamedDomainObjectContainer<GeneratorSpec>

    abstract val builtins: NamedDomainObjectContainer<ProtocBuiltinSpec>

    abstract val protocPlugins: NamedDomainObjectContainer<ProtocPluginSpec>

    @get:Nested
    abstract val copyToSrc: CopyToSrcSpec

    /** Adds a directory to search for `.proto` files, alongside the default `src/<name>/proto`. */
    fun srcDir(vararg paths: Any) {
        srcDirs.from(*paths)
    }

    /** Adds a protoc `-I` root holding protos that can be imported but are not generated. */
    fun include(vararg paths: Any) {
        includes.from(*paths)
    }

    fun copyToSrc(action: Action<in CopyToSrcSpec>) {
        action.execute(copyToSrc)
    }

    fun generators(action: Action<in NamedDomainObjectContainer<GeneratorSpec>>) {
        action.execute(generators)
    }

    fun builtins(action: Action<in NamedDomainObjectContainer<ProtocBuiltinSpec>>) {
        action.execute(builtins)
    }

    fun protocPlugins(action: Action<in NamedDomainObjectContainer<ProtocPluginSpec>>) {
        action.execute(protocPlugins)
    }

    /**
     * Configures the protoc builtin [name], creating it if it does not exist yet.
     *
     * Prefer this over `builtins { register(…) }`: a generator that needs a builtin registers it, so
     * whether it already exists depends on which generators are enabled — and `register` fails on a name
     * that is taken.
     */
    @JvmOverloads
    fun builtin(name: String, action: Action<in ProtocBuiltinSpec> = Action {}) {
        action.execute(builtins.maybeCreate(name))
    }

    /**
     * Configures the protoc plugin [name], creating it if it does not exist yet. Same reasoning as
     * [builtin].
     */
    @JvmOverloads
    fun protocPlugin(name: String, action: Action<in ProtocPluginSpec> = Action {}) {
        action.execute(protocPlugins.maybeCreate(name))
    }

    // --- Which generators to run over this source set's protos ----------------------------------------
    //
    // Each shortcut fills in the artifact and the generator class from GeneratorCatalog, and hands back
    // an options type carrying only the options that generator actually reads. See the README for which
    // one belongs in which source set.

    /** Plain Kotlin message, enum and service types. Mutually exclusive with [kotlinxSerialization]. */
    @JvmOverloads
    fun kotlin(action: Action<in TypeGeneratorOptions> = Action {}) =
        configure(GeneratorCatalog.KOTLIN, ::TypeGeneratorOptions, action)

    /** Types annotated for kotlinx.serialization. Needs a converter generator and the serialization plugin. */
    @JvmOverloads
    fun kotlinxSerialization(action: Action<in KotlinxSerializationGeneratorOptions> = Action {}) =
        configure(GeneratorCatalog.KOTLINX_TYPES, ::KotlinxSerializationGeneratorOptions, action)

    /** JVM-only converters delegating to protoc-gen-java. Needs `builtins { register("java") }`. */
    @JvmOverloads
    fun converterJvm(action: Action<in JvmConverterGeneratorOptions> = Action {}) =
        configure(GeneratorCatalog.CONVERTER_JVM, ::JvmConverterGeneratorOptions, action)

    /** `expect` converters, for commonMain. */
    @JvmOverloads
    fun converterMultiplatform(action: Action<in GeneratorOptions> = Action {}) =
        configure(GeneratorCatalog.CONVERTER_MULTIPLATFORM, ::GeneratorOptions, action)

    /** Matching `actual` converters plus the protobuf-java mappers, for jvmMain. Needs the `java` builtin. */
    @JvmOverloads
    fun converterMultiplatformJvm(action: Action<in JvmConverterGeneratorOptions> = Action {}) =
        configure(GeneratorCatalog.CONVERTER_MULTIPLATFORM_JVM, ::JvmConverterGeneratorOptions, action)

    /** Matching `actual` converters plus the protobuf.js mappers, for jsMain. Registered automatically. */
    @JvmOverloads
    fun converterMultiplatformJs(action: Action<in JsConverterGeneratorOptions> = Action {}) =
        configure(GeneratorCatalog.CONVERTER_MULTIPLATFORM_JS, ::JsConverterGeneratorOptions, action)

    /** JVM-only gRPC clients and servers. */
    @JvmOverloads
    fun grpcJvm(action: Action<in GeneratorOptions> = Action {}) =
        configure(GeneratorCatalog.GRPC_JVM, ::GeneratorOptions, action)

    /** `expect` gRPC service factories, for commonMain. */
    @JvmOverloads
    fun grpcMultiplatform(action: Action<in GeneratorOptions> = Action {}) =
        configure(GeneratorCatalog.GRPC_MULTIPLATFORM, ::GeneratorOptions, action)

    /** Matching `actual` gRPC factories backed by grpc-java, for jvmMain. */
    @JvmOverloads
    fun grpcMultiplatformJvm(action: Action<in GeneratorOptions> = Action {}) =
        configure(GeneratorCatalog.GRPC_MULTIPLATFORM_JVM, ::GeneratorOptions, action)

    /** Matching `actual` gRPC factories backed by @grpc/grpc-js, for jsMain. Registered automatically. */
    @JvmOverloads
    fun grpcMultiplatformJs(action: Action<in GeneratorOptions> = Action {}) =
        configure(GeneratorCatalog.GRPC_MULTIPLATFORM_JS, ::GeneratorOptions, action)

    /** Ktor-based REST clients driven by `google.api.http` options. */
    @JvmOverloads
    fun grpcGateway(action: Action<in GeneratorOptions> = Action {}) =
        configure(GeneratorCatalog.GRPC_GATEWAY_NAME, ::GeneratorOptions, action)

    /**
     * Runs a generator that is not one of the built-ins. Set at least
     * [GeneratorOptions.generatorClass] and [GeneratorOptions.artifact].
     */
    fun generator(name: String, action: Action<in GeneratorOptions>) =
        configure(name, ::GeneratorOptions, action)

    private fun <T : GeneratorOptions> configure(
        name: String,
        options: (GeneratorSpec) -> T,
        action: Action<in T>,
    ) {
        action.execute(options(generators.maybeCreate(name)))
    }
}
