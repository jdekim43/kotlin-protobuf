package kim.jade.kotlinx.protobuf.gradle

import org.gradle.api.Named
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * One kotlinx-protobuf generator to run over a proto source set.
 *
 * Prefer the shortcuts on [ProtoSourceSetSpec] — `kotlin()`, `converterMultiplatform()`, … — which fill
 * in [generatorClass] and [artifact] for the generators shipped with this project. Configure a spec
 * directly only for a generator built on top of `kim.jade.kotlinx.protobuf.generator.Generator` elsewhere.
 */
abstract class GeneratorSpec(private val specName: String) : Named {

    @Input
    override fun getName(): String = specName

    /**
     * Fully qualified name of the `Generator` object to run, e.g.
     * `kim.jade.kotlinx.protobuf.generator.KotlinGenerator`.
     */
    @get:Input
    abstract val generatorClass: Property<String>

    /**
     * Coordinates of the shadow jar containing [generatorClass]. Ignored when [classpath] is set
     * explicitly.
     */
    @get:Input
    @get:Optional
    abstract val artifact: Property<String>

    /**
     * Classpath the generator runs on. Defaults to resolving [artifact]; set it to use a jar that is not
     * published, such as one built in the same repository.
     */
    @get:Classpath
    abstract val classpath: ConfigurableFileCollection

    /**
     * Options passed to the generator, merged over
     * [KotlinxProtobufExtension.options][KotlinxProtobufExtension.options].
     */
    @get:Input
    abstract val options: MapProperty<String, String>

    /**
     * Directory under the source set's generated-code root that this generator writes into. Defaults to
     * the spec name, which keeps two generators from overwriting each other when they emit the same
     * relative path — as the `expect`/`actual` converter pair does.
     */
    @get:Input
    abstract val outputSubDirectory: Property<String>

    fun option(key: String, value: String) {
        options.put(key, value)
    }
}
