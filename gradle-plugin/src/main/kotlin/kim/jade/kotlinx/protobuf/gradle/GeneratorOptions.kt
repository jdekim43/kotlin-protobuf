package kim.jade.kotlinx.protobuf.gradle

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.MapProperty

/**
 * What one generator accepts, inside a `sourceSet { }` block.
 *
 * Every generator reads a different set of options, so each shortcut on [ProtoSourceSetSpec] hands back
 * a type that exposes only the ones that generator actually understands — asking `converterMultiplatform`
 * for a `serializersModule` does not compile, rather than being silently ignored at generation time.
 *
 * [option] remains available for anything this DSL does not model.
 */
open class GeneratorOptions internal constructor(internal val spec: GeneratorSpec) {

    /** The generator's name, which is also its output subdirectory by default. */
    val name: String get() = spec.name

    /** The classpath the generator runs on. Set it to use a jar that is not published. */
    val classpath: ConfigurableFileCollection get() = spec.classpath

    /** The raw option map, for a value that has to stay lazy. */
    val options: MapProperty<String, String> get() = spec.options

    /** Coordinates of the generator's shadow jar. Ignored once [classpath] is set explicitly. */
    fun artifact(notation: String) {
        spec.artifact.set(notation)
    }

    /** Fully qualified name of the `Generator` object to run. Only needed for a generator not in the catalog. */
    fun generatorClass(className: String) {
        spec.generatorClass.set(className)
    }

    /** Directory under the source set's generated root that this generator writes into. */
    fun outputSubDirectory(value: String) {
        spec.outputSubDirectory.set(value)
    }

    /** An option this DSL does not model, passed straight through to the generator. */
    fun option(key: String, value: String) {
        spec.option(key, value)
    }

    /**
     * Prefix for `Any` type URLs, e.g. `type.googleapis.com`. Overrides
     * [KotlinxProtobufExtension.typeUrlPrefix] for this generator.
     */
    fun typeUrlPrefix(value: String) = option(KotlinxProtobufExtension.OPTION_TYPE_URL_PREFIX, value)
}

/** Options for the generators that emit the message, enum and service types. */
open class TypeGeneratorOptions internal constructor(spec: GeneratorSpec) : GeneratorOptions(spec) {

    /**
     * Fully qualified name of an object mapping type URL to `KClass`, generated alongside the types.
     * Nothing is emitted when this is left unset.
     */
    fun typeRegistry(className: String) = option(KotlinxProtobufExtension.OPTION_TYPE_REGISTRY, className)
}

/** Options for the kotlinx.serialization flavour of the type generator. */
class KotlinxSerializationGeneratorOptions internal constructor(spec: GeneratorSpec) : TypeGeneratorOptions(spec) {

    /**
     * Fully qualified name of an object exposing a `SerializersModule` with a `contextual` entry per
     * message. Nothing is emitted when this is left unset.
     */
    fun serializersModule(className: String) =
        option(KotlinxProtobufExtension.OPTION_SERIALIZERS_MODULE, className)
}

/** Options for the converter generators that bridge to protobuf-java. */
class JvmConverterGeneratorOptions internal constructor(spec: GeneratorSpec) : GeneratorOptions(spec) {

    /**
     * Fully qualified name of an object exposing a protobuf-java `TypeRegistry` and its descriptors.
     * Nothing is emitted when this is left unset.
     */
    fun jvmTypeRegistry(className: String) =
        option(KotlinxProtobufExtension.OPTION_JVM_TYPE_REGISTRY, className)
}
