package kim.jade.kotlinx.protobuf.gradle

import kim.jade.kotlinx.protobuf.gradle.internal.CatalogEntry
import kim.jade.kotlinx.protobuf.gradle.internal.GeneratorCatalog
import kim.jade.kotlinx.protobuf.gradle.internal.ProtocPlatform
import kim.jade.kotlinx.protobuf.gradle.task.ExtractProtoIncludesTask
import kim.jade.kotlinx.protobuf.gradle.task.GenerateProtoDescriptorSetTask
import kim.jade.kotlinx.protobuf.gradle.task.GenerateProtoTask
import kim.jade.kotlinx.protobuf.gradle.task.PrepareProtocPluginTask
import kim.jade.kotlinx.protobuf.gradle.task.PrepareProtocTask
import org.gradle.api.Plugin
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.Usage
import org.gradle.api.file.Directory
import org.gradle.api.file.RelativePath
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Sync
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskProvider
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetsContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer

/**
 * Generates Kotlin from `.proto` files and attaches it to the right Kotlin source set.
 *
 * Works the same for `kotlin("multiplatform")` and `kotlin("jvm")`: everything goes through
 * [KotlinSourceSetContainer], where `commonMain` and `main` are the same kind of thing. That is what
 * lets a multiplatform project put generated code straight into `commonMain`, instead of the JVM
 * submodule detour the `com.google.protobuf` plugin forces.
 */
class KotlinxProtobufPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create(EXTENSION_NAME, KotlinxProtobufExtension::class.java)
        applyExtensionConventions(extension)

        val protocConfiguration = createProtocConfiguration(project, extension)
        val protoPathConfiguration = createProtoPathConfiguration(project, extension)

        val prepareProtoc = project.tasks.register(PREPARE_PROTOC_TASK, PrepareProtocTask::class.java) { task ->
            task.group = TASK_GROUP
            task.description = "Puts a runnable protoc in the build directory."
            task.protocArtifact.setFrom(protocConfiguration)
            task.override.set(extension.protocPath)
            task.executable.set(
                project.layout.buildDirectory.file("kotlinx-protobuf/bin/protoc$EXECUTABLE_SUFFIX")
            )
        }

        val extractIncludes =
            project.tasks.register(EXTRACT_INCLUDES_TASK, ExtractProtoIncludesTask::class.java) { task ->
                task.group = TASK_GROUP
                task.description = "Unpacks the .proto files carried inside jars so protoc can import them."
                task.jars.setFrom(protoPathConfiguration)
                task.outputDirectory.set(project.layout.buildDirectory.dir("kotlinx-protobuf/include-protos"))
            }

        val generateLifecycle = project.tasks.register(GENERATE_LIFECYCLE_TASK) { task ->
            task.group = TASK_GROUP
            task.description = "Generates Kotlin sources for every proto source set."
        }

        val copyLifecycle = project.tasks.register(COPY_LIFECYCLE_TASK) { task ->
            task.group = TASK_GROUP
            task.description = "Copies generated sources into the project's source tree."
        }

        // Both Kotlin plugins expose KotlinSourceSetContainer, so one reaction covers both. Reacting to
        // the plugin instead of using afterEvaluate keeps configuration lazy.
        forEachKotlinPlugin(project) {
            val container = project.extensions.findByName(KOTLIN_EXTENSION) as? KotlinSourceSetContainer
                ?: return@forEachKotlinPlugin

            container.sourceSets.configureEach { sourceSet ->
                val spec = (sourceSet as ExtensionAware).extensions.create(
                    PROTO_EXTENSION,
                    ProtoSourceSetSpec::class.java,
                    sourceSet.name,
                )

                applySourceSetConventions(project, spec, sourceSet)
                configureSourceSet(
                    project,
                    extension,
                    spec,
                    sourceSet,
                    prepareProtoc,
                    extractIncludes,
                    generateLifecycle,
                    copyLifecycle,
                )
            }
        }
    }

    // --- defaults -------------------------------------------------------------------------------------

    private fun applyExtensionConventions(extension: KotlinxProtobufExtension) {
        extension.protocVersion.convention(DEFAULT_PROTOC_VERSION)
        extension.generatorVersion.convention(PLUGIN_VERSION)
        extension.grpcVersion.convention(DEFAULT_GRPC_VERSION)
        extension.includeWellKnownTypes.convention(true)
        extension.includeProtosFromDependencies.convention(true)
        extension.includeSourceInfo.convention(true)
    }

    private fun applySourceSetConventions(
        project: Project,
        spec: ProtoSourceSetSpec,
        sourceSet: KotlinSourceSet,
    ) {
        val name = spec.name

        // setFrom, not convention: a convention is discarded by the first from(), which would make
        // srcDir("extra") silently drop src/<name>/proto.
        spec.srcDirs.setFrom(project.layout.projectDirectory.dir("src/$name/proto"))

        spec.outputDirectory.convention(project.layout.buildDirectory.dir("$GENERATED_ROOT/$name"))
        spec.protocOutputDirectory.convention(project.layout.buildDirectory.dir("$PROTOC_GENERATED_ROOT/$name"))
        spec.descriptorSetFile.convention(project.layout.buildDirectory.file("kotlinx-protobuf/descriptors/$name.pb"))
        spec.kotlinSourceSetName.convention(name)

        spec.copyToSrc.enabled.convention(false)
        spec.copyToSrc.sync.convention(true)
        spec.copyToSrc.kotlinDirectory.convention(project.layout.projectDirectory.dir("src/$name/kotlin"))

        // Copying into src while also adding the build directory as a source root would compile the same
        // declarations twice, so the two modes exclude each other by default.
        spec.wireGeneratedSources.convention(spec.copyToSrc.enabled.map { !it })

        spec.builtins.configureEach { builtin ->
            builtin.enabled.convention(true)
            builtin.wireTo.convention(
                if (builtin.name == JAVA_BUILTIN) ProtocOutputTarget.JAVA else ProtocOutputTarget.KOTLIN
            )
            builtin.copyToSrcDirectory.convention(
                project.layout.projectDirectory.dir("src/$name/${builtin.name}")
            )
        }

        spec.protocPlugins.configureEach { plugin ->
            plugin.enabled.convention(true)
            // A protoc plugin can emit Kotlin, Java or both, and nothing here knows which, so wire the
            // output everywhere it could belong rather than guessing.
            plugin.wireTo.convention(ProtocOutputTarget.BOTH)
            plugin.copyToSrcDirectory.convention(
                project.layout.projectDirectory.dir("src/$name/${plugin.name}")
            )
        }
    }

    // --- configurations -------------------------------------------------------------------------------

    private fun createProtocConfiguration(project: Project, extension: KotlinxProtobufExtension): Configuration =
        project.configurations.create(PROTOC_CONFIGURATION) { configuration ->
            configuration.isCanBeConsumed = false
            configuration.isCanBeResolved = true
            configuration.isTransitive = false
            configuration.isVisible = false
            configuration.description = "The protoc binary used to compile .proto files."

            configuration.defaultDependencies { dependencies ->
                // A build-supplied protoc wins outright; resolving the artifact too would download a
                // binary nothing uses.
                if (!extension.protocPath.isPresent) {
                    val classifier = ProtocPlatform.classifier(project.providers).get()
                    dependencies.add(
                        project.dependencies.create(
                            "com.google.protobuf:protoc:${extension.protocVersion.get()}:$classifier@exe"
                        )
                    )
                }
            }
        }

    private fun createProtoPathConfiguration(project: Project, extension: KotlinxProtobufExtension): Configuration =
        project.configurations.create(PROTO_PATH_CONFIGURATION) { configuration ->
            configuration.isCanBeConsumed = false
            configuration.isCanBeResolved = true
            configuration.isTransitive = false
            configuration.isVisible = false
            configuration.description = "Jars whose bundled .proto files go on protoc's include path."

            configuration.defaultDependencies { dependencies ->
                // protobuf-java carries the well-known-type protos. The protoc binary published to Maven
                // does not, so without this an `import "google/protobuf/timestamp.proto"` fails outright.
                if (extension.includeWellKnownTypes.get()) {
                    dependencies.add(
                        project.dependencies.create(
                            "com.google.protobuf:protobuf-java:${extension.protocVersion.get()}"
                        )
                    )
                }
            }
        }

    // --- per source set wiring ------------------------------------------------------------------------

    private fun configureSourceSet(
        project: Project,
        extension: KotlinxProtobufExtension,
        spec: ProtoSourceSetSpec,
        sourceSet: KotlinSourceSet,
        prepareProtoc: TaskProvider<PrepareProtocTask>,
        extractIncludes: TaskProvider<ExtractProtoIncludesTask>,
        generateLifecycle: TaskProvider<Task>,
        copyLifecycle: TaskProvider<Task>,
    ) {
        val suffix = spec.name.capitalized()
        val protoFiles = spec.srcDirs.asFileTree.matching { it.include("**/*.proto") }
        val dependencyProtos = extractDependencyProtos(project, extension, spec, sourceSet, suffix)

        val descriptorTask = project.tasks.register(
            "generate${suffix}ProtoDescriptorSet",
            GenerateProtoDescriptorSetTask::class.java,
        ) { task ->
            task.group = TASK_GROUP
            task.description = "Runs protoc over the ${spec.name} protos."

            task.protocExecutable.set(prepareProtoc.flatMap { it.executable })
            task.protoFiles.setFrom(protoFiles)
            task.protoRoots.setFrom(spec.srcDirs)
            task.includeDirectories.setFrom(
                spec.includes,
                extension.includes,
                extractIncludes.map { it.outputDirectory },
                dependencyProtos.map { it.outputDirectory },
            )
            task.includeSourceInfo.set(extension.includeSourceInfo)
            task.builtins.set(project.provider { spec.builtins.toList() })
            task.protocPlugins.set(project.provider { spec.protocPlugins.toList() })
            task.protocOutputDirectory.set(spec.protocOutputDirectory)

            // When another spec already produced a descriptor set for the same protos, skip writing a
            // second, identical one. Resolved lazily because the spec's own configuration block has not
            // necessarily run yet when this container hook fires.
            task.descriptorSet.set(
                spec.descriptorSetFile.zip(spec.descriptorSetFrom.map { true }.orElse(false)) { file, reused ->
                    if (reused) null else file
                }
            )
        }

        val descriptorSetFile = spec.descriptorSetFrom
            .flatMap { sourceName ->
                project.tasks
                    .named(
                        "generate${sourceName.capitalized()}ProtoDescriptorSet",
                        GenerateProtoDescriptorSetTask::class.java,
                    )
                    .flatMap { it.descriptorSet }
            }
            .orElse(descriptorTask.flatMap { it.descriptorSet })

        val generateTask = project.tasks.register("generate${suffix}Proto", GenerateProtoTask::class.java) { task ->
            task.group = TASK_GROUP
            task.description = "Generates Kotlin sources from the ${spec.name} protos."

            // Even when the descriptor set comes from another spec, this spec's own descriptor task still
            // produces its builtin and protoc plugin output.
            task.dependsOn(descriptorTask)

            task.descriptorSet.set(descriptorSetFile)
            task.protoFiles.setFrom(protoFiles)
            task.protoRoots.setFrom(spec.srcDirs)
            task.commonOptions.set(
                extension.options.map { shared -> shared + spec.options.getOrElse(emptyMap()) }
            )
            // Validated here rather than as the generators are declared: the rules are about the set as
            // a whole, and this provider is not read until the `proto { }` block has finished running.
            task.generators.set(
                project.provider {
                    spec.generators.toList().also { generators ->
                        GeneratorCatalog.validate(spec.name, generators.map { it.name })
                    }
                }
            )
            task.outputDirectory.set(spec.outputDirectory)
        }

        generateLifecycle.configure { it.dependsOn(generateTask) }

        registerCopyTasks(project, extension, spec, suffix, generateTask, descriptorTask, copyLifecycle)

        spec.generators.all { generator ->
            configureGenerator(project, extension, spec, sourceSet, generator, generateTask)
        }

        spec.builtins.all { builtin ->
            wireProtocOutput(
                project,
                spec,
                sourceSet,
                builtin.name,
                builtin.enabled,
                builtin.wireTo,
                descriptorTask.map { it.protocOutputDirectory.get().dir(builtin.name) },
            )
        }

        spec.protocPlugins.all { plugin ->
            wireProtocOutput(
                project,
                spec,
                sourceSet,
                plugin.name,
                plugin.enabled,
                plugin.wireTo,
                descriptorTask.map { it.protocOutputDirectory.get().dir(plugin.name) },
            )
        }
    }

    private fun registerCopyTasks(
        project: Project,
        extension: KotlinxProtobufExtension,
        spec: ProtoSourceSetSpec,
        suffix: String,
        generateTask: TaskProvider<GenerateProtoTask>,
        descriptorTask: TaskProvider<GenerateProtoDescriptorSetTask>,
        copyLifecycle: TaskProvider<Task>,
    ) {
        // Capture the property rather than the spec: an onlyIf closure is serialized into the
        // configuration cache, and the spec drags the whole project graph in with it.
        val copyEnabled = spec.copyToSrc.enabled

        // Deliberately not wired into compilation: a plain build must never rewrite the source tree.
        val copyGenerated = project.tasks.register("copy${suffix}ProtoToSrc", Sync::class.java) { task ->
            task.group = TASK_GROUP
            task.description = "Copies the generated ${spec.name} Kotlin sources into src/."
            task.onlyIf { copyEnabled.get() }

            task.from(generateTask.map { it.outputDirectory }) { copy ->
                // Every generator writes into its own subdirectory so they cannot clobber each other;
                // flatten that level back out on the way into src/.
                copy.eachFile { file -> file.relativePath = file.relativePath.dropFirstSegment() }
                copy.includeEmptyDirs = false
            }
            task.into(spec.copyToSrc.kotlinDirectory)
        }

        copyLifecycle.configure { it.dependsOn(copyGenerated) }

        // One task per protoc output, each into its own directory. A single task syncing the source set
        // root would delete every hand-written file next to the generated ones.
        val registerProtocCopy = { output: ProtocOutputSpec ->
            val copyOutput = project.tasks.register(
                "copy$suffix${output.name.capitalized()}ProtoToSrc",
                Sync::class.java,
            ) { task ->
                task.group = TASK_GROUP
                task.description = "Copies the protoc '${output.name}' output for ${spec.name} into src/."
                val outputEnabled = output.enabled
                task.onlyIf { copyEnabled.get() && outputEnabled.get() }

                task.from(descriptorTask.map { it.protocOutputDirectory.get().dir(output.name) }) { copy ->
                    copy.includeEmptyDirs = false
                }
                task.into(output.copyToSrcDirectory)
            }

            copyLifecycle.configure { it.dependsOn(copyOutput) }
        }

        spec.builtins.all(registerProtocCopy)

        spec.protocPlugins.all { plugin ->
            registerProtocCopy(plugin)
            configureProtocPlugin(project, extension, spec, suffix, plugin)
        }
    }

    /**
     * Unpacks the `.proto` files carried by this source set's own dependencies, the way the
     * `com.google.protobuf` plugin mines the compile classpath.
     *
     * That is how an `import "google/api/annotations.proto"` resolves from an ordinary dependency rather
     * than a hand-maintained include path.
     *
     * The view is lenient because a Kotlin source set's dependencies are declared without a platform in
     * mind: a common dependency that publishes no JVM variant simply contributes nothing here, instead of
     * failing a build that never asked for protos from it.
     */
    private fun extractDependencyProtos(
        project: Project,
        extension: KotlinxProtobufExtension,
        spec: ProtoSourceSetSpec,
        sourceSet: KotlinSourceSet,
        suffix: String,
    ): TaskProvider<ExtractProtoIncludesTask> {
        val configuration = project.configurations.create("kotlinxProtobuf${suffix}ProtoPath") { it ->
            it.isCanBeConsumed = false
            it.isCanBeResolved = true
            it.isVisible = false
            it.description = "Dependencies of '${spec.name}' whose bundled .proto files can be imported."

            listOf(
                sourceSet.apiConfigurationName,
                sourceSet.implementationConfigurationName,
                sourceSet.compileOnlyConfigurationName,
            ).mapNotNull(project.configurations::findByName).forEach(it::extendsFrom)

            // Jars are the same whatever variant they come from, so ask for the JVM one and take the
            // protos out of it.
            it.attributes { attributes ->
                attributes.attribute(
                    Usage.USAGE_ATTRIBUTE,
                    project.objects.named(Usage::class.java, Usage.JAVA_RUNTIME),
                )
                attributes.attribute(
                    Category.CATEGORY_ATTRIBUTE,
                    project.objects.named(Category::class.java, Category.LIBRARY),
                )
                attributes.attribute(KotlinPlatformType.attribute, KotlinPlatformType.jvm)
            }
        }

        val lenientJars = configuration.incoming.artifactView { view -> view.isLenient = true }.files

        return project.tasks.register(
            "extract${suffix}ProtoIncludes",
            ExtractProtoIncludesTask::class.java,
        ) { task ->
            task.group = TASK_GROUP
            task.description = "Unpacks the .proto files carried by the ${spec.name} dependencies."
            task.jars.setFrom(
                project.provider {
                    if (extension.includeProtosFromDependencies.get()) lenientJars else project.files()
                }
            )
            task.outputDirectory.set(
                project.layout.buildDirectory.dir("kotlinx-protobuf/dependency-protos/${spec.name}")
            )
        }
    }

    /**
     * Resolves an external protoc plugin from its coordinates, so a build does not depend on the plugin
     * being installed on the machine. A locally installed binary still wins: `executable` is set
     * explicitly there, which overrides this convention.
     */
    private fun configureProtocPlugin(
        project: Project,
        extension: KotlinxProtobufExtension,
        spec: ProtoSourceSetSpec,
        suffix: String,
        plugin: ProtocPluginSpec,
    ) {
        val configuration = project.configurations.create(
            "kotlinxProtobuf$suffix${plugin.name.capitalized()}ProtocPlugin"
        ) { it ->
            it.isCanBeConsumed = false
            it.isCanBeResolved = true
            // `java -jar` ignores everything outside the jar, so a transitive graph would be a lie.
            it.isTransitive = false
            it.isVisible = false
            it.description = "The '${plugin.name}' protoc plugin used for the '${spec.name}' protos."

            it.defaultDependencies { dependencies ->
                plugin.artifact.orNull?.let { notation ->
                    dependencies.add(project.dependencies.create(notation))
                }
            }
        }

        val prepare = project.tasks.register(
            "prepare$suffix${plugin.name.capitalized()}ProtocPlugin",
            PrepareProtocPluginTask::class.java,
        ) { task ->
            task.group = TASK_GROUP
            task.description = "Makes the '${plugin.name}' protoc plugin executable."
            task.artifact.setFrom(configuration)
            task.javaExecutable.set(
                project.providers.systemProperty("java.home").map { "$it/bin/java" }
            )
            task.executable.set(
                project.layout.buildDirectory.file(
                    "kotlinx-protobuf/bin/${PrepareProtocPluginTask.executableName(plugin.name)}"
                )
            )
        }

        plugin.executable.convention(prepare.flatMap { it.executable })
    }

    private fun configureGenerator(
        project: Project,
        extension: KotlinxProtobufExtension,
        spec: ProtoSourceSetSpec,
        sourceSet: KotlinSourceSet,
        generator: GeneratorSpec,
        generateTask: TaskProvider<GenerateProtoTask>,
    ) {
        val entry = GeneratorCatalog[generator.name]

        if (entry != null) {
            generator.generatorClass.convention(entry.generatorClass)
            generator.artifact.convention(
                extension.generatorVersion.map { version -> "$ARTIFACT_GROUP:${entry.artifactId}:$version" }
            )
        }
        generator.outputSubDirectory.convention(generator.name)

        val configurationName =
            "kotlinxProtobuf${spec.name.capitalized()}${generator.name.capitalized()}Generator"

        val configuration = project.configurations.create(configurationName) { it ->
            it.isCanBeConsumed = false
            it.isCanBeResolved = true
            // The shadow jar is self-contained; pulling transitives in would only duplicate its contents.
            it.isTransitive = false
            it.isVisible = false
            it.description = "Classpath for the '${generator.name}' generator over the '${spec.name}' protos."

            it.defaultDependencies { dependencies ->
                // Adding any dependency by hand replaces this default, which is how a build points the
                // plugin at a generator jar it builds itself.
                generator.artifact.orNull?.let { notation ->
                    dependencies.add(project.dependencies.create(notation))
                }
            }
        }

        generator.classpath.convention(configuration)

        val outputDirectory = generateTask.map {
            it.outputDirectory.get().dir(generator.outputSubDirectory.getOrElse(generator.name))
        }

        if (spec.wireGeneratedSources.get()) {
            sourceSet.kotlin.srcDir(outputDirectory)
        }

        if (entry != null) {
            registerRequiredProtocOutputs(project, extension, spec, entry)
            registerPlatformCounterparts(project, spec, sourceSet, entry)
        }
    }

    /**
     * Declares the platform halves of a multiplatform generator alongside the common half, and points
     * them at the same protos.
     *
     * `converterMultiplatform()` emits `expect` declarations; without the matching `actual`s from
     * `converterMultiplatformJvm()` and `converterMultiplatformJs()` nothing compiles. Since the halves
     * are never useful apart, asking for one asks for all of them — one per platform the project targets,
     * so a JVM-only project never hears about JavaScript.
     *
     * The link is drawn from the declaring source set rather than from the Kotlin source set hierarchy:
     * `dependsOn` is still empty while the build script runs — and, in this Kotlin version, after it
     * finishes too — so the hierarchy cannot be read at configuration time.
     */
    private fun registerPlatformCounterparts(
        project: Project,
        spec: ProtoSourceSetSpec,
        sourceSet: KotlinSourceSet,
        entry: CatalogEntry,
    ) {
        if (entry.jvmCounterpart == null && entry.jsCounterpart == null) return

        val targets = project.extensions.findByName(KOTLIN_EXTENSION) as? KotlinTargetsContainer ?: return

        // Main declarations belong with main compilations, test with test.
        val kind = if (sourceSet.name.endsWith(TEST_SUFFIX)) TEST_SUFFIX else MAIN_SUFFIX

        targets.targets.all { target ->
            val counterpart = when (target.platformType) {
                KotlinPlatformType.jvm -> entry.jvmCounterpart
                KotlinPlatformType.js -> entry.jsCounterpart
                else -> null
            } ?: return@all

            target.compilations.all { compilation ->
                val platformSourceSet = compilation.defaultSourceSet
                if (platformSourceSet == sourceSet || !platformSourceSet.name.endsWith(kind)) return@all

                val platformSpec = platformSourceSet.proto

                // The actuals are generated from the very protos the expects came from.
                platformSpec.srcDirs.from(spec.srcDirs)

                // …and from the same include path. Protos mined out of dependencies are gathered per
                // source set, from that source set's own configurations, and a Kotlin source set's
                // configurations do not extend the ones it dependsOn — so without this, an import that
                // resolved for commonMain would not resolve for jvmMain, over identical protos.
                platformSpec.includes.from(
                    spec.includes,
                    project.tasks
                        .named(
                            "extract${spec.name.capitalized()}ProtoIncludes",
                            ExtractProtoIncludesTask::class.java,
                        )
                        .map { it.outputDirectory },
                )

                // …so protoc need not run twice, unless this source set adds protos of its own, which
                // the shared descriptor set would not contain.
                platformSpec.descriptorSetFrom.convention(
                    project.provider { if (project.hasOwnProtos(platformSpec.name)) null else spec.name }
                )

                platformSpec.generator(counterpart) {}
            }
        }
    }

    /**
     * Registers the protoc outputs a generator's code calls into.
     *
     * These are not a matter of taste: `converterJvm` emits calls to protoc-gen-java's message classes
     * and `grpcJvm` to grpc-java's service stubs, so without them the generated code simply does not
     * compile. Registering them here means enabling a generator is enough.
     *
     * Both are ordinary specs afterwards, so a build can configure them — or set `enabled = false` on one
     * whose classes it gets from somewhere else.
     */
    private fun registerRequiredProtocOutputs(
        project: Project,
        extension: KotlinxProtobufExtension,
        spec: ProtoSourceSetSpec,
        entry: CatalogEntry,
    ) {
        if (entry.requiresJavaBuiltin) {
            spec.builtins.maybeCreate(JAVA_BUILTIN)
        }

        if (entry.requiresGrpcJavaPlugin) {
            spec.protocPlugins.maybeCreate(GRPC_JAVA_PLUGIN).artifact.convention(
                extension.grpcVersion.zip(ProtocPlatform.classifier(project.providers)) { version, classifier ->
                    "io.grpc:protoc-gen-grpc-java:$version:$classifier@exe"
                }
            )
        }
    }

    // --- attaching generated code to the Kotlin/Java source sets ---------------------------------------

    private fun wireProtocOutput(
        project: Project,
        spec: ProtoSourceSetSpec,
        sourceSet: KotlinSourceSet,
        name: String,
        enabled: Property<Boolean>,
        target: Property<ProtocOutputTarget>,
        directory: Provider<Directory>,
    ) {
        if (spec.wireGeneratedSources.get() && enabled.get()) {

            val javaSourceSet = project.extensions.findByType(SourceSetContainer::class.java)?.let {
                it.findByName(spec.kotlinSourceSetName.get()) ?: it.findByName("main")
            }

            when (target.getOrElse(ProtocOutputTarget.BOTH)) {
                ProtocOutputTarget.NONE -> Unit

                ProtocOutputTarget.KOTLIN -> sourceSet.kotlin.srcDir(directory)

                ProtocOutputTarget.BOTH -> {
                    sourceSet.kotlin.srcDir(directory)
                    // No warning: BOTH means "wherever it fits", and a Kotlin-only output in a project
                    // without Java sources is perfectly normal.
                    javaSourceSet?.java?.srcDir(directory)
                }

                ProtocOutputTarget.JAVA -> {
                    if (javaSourceSet == null) {
                        project.logger.warn(
                            "kotlinx-protobuf: the '$name' protoc output for '${spec.name}' is Java, but this " +
                                "project has no Java source set to compile it. Apply the java plugin, or set " +
                                "wireTo = ProtocOutputTarget.NONE when the output is only used through copyToSrc."
                        )
                    } else {
                        javaSourceSet.java.srcDir(directory)
                    }
                }
            }
        }
    }

    private fun forEachKotlinPlugin(project: Project, action: () -> Unit) {
        project.plugins.withId(KOTLIN_MULTIPLATFORM_PLUGIN) { action() }
        project.plugins.withId(KOTLIN_JVM_PLUGIN) { action() }
    }

    private fun String.capitalized(): String = replaceFirstChar { it.uppercase() }


    private fun RelativePath.dropFirstSegment(): RelativePath =
        RelativePath(true, *segments.drop(1).toTypedArray())

    /** Whether the source set has protos of its own, as opposed to only the ones it inherits. */
    private fun Project.hasOwnProtos(sourceSetName: String): Boolean {
        val directory = layout.projectDirectory.dir("src/$sourceSetName/proto").asFile
        return directory.isDirectory && directory.walkTopDown().any { it.isFile && it.extension == "proto" }
    }

    private companion object {
        const val EXTENSION_NAME = "kotlinxProtobuf"
        const val KOTLIN_EXTENSION = "kotlin"
        const val PROTO_EXTENSION = "proto"
        const val TASK_GROUP = "kotlinx-protobuf"

        const val KOTLIN_MULTIPLATFORM_PLUGIN = "org.jetbrains.kotlin.multiplatform"
        const val KOTLIN_JVM_PLUGIN = "org.jetbrains.kotlin.jvm"

        const val PROTOC_CONFIGURATION = "kotlinxProtobufProtoc"
        const val PROTO_PATH_CONFIGURATION = "kotlinxProtobufProtoPath"

        const val PREPARE_PROTOC_TASK = "prepareProtoc"
        const val EXTRACT_INCLUDES_TASK = "extractProtoIncludes"
        const val GENERATE_LIFECYCLE_TASK = "generateProto"
        const val COPY_LIFECYCLE_TASK = "copyProtoToSrc"

        const val GENERATED_ROOT = "generated/sources/kotlinx-protobuf"
        const val PROTOC_GENERATED_ROOT = "generated/sources/kotlinx-protobuf-protoc"
        const val JAVA_BUILTIN = "java"
        const val MAIN_SUFFIX = "Main"
        const val TEST_SUFFIX = "Test"
        const val GRPC_JAVA_PLUGIN = "grpc-java"

        val EXECUTABLE_SUFFIX: String
            get() = if (System.getProperty("os.name").lowercase().contains("win")) ".exe" else ""
    }
}
