import kim.jade.kotlinx.protobuf.gradle.proto

plugins {
    kotlin("jvm")
    id("kim.jade.kotlinx-protobuf")
    application
}

/**
 * **Use case: an event bus whose payload type is not known until it arrives.**
 *
 * A topic carries `UserRegistered`, `OrderPlaced` and `PaymentFailed` on the same partition, and the
 * transport has to move all three without a `Message` supertype the schema does not have. That is what
 * `google.protobuf.Any` is for: a type URL and the bytes, so the envelope stays one message and the
 * consumer decides what to decode.
 */
kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":kotlinx-protobuf-core"))
    // Any and Timestamp come from here — an Envelope field is typed as the generated google.protobuf.Any.
    implementation(project(":kotlinx-protobuf-wkt"))
    implementation(libs.protobuf.java)

    testImplementation(libs.kotest.framework.engine)
    testImplementation(libs.kotest.assertions.core)
    testRuntimeOnly(libs.kotest.runner.junit5)
}

application {
    mainClass = "example.events.DemoKt"
}

tasks.test {
    useJUnitPlatform()
}

kotlinxProtobuf {
    // Every TYPE_URL, and therefore every Any these types pack, is prefixed with this. The consumer
    // compares the whole string, so both ends have to have been generated with the same prefix — see
    // the note in the root README about systems that use none.
    typeUrlPrefix("type.googleapis.com")
}

val kotlinGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val converterJvmGenerator: Configuration by configurations.creating { isCanBeConsumed = false }

dependencies {
    kotlinGenerator(project(":kotlinx-protobuf-generator"))
    converterJvmGenerator(project(":kotlinx-protobuf-generator-converter-jvm"))
}

kotlin.sourceSets.named("main") {
    proto {
        kotlin {
            classpath.setFrom(kotlinGenerator)
            // typeUrl → KClass for every message in this compilation. Not a decoder — see Bus.kt for
            // what it is and is not good for.
            typeRegistry("example.events.v1.TypeRegistry")
        }
        converterJvm { classpath.setFrom(converterJvmGenerator) }
    }
}
