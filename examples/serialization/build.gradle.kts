import kim.jade.kotlinx.protobuf.gradle.proto

plugins {
    // No version: the Kotlin plugin is already on this build's classpath, through build-logic.
    kotlin("jvm")
    // kotlinxSerialization() emits @Serializable types, so the serialization compiler plugin has to be on.
    alias(kt.plugins.kotlinx.serialization)
    id("kim.jade.kotlinx-protobuf")
    application
}

/**
 * **Use case: one schema, compact bytes, and JSON when a human has to read it.**
 *
 * The oldest reason to reach for protobuf and still the most common: a record that outlives the process
 * that wrote it — put on a queue, cached, written to a column, sent to a service in another language —
 * and needs a definition both ends agree on. `order.proto` is that definition; everything Kotlin sees is
 * generated from it.
 */
kotlin {
    jvmToolchain(17)
}

dependencies {
    // In a consumer's build these are coordinates, not projects:
    //
    //     implementation("kim.jade:kotlinx-protobuf:<version>")            // core + wkt + protobuf-java
    //     implementation("kim.jade:kotlinx-protobuf-serialization:<version>")
    implementation(project(":kotlinx-protobuf-core"))
    implementation(project(":kotlinx-protobuf-wkt"))
    implementation(project(":kotlinx-protobuf-serialization"))
    // converterJvm() delegates to protoc-gen-java's message classes, so they are on the compile path.
    implementation(libs.protobuf.java)

    testImplementation(libs.kotest.framework.engine)
    testImplementation(libs.kotest.assertions.core)
    testRuntimeOnly(libs.kotest.runner.junit5)
}

application {
    mainClass = "example.order.DemoKt"
}

tasks.test {
    useJUnitPlatform()
}

kotlinxProtobuf {
    // Goes into every TYPE_URL, and therefore into every Any these types pack. protobuf's own default.
    typeUrlPrefix("type.googleapis.com")
}

// The generators as sibling projects rather than published coordinates. This block is the one thing
// here a consumer's build does not have: outside this repository the plugin resolves each generator by
// coordinate, and `kotlinxSerialization()` / `converterJvm()` on their own are the whole declaration.
val serializationGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val converterJvmGenerator: Configuration by configurations.creating { isCanBeConsumed = false }

dependencies {
    serializationGenerator(project(":kotlinx-protobuf-generator-serialization"))
    converterJvmGenerator(project(":kotlinx-protobuf-generator-converter-jvm"))
}

kotlin.sourceSets.named("main") {
    proto {
        // From src/main/proto. kotlinxSerialization() is kotlin() plus the serialization plugins, so
        // the same messages arrive usable by both the converters and kotlinx.serialization's formats.
        kotlinxSerialization { classpath.setFrom(serializationGenerator) }
        converterJvm { classpath.setFrom(converterJvmGenerator) }
    }
}
