import kim.jade.kotlinx.protobuf.gradle.proto
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("convention.kotlin-multiplatform")
    // kotlinxSerialization() emits @Serializable types, and the grpc-gateway clients encode their
    // bodies through kotlinx.serialization.
    alias(kt.plugins.kotlinx.serialization)
    // This repository's own plugin, applied inside the build that produces it — gradle-plugin is an
    // included build, so there is no publish-then-resolve round trip. Nothing here is published: this
    // module is a consumer of the plugin, not something the repository ships.
    id("kim.jade.kotlinx-protobuf")
}

/**
 * The plugin against a real-world schema, exercised the way a consumer would.
 *
 * Everything in `gradle-plugin`'s TestKit suite builds protos written for the test — which is how the
 * tricky cases stay legible, and also how they stay unrepresentative. cosmos-sdk brings what real
 * schemas bring: 120-odd files, imports several levels deep, `Any`, extensions on nearly every message,
 * `map` fields, recursive types and 40 services.
 *
 * That the generated code compiles is the weaker half of the claim. The tests alongside this file check
 * the stronger one: that the types it produces actually round-trip, that the bytes they produce are the
 * bytes protobuf-java produces for the same message, and that the gRPC and REST clients built on top of
 * them carry a call end to end.
 *
 * kotlinxSerialization() stands in for kotlin() here. It is that generator plus a set of plugins — the
 * same message, enum and service generators underneath — so nothing is lost by preferring it, and the
 * serialization plugins and the grpc-gateway clients that need @Serializable types come along.
 *
 * The versions are pinned. Following a branch would break this build on commits that never touched it,
 * for reasons belonging to someone else's repository.
 */
val cosmosSdkVersion = "v0.55.0"
val cosmosProtoVersion = "v1.0.0-beta.5"
val gogoprotoVersion = "v1.7.2"

kotlin {
    // The converters and gRPC factories come in expect/actual class pairs.
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    // 17, not the convention's 8: grpc-kotlin-stub is compiled for 17, and :kotlinx-protobuf-grpc
    // raises its own target for the same reason.
    jvmToolchain(17)

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Declared here, not on jvmMain: cosmos protos import google/api/annotations.proto, and the
            // plugin mines a source set's own dependencies for the .proto files they carry.
            api(libs.googleCommonProtos)
            api(project(":kotlinx-protobuf-core"))
            api(project(":kotlinx-protobuf-wkt"))
            api(project(":kotlinx-protobuf-grpc"))
            api(project(":kotlinx-protobuf-serialization"))
            api(project(":kotlinx-protobuf-grpc-gateway"))
            api(kt.kotlinx.coroutine)
        }
        commonTest.dependencies {
            // The JSON mapping is asserted on parsed documents rather than raw strings, because the two
            // implementations disagree about whitespace and nothing else.
            implementation(kt.kotlinx.json)
        }
        jvmMain.dependencies {
            api(libs.protobuf.java)
        }
        jvmTest.dependencies {
            implementation(libs.grpc.inprocess)
            // Answers the gateway clients' HTTP calls in-process, so their requests can be inspected.
            implementation(kt.ktor.client.mock)
            // …and a real client and server, so one gateway call also crosses an actual socket.
            implementation(kt.ktor.client.cio)
            implementation(kt.ktor.server.core)
            implementation(kt.ktor.server.cio)
        }
    }
}

// No browser runner for this module's JS tests. CosmosGrpcJsTest drives `@grpc/grpc-js`, and a bundle
// that references it cannot be loaded in a browser: its modules extend classes out of Node's `stream`
// and reach into `http2` while they are still being imported, and `http2` has no browser polyfill to
// stand in. The rest of the JS tests here are platform-neutral and covered on Node.
tasks.named("jsBrowserTest") { enabled = false }

// CosmosLiveLcdTest talks to a public Cosmos node. It is the only test here whose result depends on
// somebody else's uptime, so it is off unless asked for — a chain node going down should not turn this
// repository red.
//
//     ./gradlew :integration-test:allTests -PliveTests=true
tasks.withType<Test>().configureEach {
    systemProperty(
        "kotlinxProtobuf.liveTests",
        providers.gradleProperty("liveTests").getOrElse("false"),
    )
    systemProperty(
        "kotlinxProtobuf.lcdUrl",
        providers.gradleProperty("lcdUrl").getOrElse("https://cosmos-rest.publicnode.com"),
    )
}

// The generators as sibling projects rather than published coordinates. Same arrangement as
// :kotlinx-protobuf-wkt, and load-bearing: left to resolve by coordinate, every generator here would
// come from whatever version is in the local Maven repository, and these tests would quietly check the
// last release instead of the working tree.
val serializationGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val converterGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val converterJvmGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val converterJsGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val grpcGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val grpcJvmGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val grpcJsGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val gatewayGenerator: Configuration by configurations.creating { isCanBeConsumed = false }

/** Third-party proto trees, resolved as artifacts through the codeload ivy repository in settings. */
val cosmosSdkProtos: Configuration by configurations.creating { isCanBeConsumed = false; isTransitive = false }
val cosmosProtoProtos: Configuration by configurations.creating { isCanBeConsumed = false; isTransitive = false }
val gogoprotoProtos: Configuration by configurations.creating { isCanBeConsumed = false; isTransitive = false }

dependencies {
    serializationGenerator(project(":kotlinx-protobuf-generator-serialization"))
    converterGenerator(project(":kotlinx-protobuf-generator-converter-multiplatform"))
    converterJvmGenerator(project(":kotlinx-protobuf-generator-converter-multiplatform-jvm"))
    converterJsGenerator(project(":kotlinx-protobuf-generator-converter-multiplatform-js"))
    grpcGenerator(project(":kotlinx-protobuf-generator-grpc-multiplatform"))
    grpcJvmGenerator(project(":kotlinx-protobuf-generator-grpc-multiplatform-jvm"))
    grpcJsGenerator(project(":kotlinx-protobuf-generator-grpc-multiplatform-js"))
    gatewayGenerator(project(":kotlinx-protobuf-generator-grpc-gateway"))

    cosmosSdkProtos("cosmos:cosmos-sdk:$cosmosSdkVersion@zip")
    cosmosProtoProtos("cosmos:cosmos-proto:$cosmosProtoVersion@zip")
    gogoprotoProtos("cosmos:gogoproto:$gogoprotoVersion@zip")
}

/** Drops the archive's own leading directories, so the proto package becomes the path protoc sees. */
fun RelativePath.dropLeading(count: Int): RelativePath =
    RelativePath(true, *segments.drop(count).toTypedArray())

/**
 * cosmos-sdk's own protos: the `cosmos`, `amino` and `tendermint` packages.
 *
 * Only the top-level proto directory, not the whole archive: the repository also carries a `contrib`
 * proto tree, a module of its own whose imports this build never asked for.
 */
val extractCosmosSdkProtos by tasks.registering(Sync::class) {
    from(zipTree(cosmosSdkProtos.elements.map { it.single() })) {
        include("*/proto/**/*.proto")
        eachFile { relativePath = relativePath.dropLeading(2) }
        includeEmptyDirs = false
    }
    into(layout.buildDirectory.dir("cosmos-protos"))
}

/**
 * The three files cosmos-sdk imports from outside its own tree.
 *
 * Picked out one by one rather than pointing protoc at each repository wholesale: cosmos-proto and
 * gogoproto both also carry test protos — `testpb/1.proto` and friends — that have no business being
 * on an include path, let alone being generated.
 */
val extractDependencyProtos by tasks.registering(Sync::class) {
    from(zipTree(cosmosProtoProtos.elements.map { it.single() })) {
        include("*/proto/cosmos_proto/cosmos.proto")
        eachFile { relativePath = relativePath.dropLeading(2) }
        includeEmptyDirs = false
    }
    from(zipTree(gogoprotoProtos.elements.map { it.single() })) {
        include("*/gogoproto/gogo.proto")
        eachFile { relativePath = relativePath.dropLeading(1) }
        includeEmptyDirs = false
    }
    into(layout.buildDirectory.dir("cosmos-dependency-protos"))
}

kotlinxProtobuf {
    // No typeUrlPrefix on purpose. A type URL is "<prefix>/<fully.qualified.name>", and Cosmos uses no
    // prefix at all — an Any on that chain carries "/cosmos.bank.v1beta1.MsgSend". Setting
    // type.googleapis.com here would generate a TYPE_URL no cosmos node agrees with, and a packed Any
    // built from it would be rejected on broadcast.
}

// named() rather than the generated accessors, which are not in scope for a target's source sets
// outside the kotlin { } block.
kotlin.sourceSets.named("commonMain") {
    proto {
        // The dependency protos are generated, not merely imported: the JVM converters delegate to
        // protoc-gen-java's classes, and cosmos messages carry cosmos_proto and gogoproto extensions,
        // so that Java has to exist.
        srcDirs.setFrom(
            extractCosmosSdkProtos.map { it.destinationDir },
            extractDependencyProtos.map { it.destinationDir },
        )

        kotlinxSerialization { classpath.setFrom(serializationGenerator) }
        converterMultiplatform { classpath.setFrom(converterGenerator) }
        grpcMultiplatform { classpath.setFrom(grpcGenerator) }
        grpcGateway { classpath.setFrom(gatewayGenerator) }
    }
}

kotlin.sourceSets.named("jvmMain") {
    proto {
        // protobuf's JSON mapping cannot decode an `Any` without a descriptor for what it holds, and a
        // real cosmos transaction is a list of them. This emits the TypeRegistry that resolves them.
        converterMultiplatformJvm {
            classpath.setFrom(converterJvmGenerator)
            jvmTypeRegistry("cosmos.JvmTypeRegistry")
        }
        grpcMultiplatformJvm { classpath.setFrom(grpcJvmGenerator) }
    }
}

kotlin.sourceSets.named("jsMain") {
    proto {
        // protobuf.js resolves an Any out of the descriptors it has, and a cosmos transaction is a list
        // of them. This emits the list of generated files that describe them.
        converterMultiplatformJs {
            classpath.setFrom(converterJsGenerator)
            jsTypeRegistry("cosmos.JsTypeRegistry")
        }
        grpcMultiplatformJs { classpath.setFrom(grpcJsGenerator) }
    }
}
