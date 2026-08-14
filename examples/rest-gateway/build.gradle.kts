import kim.jade.kotlinx.protobuf.gradle.proto
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    // Kotlin Multiplatform, because this is the case that argues for it: the REST client is generated
    // into commonMain, so a shared module can call the API a JVM backend serves.
    id("convention.kotlin-multiplatform")
    alias(kt.plugins.kotlinx.serialization)
    id("kim.jade.kotlinx-protobuf")
}

/**
 * **Use case: the same schema serving HTTP/JSON, not only gRPC.**
 *
 * `library.proto` carries `google.api.http` options — the ones a grpc-gateway, an Envoy transcoder or an
 * ESPv2 deployment is configured from. `grpcGateway()` reads the same options and generates a Ktor client
 * that speaks that REST surface: path templates filled from the request, the leftover fields as query
 * parameters, bodies encoded with protobuf's JSON mapping.
 *
 * The server here is a plain Ktor application standing in for the gateway, so the example runs end to end
 * in one process. In a real deployment it is the gateway that serves these routes and forwards to gRPC.
 */
kotlin {
    // converterMultiplatform() emits `expect class` declarations and its JVM half the `actual`s.
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    jvmToolchain(17)

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    sourceSets {
        commonMain.dependencies {
            // library.proto imports google/api/annotations.proto, and the plugin mines a source set's own
            // dependencies for the .proto files they carry — so the import needs no extra include path.
            api(libs.googleCommonProtos)
            api(project(":kotlinx-protobuf-core"))
            api(project(":kotlinx-protobuf-wkt"))
            api(project(":kotlinx-protobuf-serialization"))
            api(project(":kotlinx-protobuf-grpc-gateway"))
            api(kt.kotlinx.coroutine)
        }
        jvmMain.dependencies {
            api(libs.protobuf.java)
            // A client engine and a server, so the demo is an actual HTTP round trip.
            api(kt.ktor.client.cio)
            api(kt.ktor.server.core)
            api(kt.ktor.server.cio)
        }
        jvmTest.dependencies {
            // Answers the generated client in-process, so its requests can be inspected.
            implementation(kt.ktor.client.mock)
        }
    }
}

// The `application` plugin drives the java plugin's `main` source set, which a multiplatform project does
// not have — so the demo gets a JavaExec of its own over the JVM target's runtime classpath.
val jvmMain = kotlin.jvm().compilations.getByName("main")
tasks.register<JavaExec>("run") {
    group = "application"
    description = "Runs the REST gateway demo: a Ktor server, and the generated client calling it."
    classpath = jvmMain.output.allOutputs + jvmMain.runtimeDependencyFiles
    mainClass = "example.library.DemoKt"
}

kotlinxProtobuf {
    typeUrlPrefix("type.googleapis.com")
}

val serializationGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val converterGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val converterJvmGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val gatewayGenerator: Configuration by configurations.creating { isCanBeConsumed = false }

dependencies {
    serializationGenerator(project(":kotlinx-protobuf-generator-serialization"))
    converterGenerator(project(":kotlinx-protobuf-generator-converter-multiplatform"))
    converterJvmGenerator(project(":kotlinx-protobuf-generator-converter-multiplatform-jvm"))
    gatewayGenerator(project(":kotlinx-protobuf-generator-grpc-gateway"))
}

kotlin.sourceSets.named("commonMain") {
    proto {
        // The gateway clients encode their bodies through kotlinx.serialization, so the types have to be
        // the annotated ones.
        kotlinxSerialization { classpath.setFrom(serializationGenerator) }
        converterMultiplatform { classpath.setFrom(converterGenerator) }
        grpcGateway { classpath.setFrom(gatewayGenerator) }
    }
}

kotlin.sourceSets.named("jvmMain") {
    proto {
        // Registered by converterMultiplatform() over the same protos and the same protoc run; this block
        // only configures it.
        converterMultiplatformJvm { classpath.setFrom(converterJvmGenerator) }
    }
}
