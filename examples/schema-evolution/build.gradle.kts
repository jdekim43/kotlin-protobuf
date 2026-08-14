import kim.jade.kotlinx.protobuf.gradle.proto

plugins {
    kotlin("jvm")
    id("kim.jade.kotlinx-protobuf")
    application
}

/**
 * **Use case: two versions of the same message, deployed at the same time.**
 *
 * The reason a schema language has field numbers at all. During a rolling deploy the old service and the
 * new one are both running, both reading each other's bytes, and neither can be upgraded first. This
 * module generates `profile/v1` and `profile/v2` side by side so the cross-version decodes can actually
 * be run rather than reasoned about.
 */
kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":kotlinx-protobuf-core"))
    implementation(project(":kotlinx-protobuf-wkt"))
    implementation(libs.protobuf.java)

    testImplementation(libs.kotest.framework.engine)
    testImplementation(libs.kotest.assertions.core)
    testRuntimeOnly(libs.kotest.runner.junit5)
}

application {
    mainClass = "example.profile.DemoKt"
}

tasks.test {
    useJUnitPlatform()
}

kotlinxProtobuf {
    typeUrlPrefix("type.googleapis.com")
}

// Only here because the generators are built by this repository; see examples/serialization for the
// consumer-facing form.
val kotlinGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val converterJvmGenerator: Configuration by configurations.creating { isCanBeConsumed = false }

dependencies {
    kotlinGenerator(project(":kotlinx-protobuf-generator"))
    converterJvmGenerator(project(":kotlinx-protobuf-generator-converter-jvm"))
}

kotlin.sourceSets.named("main") {
    proto {
        // kotlin(), not kotlinxSerialization(): this module is about the wire format, and plain data
        // classes are all it needs.
        kotlin { classpath.setFrom(kotlinGenerator) }
        converterJvm { classpath.setFrom(converterJvmGenerator) }
    }
}
