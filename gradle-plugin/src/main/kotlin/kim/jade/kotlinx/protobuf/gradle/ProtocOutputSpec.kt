package kim.jade.kotlinx.protobuf.gradle

import org.gradle.api.Named
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity

/** Where a protoc output — a builtin or a protoc plugin — should be attached in the project. */
enum class ProtocOutputTarget {
    /** Add the output directory to the source set's Kotlin sources. */
    KOTLIN,

    /** Add it to the Java sources. Warns when the project has no Java source set to compile them. */
    JAVA,

    /**
     * Add it to both, which is the safe default for a plugin whose output language is not known up front.
     *
     * Wiring Java output to the Kotlin sources alone is a silent trap: kotlinc reads the `.java` files
     * for symbol resolution, so everything compiles, but javac never sees them and the classes are
     * missing at runtime. Registering both source directories costs nothing — the Java source set only
     * picks up `.java`, and the Kotlin one ignores what it cannot use.
     */
    BOTH,

    /** Generate it but wire it nowhere; useful when only [CopyToSrcSpec] consumes the result. */
    NONE,
}

/**
 * Something protoc itself writes, as opposed to a generator this plugin runs in a worker.
 *
 * protoc takes these options as a comma-separated list in front of the output path
 * — `--<name>_out=<options>:<dir>` — and some of them are bare flags rather than key/value pairs, so
 * [options] is a list rather than the map the kotlinx-protobuf generators use.
 */
abstract class ProtocOutputSpec(private val specName: String) : Named {

    @Input
    override fun getName(): String = specName

    /**
     * Whether protoc runs this output at all. On by default.
     *
     * Turn it off to decline one the plugin registered automatically, when the classes it would produce
     * already come from somewhere else. `kotlinx-protobuf-wkt` is the motivating case: it generates
     * the well-known types, whose Java classes protobuf-java already ships, and a second copy would
     * shadow the real one.
     */
    @get:Input
    abstract val enabled: Property<Boolean>

    /** The raw option tokens, in the order protoc receives them. */
    @get:Input
    abstract val options: ListProperty<String>

    /** Where the output is attached. */
    @get:Input
    abstract val wireTo: Property<ProtocOutputTarget>

    /**
     * Where `copyToSrc` puts this output. Defaults to `src/<sourceSet>/<name>`, so the `java` builtin
     * lands in `src/jvmMain/java`.
     *
     * Not a task input — it only affects the copy task, which has its own.
     */
    @get:Internal
    abstract val copyToSrcDirectory: DirectoryProperty

    // Gradle only auto-imports its own types into build scripts, so `wireTo = ProtocOutputTarget.JAVA`
    // needs an import statement. These say the same thing without one.

    /** Attach the output to the Kotlin sources only. */
    fun wireToKotlin() = wireTo.set(ProtocOutputTarget.KOTLIN)

    /** Attach the output to the Java sources only. */
    fun wireToJava() = wireTo.set(ProtocOutputTarget.JAVA)

    /** Attach the output to both, the safe choice when the output language is not fixed. */
    fun wireToBoth() = wireTo.set(ProtocOutputTarget.BOTH)

    /** Generate the output but attach it nowhere. */
    fun wireToNothing() = wireTo.set(ProtocOutputTarget.NONE)

    /** A bare flag, such as `lite` for `--java_out=lite:…`. */
    fun option(value: String) {
        options.add(value)
    }

    /** A key/value option, such as `annotate_code=true`. */
    fun option(key: String, value: String) {
        options.add("$key=$value")
    }
}

/**
 * A protoc builtin output such as `java` or `cpp`, emitted as `--<name>_out`.
 *
 * The JVM converters delegate to protoc-gen-java's classes, so `converterJvm()` and
 * `converterMultiplatformJvm()` need `builtins { register("java") }` alongside them.
 */
abstract class ProtocBuiltinSpec(specName: String) : ProtocOutputSpec(specName)

/**
 * A `protoc-gen-*` plugin, invoked through the standard protoc plugin protocol.
 *
 * This covers the whole protoc plugin ecosystem, whatever language it is written in. A generator built on
 * this project's `Generator` class can also run this way, but `generator(…)` runs it in a worker instead,
 * which is faster and reports errors better.
 */
abstract class ProtocPluginSpec(specName: String) : ProtocOutputSpec(specName) {

    /**
     * Coordinates the plugin is resolved from, so it does not have to be installed on every machine that
     * builds. Two shapes work:
     *
     *  - a self-contained (shadow) jar, e.g. `io.grpc:protoc-gen-grpc-kotlin:1.5.0:jdk8@jar`. protoc
     *    cannot execute a jar, so the plugin writes a small `java -jar` launcher around it.
     *  - a native binary published per platform, e.g. `io.grpc:protoc-gen-grpc-java:1.83.1:osx-aarch_64@exe`.
     *
     * A jar has to carry its own dependencies: the launcher runs `java -jar`, which ignores everything
     * outside the jar.
     */
    @get:Input
    @get:Optional
    abstract val artifact: Property<String>

    /**
     * An already-installed executable, taking precedence over [artifact]. Only the file's content
     * matters, not where it lives, so moving a checkout does not invalidate the task.
     */
    @get:InputFile
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val executable: RegularFileProperty
}
