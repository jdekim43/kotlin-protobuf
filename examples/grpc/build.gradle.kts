import kim.jade.kotlinx.protobuf.gradle.proto

plugins {
    kotlin("jvm")
    id("kim.jade.kotlinx-protobuf")
    application
}

/**
 * **Use case: a service contract two teams build against.**
 *
 * `chat.proto` declares four RPCs, one of each streaming shape, and the generator turns them into an
 * interface of `suspend` functions and `Flow`s. The server implements that interface; the client is that
 * interface. Neither side writes a stub, a marshaller or a method descriptor, and neither can drift from
 * the schema without the compiler saying so.
 */
kotlin {
    // 17, not 8: grpc-kotlin-stub is compiled for 17, and :kotlinx-protobuf-grpc raises its target to
    // match.
    jvmToolchain(17)
}

dependencies {
    implementation(project(":kotlinx-protobuf-core"))
    // Any converter generator needs this: the toAny() helpers it emits reference google.protobuf.Any,
    // and chat.proto imports Timestamp on top of that.
    implementation(project(":kotlinx-protobuf-wkt"))
    implementation(project(":kotlinx-protobuf-grpc"))
    implementation(libs.protobuf.java)
    // Streaming RPCs are Flows. kotlinx-protobuf-core compiles against coroutines but does not pass them
    // on, so a build with a streaming RPC declares them like any other library.
    implementation(kt.kotlinx.coroutine)
    // A transport. grpc-java ships several; this one is the usual choice for a JVM server, and it is
    // what makes `run` an actual server on an actual port rather than an in-process fixture.
    implementation(libs.grpc.netty.shaded)

    testImplementation(libs.kotest.framework.engine)
    testImplementation(libs.kotest.assertions.core)
    testRuntimeOnly(libs.kotest.runner.junit5)
}

application {
    mainClass = "example.chat.DemoKt"
}

tasks.test {
    useJUnitPlatform()
}

kotlinxProtobuf {
    typeUrlPrefix("type.googleapis.com")
}

val kotlinGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val converterJvmGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val grpcJvmGenerator: Configuration by configurations.creating { isCanBeConsumed = false }

dependencies {
    kotlinGenerator(project(":kotlinx-protobuf-generator"))
    converterJvmGenerator(project(":kotlinx-protobuf-generator-converter-jvm"))
    grpcJvmGenerator(project(":kotlinx-protobuf-generator-grpc-jvm"))
}

kotlin.sourceSets.named("main") {
    proto {
        kotlin { classpath.setFrom(kotlinGenerator) }
        converterJvm { classpath.setFrom(converterJvmGenerator) }
        // grpcJvm(), not grpcMultiplatform(): those emit `expect` declarations, which need a second
        // source set to hold the `actual`s. It registers the `java` builtin and protoc-gen-grpc-java for
        // itself, because the generated clients and servers call into both.
        grpcJvm { classpath.setFrom(grpcJvmGenerator) }
    }
}
