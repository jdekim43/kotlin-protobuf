import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    // Versioned through the catalog: this build has no buildSrc to put the Kotlin plugin on its script
    // classpath, the way the root build does.
    alias(kt.plugins.kotlin.jvm)
    // Brings java-gradle-plugin and maven-publish with it, so the gradlePlugin { } block below and the
    // plugin marker publication are already there.
    alias(libs.plugins.pluginPublish)
    // convention.publish-pom, not convention.publish: com.gradle.plugin-publish already produces the
    // sources and javadoc jars, and a second javadoc artifact would fail the publication.
    id("convention.publish-pom")
}

// A build of its own, so it does not inherit the root build's allprojects { group; version }. The
// version is read from the repository's single gradle.properties so the plugin, the generators it
// resolves and the runtime modules always release as one set.
group = "kim.jade"
version = file("../gradle.properties").readLines()
    .first { it.startsWith("releaseVersion=") }
    .substringAfter('=')
    .trim()

// Gradle 9 runs on Java 17+, so the plugin is compiled for it directly rather than through
// convention.kotlin-jvm, which targets Java 8 for the runtime libraries.
kotlin {
    jvmToolchain(17)

    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)

        // Build scripts are compiled by the Kotlin version embedded in Gradle, not the one used here, so
        // the plugin's metadata has to stay readable by that older compiler.
        apiVersion.set(KotlinVersion.KOTLIN_2_0)
        languageVersion.set(KotlinVersion.KOTLIN_2_0)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

// TestKit runs real builds against the plugin, which is slower than a unit test and needs a Kotlin
// plugin on the test classpath — hence a separate source set, so `test` stays fast.
val functionalTest: SourceSet by sourceSets.creating

configurations[functionalTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
configurations[functionalTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())

dependencies {
    // compileOnly, and the -api artifact only: depending on the full Kotlin Gradle Plugin would force a
    // KGP version on every consumer of this plugin.
    compileOnly(kt.kotlin.gradlePlugin.api)

    testImplementation(gradleTestKit())
    testImplementation(libs.kotest.framework.engine)
    testImplementation(libs.kotest.assertions.core)
    testRuntimeOnly(libs.kotest.runner.junit5)
}

val functionalTestTask = tasks.register<Test>("functionalTest") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Runs the Gradle TestKit tests."
    testClassesDirs = functionalTest.output.classesDirs
    classpath = functionalTest.runtimeClasspath

    // The test builds resolve the generators and the runtime modules by coordinate, so they have to be
    // in the local repository first. This build cannot depend on the root build that produces them —
    // that is the direction an included build may not point — and running the root's
    // `publishToMavenLocal` in the same invocation does not order the two either, since a composite
    // build starts an included build's tasks as soon as that build is ready. So the publishing is its
    // own invocation, and this task is reached through `./gradlew -p gradle-plugin check` after
    // `./gradlew publishToMavenLocal` in the root.
    systemProperty("kotlinxProtobufVersion", project.version.toString())
}

tasks.check {
    dependsOn(functionalTestTask)
}

val generatedDirectory = layout.buildDirectory.dir("generated/sources/main/kotlin").get()

// Read through the catalog API rather than the type-safe accessors: `grpc` and `grpc-kotlin` both exist,
// so `libs.versions.grpc` resolves to a group accessor rather than to the version itself.
val versionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
val protocVersion = versionCatalog.findVersion("protobuf").get().requiredVersion
val grpcVersion = versionCatalog.findVersion("grpc").get().requiredVersion

// The whole directory is the output, not just the file inside it: the source set below reads the whole
// directory, so a file left over from an earlier name would still be compiled in.
val writePluginConstants = tasks.register("writePluginConstants") {
    val pluginVersion = providers.provider { project.version.toString() }
    val outputDirectory = generatedDirectory.asFile
    val outputFile = outputDirectory.resolve("kim/jade/kotlinx/protobuf/gradle/BuildConstants.kt")

    inputs.property("pluginVersion", pluginVersion)
    inputs.property("protocVersion", protocVersion)
    inputs.property("grpcVersion", grpcVersion)
    outputs.dir(outputDirectory)

    doLast {
        outputDirectory.deleteRecursively()
        val output = outputFile
        output.parentFile.mkdirs()
        output.writeText(
            """
            package kim.jade.kotlinx.protobuf.gradle

            /** Version of this plugin, and the default version of the generator and runtime artifacts. */
            internal const val PLUGIN_VERSION: String = "${pluginVersion.get()}"

            /** protoc version used unless the build overrides it. */
            internal const val DEFAULT_PROTOC_VERSION: String = "$protocVersion"

            /** grpc version the protoc-gen-grpc-java plugin is taken from unless the build overrides it. */
            internal const val DEFAULT_GRPC_VERSION: String = "$grpcVersion"

            /** Maven group of the generator and runtime artifacts. */
            internal const val ARTIFACT_GROUP: String = "kim.jade"
            """.trimIndent() + "\n"
        )
    }
}

sourceSets {
    main {
        kotlin.srcDir(files(generatedDirectory).builtBy(writePluginConstants))
    }
}

gradlePlugin {
    website.set("https://github.com/jdekim43/kotlinx-protobuf")
    vcsUrl.set("https://github.com/jdekim43/kotlinx-protobuf.git")

    plugins {
        create("kotlinxProtobuf") {
            id = "kim.jade.kotlinx-protobuf"
            implementationClass = "kim.jade.kotlinx.protobuf.gradle.KotlinxProtobufPlugin"
            displayName = "Kotlin Protobuf"
            description = "Generates Kotlin Multiplatform code from .proto files, without protoc-gen-java"
            tags.set(listOf("protobuf", "protoc", "kotlin", "kotlin-multiplatform", "grpc", "codegen"))
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

// The same staging hygiene the root build applies to its own modules. It cannot reach in here — this is
// a separate build — so the wiring is repeated rather than shared.
val clearStagingDirectory = tasks.register<Delete>("clearStagingDirectory") {
    description = "Deletes the staged publications, so a release carries only what this build produced."
    delete(layout.buildDirectory.dir("staging-deploy"))
}

tasks.withType<PublishToMavenRepository>().configureEach {
    dependsOn(clearStagingDirectory)
}
