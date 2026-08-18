package kim.jade.kotlinx.protobuf.gradle.internal

import org.gradle.api.InvalidUserDataException

/**
 * The generators shipped with kotlinx-protobuf: which artifact carries each one, which `Generator` object
 * to run, and which protoc outputs the code it emits calls into.
 *
 * Keep this in sync with the generator modules; it is the only place their coordinates and entry points
 * are written down on the plugin side.
 */
internal data class CatalogEntry(
    /** Artifact id under `kim.jade`. */
    val artifactId: String,
    /** Fully qualified name of the `Generator` object. */
    val generatorClass: String,
    /**
     * Whether the generated code delegates to protoc-gen-java's message classes, and therefore needs the
     * `java` builtin.
     */
    val requiresJavaBuiltin: Boolean = false,
    /**
     * Whether the generated code calls into grpc-java's service stubs, and therefore needs the
     * `protoc-gen-grpc-java` plugin. The `java` builtin alone does not produce them.
     */
    val requiresGrpcJavaPlugin: Boolean = false,
    /**
     * The generator that emits the JVM `actual`s for this one's `expect`s.
     *
     * The two halves are never useful apart — an `expect` without its `actual` does not compile — so
     * declaring the common half is taken as declaring both.
     */
    val jvmCounterpart: String? = null,
    /** The generator that emits the JS `actual`s for this one's `expect`s. Same reasoning as [jvmCounterpart]. */
    val jsCounterpart: String? = null,
)

internal object GeneratorCatalog {

    const val KOTLIN = "kotlin"
    const val KOTLINX_TYPES = "kotlinxSerialization"
    const val CONVERTER_JVM = "converterJvm"
    const val CONVERTER_MULTIPLATFORM = "converterMultiplatform"
    const val CONVERTER_MULTIPLATFORM_JVM = "converterMultiplatformJvm"
    const val CONVERTER_MULTIPLATFORM_JS = "converterMultiplatformJs"
    const val GRPC_JVM = "grpcJvm"
    const val GRPC_MULTIPLATFORM = "grpcMultiplatform"
    const val GRPC_MULTIPLATFORM_JVM = "grpcMultiplatformJvm"
    const val GRPC_MULTIPLATFORM_JS = "grpcMultiplatformJs"
    const val GRPC_GATEWAY_NAME = "grpcGateway"

    /** Generators that emit the message/enum/service type file, and therefore cannot be combined. */
    val TYPE_GENERATORS = setOf(KOTLIN, KOTLINX_TYPES)

    /** Generators that emit `<Type>Converter`, which the kotlinx serializers reference. */
    val CONVERTER_GENERATORS = setOf(
        CONVERTER_JVM,
        CONVERTER_MULTIPLATFORM,
        CONVERTER_MULTIPLATFORM_JVM,
        CONVERTER_MULTIPLATFORM_JS,
    )

    private val entries: Map<String, CatalogEntry> = mapOf(
        KOTLIN to CatalogEntry(
            artifactId = "kotlinx-protobuf-generator",
            generatorClass = "kim.jade.kotlinx.protobuf.generator.KotlinGenerator",
        ),
        KOTLINX_TYPES to CatalogEntry(
            artifactId = "kotlinx-protobuf-generator-serialization",
            generatorClass = "kim.jade.kotlinx.protobuf.generator.serialization.KotlinxSerializationGenerator",
        ),
        CONVERTER_JVM to CatalogEntry(
            artifactId = "kotlinx-protobuf-generator-converter-jvm",
            generatorClass = "kim.jade.kotlinx.protobuf.generator.converter.jvm.JvmConverterGenerator",
            requiresJavaBuiltin = true,
        ),
        CONVERTER_MULTIPLATFORM to CatalogEntry(
            artifactId = "kotlinx-protobuf-generator-converter-multiplatform",
            generatorClass = "kim.jade.kotlinx.protobuf.generator.converter.multiplatform.MultiplatformConverterGenerator",
            jvmCounterpart = CONVERTER_MULTIPLATFORM_JVM,
            jsCounterpart = CONVERTER_MULTIPLATFORM_JS,
        ),
        CONVERTER_MULTIPLATFORM_JVM to CatalogEntry(
            artifactId = "kotlinx-protobuf-generator-converter-multiplatform-jvm",
            generatorClass = "kim.jade.kotlinx.protobuf.generator.converter.multiplatform.jvm.MultiplatformJvmConverterGenerator",
            requiresJavaBuiltin = true,
        ),
        CONVERTER_MULTIPLATFORM_JS to CatalogEntry(
            artifactId = "kotlinx-protobuf-generator-converter-multiplatform-js",
            generatorClass = "kim.jade.kotlinx.protobuf.generator.converter.multiplatform.js.MultiplatformJsConverterGenerator",
        ),
        GRPC_JVM to CatalogEntry(
            artifactId = "kotlinx-protobuf-generator-grpc-jvm",
            generatorClass = "kim.jade.kotlinx.protobuf.generator.grpc.jvm.JvmGrpcGenerator",
            requiresJavaBuiltin = true,
            requiresGrpcJavaPlugin = true,
        ),
        GRPC_MULTIPLATFORM to CatalogEntry(
            artifactId = "kotlinx-protobuf-generator-grpc-multiplatform",
            generatorClass = "kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.MultiplatformGrpcGenerator",
            jvmCounterpart = GRPC_MULTIPLATFORM_JVM,
            jsCounterpart = GRPC_MULTIPLATFORM_JS,
        ),
        GRPC_MULTIPLATFORM_JVM to CatalogEntry(
            artifactId = "kotlinx-protobuf-generator-grpc-multiplatform-jvm",
            generatorClass = "kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.jvm.MultiplatformJvmGrpcGenerator",
            requiresJavaBuiltin = true,
            requiresGrpcJavaPlugin = true,
        ),
        GRPC_MULTIPLATFORM_JS to CatalogEntry(
            artifactId = "kotlinx-protobuf-generator-grpc-multiplatform-js",
            generatorClass = "kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.js.MultiplatformJsGrpcGenerator",
        ),
        GRPC_GATEWAY_NAME to CatalogEntry(
            artifactId = "kotlinx-protobuf-generator-grpc-gateway",
            generatorClass = "kim.jade.kotlinx.protobuf.generator.grpc.gateway.GrpcGatewayGenerator",
        ),
    )

    operator fun get(name: String): CatalogEntry? = entries[name]

    /**
     * Checks one source set's generators against the two rules that make a combination unbuildable.
     *
     * Both are caught here because the alternative is a compile error inside code the build never wrote:
     * two type generators produce duplicate declarations, and serializers without a converter produce an
     * unresolved `<Type>Converter`. Neither error names the generator that caused it.
     */
    fun validate(sourceSetName: String, generatorNames: Collection<String>) {
        val declared = generatorNames.toSet()

        val types = declared.filter { it in TYPE_GENERATORS }.sorted()
        if (types.size > 1) {
            throw InvalidUserDataException(
                "kotlinx-protobuf: ${types.joinToString(" and ") { "$it()" }} are both declared on " +
                    "'$sourceSetName', and both emit the message and enum types. Keep one of them."
            )
        }

        if (KOTLINX_TYPES in declared && declared.none { it in CONVERTER_GENERATORS }) {
            throw InvalidUserDataException(
                "kotlinx-protobuf: $KOTLINX_TYPES() on '$sourceSetName' emits serializers that delegate " +
                    "to <Type>Converter, so it needs a converter generator alongside it. Add " +
                    "$CONVERTER_MULTIPLATFORM() for Kotlin Multiplatform, or $CONVERTER_JVM() for a " +
                    "kotlin(\"jvm\") project."
            )
        }
    }
}
