package kim.jade.kotlinx.protobuf.gradle

import io.kotest.core.spec.style.StringSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.file.shouldNotExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.gradle.testkit.runner.TaskOutcome

class KotlinxProtobufPluginTest : StringSpec({

    fun jvmProject(protoBlock: String = "kotlin()"): TestProject = TestProject(tempdir()).apply {
        write(
            "build.gradle.kts",
            """
            import kim.jade.kotlinx.protobuf.gradle.proto

            plugins {
                kotlin("jvm") version "2.4.10"
                id("kim.jade.kotlinx-protobuf") version "$pluginVersion"
            }
            kotlin { jvmToolchain(17) }
            dependencies {
                implementation("kim.jade:kotlinx-protobuf-core:$pluginVersion")
                implementation("kim.jade:kotlinx-protobuf-wkt:$pluginVersion")
                implementation("com.google.protobuf:protobuf-java:4.35.1")
            }
            kotlinxProtobuf {
                generatorVersion = "$pluginVersion"
                typeUrlPrefix("type.googleapis.com")
            }
            kotlin.sourceSets.named("main") {
                proto {
                    $protoBlock
                }
            }
            """.trimIndent(),
        )
        write("src/main/proto/demo/v1/greeter.proto", TestProject.GREETER_PROTO)
    }

    fun multiplatformProject(): TestProject = TestProject(tempdir()).apply {
        write(
            "build.gradle.kts",
            """
            import kim.jade.kotlinx.protobuf.gradle.proto

            plugins {
                kotlin("multiplatform") version "2.4.10"
                id("kim.jade.kotlinx-protobuf") version "$pluginVersion"
            }
            kotlinxProtobuf {
                generatorVersion = "$pluginVersion"
            }
            kotlin {
                jvmToolchain(17)
                jvm()

                sourceSets {
                    commonMain {
                        proto {
                            kotlin()
                            converterMultiplatform()
                        }
                        dependencies {
                            implementation("kim.jade:kotlinx-protobuf-core:$pluginVersion")
                            implementation("kim.jade:kotlinx-protobuf-wkt:$pluginVersion")
                        }
                    }
                    jvmMain.dependencies {
                        implementation("com.google.protobuf:protobuf-java:4.35.1")
                    }
                }
            }
            """.trimIndent(),
        )
        write("src/commonMain/proto/demo/v1/greeter.proto", TestProject.GREETER_PROTO)
    }

    "generates Kotlin for a kotlin-jvm project and compiles it" {
        val project = jvmProject()

        val result = project.build("build")

        result.task(":generateMainProto")?.outcome shouldBe TaskOutcome.SUCCESS
        result.task(":compileKotlin")?.outcome shouldBe TaskOutcome.SUCCESS
        project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/v1/greeter.kt").shouldExist()
    }

    "puts expects in commonMain and actuals in jvmMain for a multiplatform project" {
        val project = multiplatformProject()

        project.build("build")

        val common = project.file(
            "build/generated/sources/kotlinx-protobuf/commonMain/converterMultiplatform/demo/v1/greeter.converter.kt"
        )
        val jvm = project.file(
            "build/generated/sources/kotlinx-protobuf/jvmMain/converterMultiplatformJvm/demo/v1/greeter.converter.kt"
        )

        common.shouldExist()
        jvm.shouldExist()
        common.readText() shouldContain "expect object HelloRequestConverter"
        jvm.readText() shouldContain "actual object HelloRequestConverter"
    }

    "brings the JVM half of a multiplatform generator along" {
        // converterMultiplatform() emits `expect`s; declaring it without its `actual`s never compiles, so
        // the pair is declared together — over the same protos, from one protoc run.
        val project = multiplatformProject()

        project.build("build")

        project.file(
            "build/generated/sources/kotlinx-protobuf/jvmMain/converterMultiplatformJvm/demo/v1/greeter.converter.kt"
        ).shouldExist()
        // The java builtin the JVM converters delegate to came along too.
        project.file("build/generated/sources/kotlinx-protobuf-protoc/jvmMain/java").shouldExist()
        // And jvmMain reused commonMain's descriptor set rather than running protoc again.
        project.file("build/kotlinx-protobuf/descriptors/commonMain.pb").shouldExist()
        project.file("build/kotlinx-protobuf/descriptors/jvmMain.pb").shouldNotExist()
    }

    "is up to date on a second run" {
        val project = jvmProject()
        project.build("generateProto")

        val result = project.build("generateProto")

        result.task(":generateMainProtoDescriptorSet")?.outcome shouldBe TaskOutcome.UP_TO_DATE
        result.task(":generateMainProto")?.outcome shouldBe TaskOutcome.UP_TO_DATE
    }

    "removes the generated file when its proto is deleted" {
        val project = jvmProject()
        project.write(
            "src/main/proto/demo/v1/extra.proto",
            """
            syntax = "proto3";
            package demo.v1;
            message Extra { string a = 1; }
            """.trimIndent(),
        )
        project.build("generateProto")
        project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/v1/extra.kt").shouldExist()

        project.file("src/main/proto/demo/v1/extra.proto").delete()
        project.build("generateProto")

        project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/v1/extra.kt").shouldNotExist()
        project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/v1/greeter.kt").shouldExist()
    }

    "reuses the configuration cache" {
        val project = jvmProject()
        project.build("generateProto", "--configuration-cache")

        val result = project.build("generateProto", "--configuration-cache")

        result.output shouldContain "Configuration cache entry reused"
    }

    "keeps generator state from leaking between source sets" {
        // The registry generators accumulate descriptors in Kotlin objects and read their options from
        // JVM system properties, so two invocations sharing a worker would produce each other's output.
        val project = TestProject(tempdir()).apply {
            write(
                "build.gradle.kts",
                """
                import kim.jade.kotlinx.protobuf.gradle.proto

                plugins {
                    kotlin("multiplatform") version "2.4.10"
                    id("kim.jade.kotlinx-protobuf") version "$pluginVersion"
                }
                kotlinxProtobuf {
                    generatorVersion = "$pluginVersion"
                }
                kotlin {
                    jvmToolchain(17)
                    jvm()

                    sourceSets {
                        commonMain {
                            proto { kotlin { typeRegistry("demo.v1.CommonRegistry") } }
                        }
                        jvmMain {
                            proto { kotlin { typeRegistry("other.v1.JvmRegistry") } }
                        }
                    }
                }
                """.trimIndent(),
            )
            write("src/commonMain/proto/demo/v1/greeter.proto", TestProject.GREETER_PROTO)
            write(
                "src/jvmMain/proto/other/v1/other.proto",
                """
                syntax = "proto3";
                package other.v1;
                message Other { string a = 1; }
                """.trimIndent(),
            )
        }

        project.build("generateProto")

        val common =
            project.file("build/generated/sources/kotlinx-protobuf/commonMain/kotlin/demo/v1/CommonRegistry.kt")
        val jvm =
            project.file("build/generated/sources/kotlinx-protobuf/jvmMain/kotlin/other/v1/JvmRegistry.kt")

        common.shouldExist()
        jvm.shouldExist()
        // Each registry lists only its own messages.
        common.readText() shouldContain "demo.v1.HelloRequest"
        jvm.readText() shouldContain "other.v1.Other"
        jvm.readText() shouldNotContain "HelloRequest"
    }

    "routes each option to the generator that reads it" {
        val project = jvmProject(
            """
            kotlin { typeRegistry("demo.v1.TypeRegistry") }
                    converterJvm { jvmTypeRegistry("demo.v1.JvmTypeRegistry") }
            """.trimIndent(),
        )

        project.build("generateProto")

        // Each registry is emitted by, and only by, the generator that understands its option.
        project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/v1/TypeRegistry.kt").shouldExist()
        project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/v1/JvmTypeRegistry.kt")
            .shouldNotExist()
        project.file("build/generated/sources/kotlinx-protobuf/main/converterJvm/demo/v1/JvmTypeRegistry.kt")
            .shouldExist()
        project.file("build/generated/sources/kotlinx-protobuf/main/converterJvm/demo/v1/TypeRegistry.kt")
            .shouldNotExist()
        // The prefix set on the extension still reaches every generator.
        project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/v1/TypeRegistry.kt")
            .readText() shouldContain "type.googleapis.com/demo.v1.HelloRequest"
    }

    "passes builtin options through to protoc" {
        val project = jvmProject(
            """
            kotlin()
                    builtin("java") {
                        // A bare flag, the shape most protoc builtin options take.
                        option("lite")
                        wireToNothing()
                    }
            """.trimIndent(),
        )

        project.build("generateProto")

        val generated =
            project.file("build/generated/sources/kotlinx-protobuf-protoc/main/java/demo/v1/Greeter.java")
        generated.shouldExist()
        // protoc only emits the lite runtime types when it actually received the option.
        generated.readText() shouldContain "GeneratedMessageLite"
    }

    "runs a jar-based protoc plugin resolved from coordinates" {
        // Nothing is installed on the machine: the plugin comes from a repository, and the launcher that
        // lets protoc exec a jar is generated by the build.
        val project = TestProject(tempdir())
        val protoBlock = """
            protocPlugin("kotlinx-protobuf") {
                        artifact = "kim.jade:kotlinx-protobuf-generator:${project.pluginVersion}"
                        option("kotlinx-protobuf.prefix", "type.googleapis.com")
                    }
        """.trimIndent()
        val configured = jvmProject(protoBlock)

        val result = configured.build("generateProto")

        result.task(":prepareMainKotlinx-protobufProtocPlugin")?.outcome shouldBe TaskOutcome.SUCCESS

        val generated = configured.file(
            "build/generated/sources/kotlinx-protobuf-protoc/main/kotlinx-protobuf/demo/v1/greeter.kt"
        )
        generated.shouldExist()
        generated.readText() shouldContain "type.googleapis.com/demo.v1.HelloRequest"
    }

    "compiles the Java a protoc plugin emits, not just resolves it" {
        // Wiring Java output only to the Kotlin sources compiles fine — kotlinc reads .java for symbol
        // resolution — but javac never sees it, so the class is missing at runtime. Guard against that.
        val project = jvmProject("""builtin("java") { wireToBoth() }""")

        project.build("build")

        // The outer class javac produced, which only exists if javac actually ran over the output.
        project.file("build/classes/java/main/demo/v1/Greeter.class").shouldExist()
    }

    "registers the protoc outputs a generator's code calls into" {
        val project = TestProject(tempdir()).apply {
            write(
                "build.gradle.kts",
                """
                import kim.jade.kotlinx.protobuf.gradle.proto

                plugins {
                    kotlin("jvm") version "2.4.10"
                    id("kim.jade.kotlinx-protobuf") version "$pluginVersion"
                }
                kotlin { jvmToolchain(17) }
                dependencies {
                    implementation("kim.jade:kotlinx-protobuf-core:$pluginVersion")
                    implementation("kim.jade:kotlinx-protobuf-wkt:$pluginVersion")
                    implementation("kim.jade:kotlinx-protobuf-grpc:$pluginVersion")
                    implementation("com.google.protobuf:protobuf-java:4.35.1")
                }
                kotlinxProtobuf {
                    generatorVersion = "$pluginVersion"
                    typeUrlPrefix("type.googleapis.com")
                }
                // No builtins, no protocPlugins: grpcJvm needs both and should bring them along.
                kotlin.sourceSets.named("main") {
                    proto {
                        kotlin()
                        converterJvm()
                        grpcJvm()
                    }
                }
                """.trimIndent(),
            )
            write("src/main/proto/demo/v1/greeter.proto", TestProject.SERVICE_PROTO)
        }

        project.build("build")

        project.file("build/generated/sources/kotlinx-protobuf-protoc/main/java/demo/v1/GreeterOuterClass.java")
            .shouldExist()
        project.file("build/generated/sources/kotlinx-protobuf-protoc/main/grpc-java/demo/v1/GreeterGrpc.java")
            .shouldExist()
        // And they are compiled, not merely generated.
        project.file("build/classes/java/main/demo/v1/GreeterGrpc.class").shouldExist()
    }

    "lets a build decline a protoc output it does not need" {
        val project = jvmProject(
            """
            converterJvm()
                    // The delegator classes come from a dependency here, so protoc need not emit them.
                    builtin("java") { enabled = false }
            """.trimIndent(),
        )

        project.build("generateProto")

        project.file("build/generated/sources/kotlinx-protobuf-protoc/main/java").shouldNotExist()
    }

    "copies generated sources into src only when asked" {
        val project = jvmProject(
            """
            kotlin()
                    copyToSrc {
                        enabled = true
                        kotlinDirectory = layout.projectDirectory.dir("src/main/generated")
                    }
            """.trimIndent(),
        )

        // A plain build must never rewrite the source tree.
        project.build("build")
        project.file("src/main/generated").shouldNotExist()

        project.build("copyProtoToSrc")
        project.file("src/main/generated/demo/v1/greeter.kt").shouldExist()
    }

    "imports protos carried by an ordinary dependency" {
        // Nothing points protoc at google/api/annotations.proto: it comes out of the dependency's jar,
        // the way the com.google.protobuf plugin mines the compile classpath.
        val project = TestProject(tempdir()).apply {
            write(
                "build.gradle.kts",
                """
                import kim.jade.kotlinx.protobuf.gradle.proto

                plugins {
                    kotlin("jvm") version "2.4.10"
                    id("kim.jade.kotlinx-protobuf") version "$pluginVersion"
                }
                kotlin { jvmToolchain(17) }
                dependencies {
                    implementation("kim.jade:kotlinx-protobuf-core:$pluginVersion")
                    implementation("com.google.api.grpc:proto-google-common-protos:2.63.1")
                }
                kotlinxProtobuf {
                    generatorVersion = "$pluginVersion"
                }
                kotlin.sourceSets.named("main") {
                    proto { kotlin() }
                }
                """.trimIndent(),
            )
            write(
                "src/main/proto/demo/v1/greeter.proto",
                """
                syntax = "proto3";
                package demo.v1;
                import "google/api/annotations.proto";

                message HelloRequest { string name = 1; }
                message HelloReply { string message = 1; }

                service Greeter {
                  rpc SayHello(HelloRequest) returns (HelloReply) {
                    option (google.api.http) = { get: "/v1/hello" };
                  }
                }
                """.trimIndent(),
            )
        }

        project.build("generateProto")

        project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/v1/greeter.kt").shouldExist()
        // The imported proto is only ever an include; it must not be generated.
        project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/google/api").shouldNotExist()
    }

    "imports protos from a commonMain dependency in the jvmMain run too" {
        // converterMultiplatform() puts its JVM half on jvmMain over commonMain's protos, so jvmMain
        // runs protoc as well — and it has to resolve the same imports. A Kotlin source set's own
        // configurations do not reach across the dependsOn edge, so mining only jvmMain's would leave
        // google/api/annotations.proto missing on the very protos commonMain compiled happily.
        val project = TestProject(tempdir()).apply {
            write(
                "build.gradle.kts",
                """
                import kim.jade.kotlinx.protobuf.gradle.proto

                plugins {
                    kotlin("multiplatform") version "2.4.10"
                    id("kim.jade.kotlinx-protobuf") version "$pluginVersion"
                }
                kotlinxProtobuf {
                    generatorVersion = "$pluginVersion"
                }
                kotlin {
                    jvmToolchain(17)
                    jvm()

                    sourceSets {
                        commonMain {
                            proto {
                                kotlin()
                                converterMultiplatform()
                            }
                            dependencies {
                                implementation("kim.jade:kotlinx-protobuf-core:$pluginVersion")
                                implementation("kim.jade:kotlinx-protobuf-wkt:$pluginVersion")
                                // The proto carrying google/api/annotations.proto, declared here only.
                                implementation("com.google.api.grpc:proto-google-common-protos:2.63.1")
                            }
                        }
                        jvmMain.dependencies {
                            implementation("com.google.protobuf:protobuf-java:4.35.1")
                        }
                    }
                }
                """.trimIndent(),
            )
            write(
                "src/commonMain/proto/demo/v1/greeter.proto",
                """
                syntax = "proto3";
                package demo.v1;
                import "google/api/annotations.proto";

                message HelloRequest { string name = 1; }
                message HelloReply { string message = 1; }

                service Greeter {
                  rpc SayHello(HelloRequest) returns (HelloReply) {
                    option (google.api.http) = { get: "/v1/hello" };
                  }
                }
                """.trimIndent(),
            )
        }

        project.build("generateProto")

        project.file(
            "build/generated/sources/kotlinx-protobuf/commonMain/converterMultiplatform/demo/v1/greeter.converter.kt"
        ).shouldExist()
        project.file(
            "build/generated/sources/kotlinx-protobuf/jvmMain/converterMultiplatformJvm/demo/v1/greeter.converter.kt"
        ).shouldExist()
    }

    // The catalog versions are baked into the plugin jar as conventions when it is built. These two
    // check that a build's own value still wins — a convention read too eagerly would pin every consumer
    // to whatever this repository happened to compile against.
    "resolves protoc at the version the build asks for" {
        val project = jvmProject()
        project.write(
            "build.gradle.kts",
            project.file("build.gradle.kts").readText().replace(
                "typeUrlPrefix(\"type.googleapis.com\")",
                // includeWellKnownTypes off: protocVersion also picks the protobuf-java that carries the
                // well-known-type protos, and that resolves first — this test is about the binary.
                "typeUrlPrefix(\"type.googleapis.com\")\n    protocVersion = \"0.0.0-not-a-release\"" +
                    "\n    includeWellKnownTypes = false",
            ),
        )

        val result = project.buildAndFail("generateMainProtoDescriptorSet")

        result.output shouldContain "com.google.protobuf:protoc:0.0.0-not-a-release"
    }

    "resolves protoc-gen-grpc-java at the version the build asks for" {
        val project = jvmProject(protoBlock = "kotlin()\n        grpcJvm()")
        project.write("src/main/proto/demo/v1/greeter.proto", TestProject.SERVICE_PROTO)
        project.write(
            "build.gradle.kts",
            project.file("build.gradle.kts").readText().replace(
                "typeUrlPrefix(\"type.googleapis.com\")",
                "typeUrlPrefix(\"type.googleapis.com\")\n    grpcVersion = \"0.0.0-not-a-release\"",
            ),
        )

        val result = project.buildAndFail("generateMainProtoDescriptorSet")

        result.output shouldContain "io.grpc:protoc-gen-grpc-java:0.0.0-not-a-release"
    }

    "generates a Flow signature for each streaming shape" {
        val project = jvmProject()
        project.write("src/main/proto/demo/v1/greeter.proto", TestProject.STREAMING_SERVICE_PROTO)

        project.build("generateProto")

        val generated =
            project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/v1/greeter.kt").readText()

        // A response Flow is cold, so server-streaming and bidi are returned rather than awaited — those
        // two must not be suspend, or every caller would need a coroutine to get a Flow it never collects.
        generated shouldContain "public suspend fun sayHello(request: HelloRequest): HelloReply"
        generated shouldContain "public fun watchHello(request: HelloRequest): Flow<HelloReply>"
        generated shouldContain "public suspend fun uploadHello(requests: Flow<HelloRequest>): HelloReply"
        generated shouldContain "public fun chatHello(requests: Flow<HelloRequest>): Flow<HelloReply>"
    }

    "compiles streaming gRPC clients and servers against grpc-kotlin" {
        // The real guard: the generated interface, its overrides and the ClientCalls/ServerCalls entry
        // points have to agree on all four shapes, and only the compiler checks all three at once.
        val project = TestProject(tempdir()).apply {
            write(
                "build.gradle.kts",
                """
                import kim.jade.kotlinx.protobuf.gradle.proto

                plugins {
                    kotlin("jvm") version "2.4.10"
                    id("kim.jade.kotlinx-protobuf") version "$pluginVersion"
                }
                kotlin { jvmToolchain(17) }
                dependencies {
                    implementation("kim.jade:kotlinx-protobuf-core:$pluginVersion")
                    implementation("kim.jade:kotlinx-protobuf-wkt:$pluginVersion")
                    implementation("kim.jade:kotlinx-protobuf-grpc:$pluginVersion")
                    implementation("com.google.protobuf:protobuf-java:4.35.1")
                    // Declared by the build, not inherited: core compiles against coroutines without
                    // passing them on, so a streaming service needs this line.
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
                }
                kotlinxProtobuf {
                    generatorVersion = "$pluginVersion"
                    typeUrlPrefix("type.googleapis.com")
                }
                kotlin.sourceSets.named("main") {
                    proto {
                        kotlin()
                        converterJvm()
                        grpcJvm()
                    }
                }
                """.trimIndent(),
            )
            write("src/main/proto/demo/v1/greeter.proto", TestProject.STREAMING_SERVICE_PROTO)
        }

        val result = project.build("build")

        result.task(":compileKotlin")?.outcome shouldBe TaskOutcome.SUCCESS

        // The grpc/ file is the thin GreeterGrpc wrapper; the grpc-java delegation is in grpc/jvm/.
        val grpc = project.file(
            "build/generated/sources/kotlinx-protobuf/main/grpcJvm/demo/v1/grpc/jvm/greeter.kt"
        ).readText()

        // Each shape delegates to the matching grpc-kotlin entry point, not to the unary one.
        grpc shouldContain "serverStreamingRpc"
        grpc shouldContain "clientStreamingRpc"
        grpc shouldContain "bidiStreamingRpc"
        grpc shouldContain "serverStreamingServerMethodDefinition"
        grpc shouldContain "clientStreamingServerMethodDefinition"
        grpc shouldContain "bidiStreamingServerMethodDefinition"
    }

    "maps proto2 presence, required, defaults and groups" {
        // converterJvm() as well as kotlin(): a `group`'s field is named after the lowercased message
        // while protoc-gen-java names its accessors after the message, so the converter is the only
        // thing that finds out whether this generator agreed with protoc-gen-java about which is which.
        val project = jvmProject("kotlin()\n        converterJvm()")
        project.file("src/main/proto/demo/v1/greeter.proto").delete()
        project.write("src/main/proto/demo/v2/legacy.proto", TestProject.PROTO2_PROTO)

        project.build("build")

        val generated =
            project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/v2/legacy.kt").readText()

        // proto2 gives every singular scalar presence, so each becomes nullable — and `required` is the
        // one that cannot be absent, so it gets no default and the compiler asks for it.
        generated shouldContain "public val id: String,"
        generated shouldContain "public val retries: Int? = null"
        generated shouldContain "public val shade: Shade? = null"
        // Repeated is unchanged: an empty list is already the absent value. A message field is not — a
        // singular message carries presence in every syntax, and a group is one, so absence has to be a
        // value the type can hold rather than a default instance that claims the field was there.
        generated shouldContain "public val tags: List<String> = emptyList()"
        generated shouldContain "public val nested: Nested? = null"
        // A `[default = …]` changes no Kotlin default — absence is null, not the proto2 fallback — so
        // the value is recorded instead of being lost.
        generated shouldContain """@ProtobufOption(key = "default_value", value = "\"3\"")"""
        generated shouldContain """@ProtobufOption(key = "default_value", value = "\"DARK\"")"""
    }

    "reads an editions file's per-field presence" {
        val project = jvmProject("kotlin()\n        converterJvm()")
        project.file("src/main/proto/demo/v1/greeter.proto").delete()
        project.write("src/main/proto/demo/e/modern.proto", TestProject.EDITIONS_PROTO)

        project.build("build")

        val generated =
            project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/e/modern.kt").readText()

        // Editions replaces the syntax keyword with a feature, and 2023 defaults field_presence to
        // EXPLICIT — so the field that opts out of it is the one that is not nullable.
        generated shouldContain "public val implicit: String = \"\""
        generated shouldContain "public val explicit: String? = null"
        generated shouldContain "public val tags: List<String> = emptyList()"
    }

    "constructs a message that reaches its own type" {
        // The generated code is run rather than only compiled. A message field defaulting to its own
        // default instance compiles perfectly well and then overflows the stack the first time anything
        // constructs it, so compiling proves nothing about the case this covers.
        val project = jvmProject()
        project.file("src/main/proto/demo/v1/greeter.proto").delete()
        project.write("src/main/proto/demo/r/node.proto", TestProject.RECURSIVE_PROTO)
        project.file("build.gradle.kts").appendText(
            """

            apply(plugin = "application")
            extensions.configure<JavaApplication>("application") { mainClass.set("ProbeKt") }
            """.trimIndent(),
        )
        project.write(
            "src/main/kotlin/Probe.kt",
            """
            import demo.r.Left
            import demo.r.Node
            import demo.r.Right

            fun main() {
                check(Node().next == null)
                check(Node(name = "head", next = Node(name = "tail")).next?.name == "tail")
                check(Left().right == null)
                check(Right().left == null)
            }
            """.trimIndent(),
        )

        project.build("run")

        val generated =
            project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/r/node.kt").readText()

        generated shouldContain "public val next: Node? = null"
        generated shouldContain "public val right: Right? = null"
        generated shouldContain "public val left: Left? = null"
    }

    "rejects two enum values sharing a number unless allow_alias says so" {
        val project = jvmProject()
        project.file("src/main/proto/demo/v1/greeter.proto").delete()
        project.write(
            "src/main/proto/demo/v1/shade.proto",
            """
            syntax = "proto3";
            package demo.v1;
            enum Shade {
              SHADE_UNSPECIFIED = 0;
              DARK = 1;
              DIM = 1;
            }
            """.trimIndent(),
        )

        // protoc refuses this one itself, which is the outer guard; the generator's own check is what
        // catches a descriptor set assembled some other way.
        val result = project.buildAndFail("generateProto")

        result.output shouldContain "allow_alias"
    }

    "generates one client method per http binding" {
        val project = TestProject(tempdir()).apply {
            write(
                "build.gradle.kts",
                """
                import kim.jade.kotlinx.protobuf.gradle.proto

                plugins {
                    kotlin("jvm") version "2.4.10"
                    id("kim.jade.kotlinx-protobuf") version "$pluginVersion"
                }
                kotlin { jvmToolchain(17) }
                dependencies {
                    implementation("kim.jade:kotlinx-protobuf-core:$pluginVersion")
                    implementation("com.google.api.grpc:proto-google-common-protos:2.63.1")
                }
                kotlinxProtobuf {
                    generatorVersion = "$pluginVersion"
                }
                kotlin.sourceSets.named("main") {
                    proto {
                        kotlin()
                        grpcGateway()
                    }
                }
                """.trimIndent(),
            )
            write("src/main/proto/demo/v1/library.proto", TestProject.GATEWAY_PROTO)
        }

        project.build("generateProto")

        val generated = project
            .file("build/generated/sources/kotlinx-protobuf/main/grpcGateway/demo/v1/grpc/gateway/library.kt")
            .readText()

        // The primary binding is the interface method; the additional ones are alternative routes onto
        // the same RPC, so they sit next to it rather than replacing it.
        generated shouldContain "override suspend fun createBook(request: CreateBookRequest)"
        generated shouldContain "suspend fun createBookBinding2(request: CreateBookRequest)"
        generated shouldContain "suspend fun createBookBinding3(request: CreateBookRequest)"

        // `{shelf=shelves/**}` is the one form allowed to span segments, so its value keeps its slashes
        // — and the `:create` suffix is a verb, not part of the variable.
        generated shouldContain "\${pathParameter0.encodeURLPath()}/books:create"
        // The second binding binds the same field as a single segment, which does not.
        generated shouldContain "\${pathParameter0.encodeURLPathPart()}"

        // body: "book" sends that field, not the whole request; body: "*" sends the request. The named
        // field is a singular message and so nullable, which is the difference between sending an empty
        // body and sending none at all.
        generated shouldContain "request.book?.let { setBody(it) }"
        generated shouldContain "setBody(request)"
        // …and everything the path and the body did not take is a query parameter.
        generated shouldContain "parameter(\"filter\""

        // response_body: "book" means the reply body *is* that field, so the message is put back around it.
        generated shouldContain ".body<Book>()"
        generated shouldContain "return CreateBookResponse(book = responseBody)"
    }

    "records the options it does not act on" {
        val project = jvmProject()
        project.write(
            "src/main/proto/demo/v1/greeter.proto",
            """
            syntax = "proto3";
            package demo.v1;
            option java_package = "com.example.generated";

            message HelloRequest {
              string user_name = 1 [json_name = "who", deprecated = true];
            }
            """.trimIndent(),
        )

        project.build("build")

        val generated =
            project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/v1/greeter.kt").readText()

        // java_package changes nothing here — the Kotlin package is the proto one — so the file says so
        // rather than leaving a reader to find out by not finding the class.
        generated shouldContain "package demo.v1"
        generated shouldContain """@file:ProtobufOption(key = "java_package", value = "\"com.example.generated\"")"""
        // One annotation per option, so a declaration setting several reads as a list rather than as a
        // string that has to be parsed back apart.
        generated shouldContain """@ProtobufOption(key = "deprecated", value = "true")"""
        // An overridden json_name is carried whichever generator produced the file, on the field's entry.
        generated shouldContain "jsonName = \"who\""
    }

    "records the descriptor the types were generated from" {
        // The options test above covers what a schema declared; this covers what it *was*. A generator
        // handed only the Kotlin — one built on KSP, or one reading a module whose .proto files were never
        // published — has to get the schema back out of the code, and the mapping loses most of it: `Int`
        // is three proto types at once, `scores` no longer says which, `nickname` does not say it was
        // written `optional`, and an interface says nothing about the `pkg.Service/Method` it is called by.
        val project = jvmProject()
        project.write(
            "src/main/proto/demo/v1/greeter.proto",
            """
            syntax = "proto3";
            package demo.v1;
            import "google/protobuf/timestamp.proto";

            message HelloRequest {
              optional string nickname = 1;
              map<string, sint32> scores = 2;
              google.protobuf.Timestamp asked_at = 3;
              oneof result {
                string ok = 4;
                Shade shade = 5;
              }
            }

            enum Shade {
              LIGHT = 0;
              DARK = 1;
            }

            service Greeter {
              rpc Hello(HelloRequest) returns (HelloRequest);
              rpc Watch(HelloRequest) returns (stream HelloRequest);
            }
            """.trimIndent(),
        )

        // generateProto, not build: a streaming RPC needs coroutines on the compile classpath, and that
        // the annotations compile is what every other test in here already builds.
        project.build("generateProto")

        val generated =
            project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/v1/greeter.kt").readText()

        // The file it came from, and the two facts a Kotlin file cannot hold: the syntax it was written in
        // and what it imported.
        generated shouldContain "@file:ProtobufFile(path = \"demo/v1/greeter.proto\", protoPackage = \"demo.v1\", " +
            "syntax = \"proto3\", dependencies = [\"google/protobuf/timestamp.proto\"])"
        // The proto name as a literal, next to the type URL that is emitted as a reference to a const.
        generated shouldContain "name = \"demo.v1.HelloRequest\""
        generated shouldContain "@ProtobufEnum(typeUrl = Shade.TYPE_URL, name = \"demo.v1.Shade\")"
        generated shouldContain "@ProtobufEnumValue(name = \"DARK\", number = 1)"
        // `optional` in proto3 is a synthetic one-of, which is presence rather than a one-of the schema
        // wrote — so it is recorded as that and not under a name nothing declared.
        generated shouldContain "@ProtobufField(name = \"nickname\", number = 1, jsonName = \"nickname\", " +
            "type = ProtobufType.STRING, label = ProtobufLabel.OPTIONAL, proto3Optional = true)"
        // A map field is a repeated field over an entry message this generator does not emit, so the entry
        // is recorded too: without it `Map<String, Int>` never says the values were sint32.
        generated shouldContain "@ProtobufMapEntry(typeName = \"demo.v1.HelloRequest.ScoresEntry\", " +
            "keyType = ProtobufType.STRING, valueType = ProtobufType.SINT32)"
        generated shouldContain "typeName = \"google.protobuf.Timestamp\""
        // Both halves of a one-of: the interface carries the name, each branch the field it stands for.
        generated shouldContain "@ProtobufOneOf(name = \"result\")"
        generated shouldContain "@ProtobufField(name = \"ok\", number = 4, jsonName = \"ok\", " +
            "type = ProtobufType.STRING, label = ProtobufLabel.OPTIONAL, oneOf = \"result\")"
        // A service is addressed by proto names, and `Flow` is the only trace streaming otherwise leaves.
        // The annotation shares its name with the interface the service implements, so it arrives aliased.
        generated shouldContain "@AnnotationProtobufService(name = \"demo.v1.Greeter\")"
        generated shouldContain "@ProtobufMethod(name = \"Hello\", inputType = \"demo.v1.HelloRequest\", " +
            "outputType = \"demo.v1.HelloRequest\")"
        generated shouldContain "@ProtobufMethod(name = \"Watch\", inputType = \"demo.v1.HelloRequest\", " +
            "outputType = \"demo.v1.HelloRequest\", serverStreaming = true)"
        // Messages, enums and services each carry their own descriptor bytes as well — the annotations are
        // this generator's reading of the schema, the bytes are the schema.
        generated.split("public val descriptorBytes: ByteArray").size - 1 shouldBe 3
    }

    "hands each declaration its own descriptor back as bytes" {
        // Parsed by protobuf-java in the test project, not string-matched: the point of the bytes is that
        // they are a DescriptorProto, and only something that parses them says whether they are.
        val project = jvmProject()
        project.write("src/main/proto/demo/v1/greeter.proto", TestProject.SERVICE_PROTO)
        project.file("build.gradle.kts").appendText(
            """

            apply(plugin = "application")
            extensions.configure<JavaApplication>("application") { mainClass.set("ProbeKt") }
            """.trimIndent(),
        )
        project.write(
            "src/main/kotlin/Probe.kt",
            """
            import com.google.protobuf.DescriptorProtos.DescriptorProto
            import com.google.protobuf.DescriptorProtos.ServiceDescriptorProto
            import demo.v1.Greeter
            import demo.v1.HelloRequest

            fun main() {
                val message = DescriptorProto.parseFrom(HelloRequest.descriptorBytes)
                check(message.name == "HelloRequest") { message.name }
                check(message.fieldList.map { it.name } == listOf("name", "retries", "tags")) { message }
                // The one-of the fields belong to is in there too, which is what makes these bytes worth
                // carrying: the annotations name it, this reproduces it.
                check(message.getField(1).proto3Optional) { message }

                val service = ServiceDescriptorProto.parseFrom(Greeter.descriptorBytes)
                check(service.name == "Greeter") { service.name }
                check(service.methodList.single().inputType == ".demo.v1.HelloRequest") { service }
            }
            """.trimIndent(),
        )

        project.build("run")

        val generated =
            project.file("build/generated/sources/kotlinx-protobuf/main/kotlin/demo/v1/greeter.kt").readText()

        // On the companion, next to the type URL — and decoded per call rather than held, so a descriptor
        // nothing reads costs nothing at class initialization.
        generated shouldContain "public val descriptorBytes: ByteArray"
        generated shouldContain "get() = Base64.decode(DESCRIPTOR_BASE64)"
        generated shouldContain "private const val DESCRIPTOR_BASE64: String"
    }

    "rejects two generators that both emit the message types" {
        val project = jvmProject("kotlin()\n        kotlinxSerialization()")

        val result = project.buildAndFail("generateProto")

        result.output shouldContain "both emit the message and enum types"
    }

    "rejects kotlinxSerialization without a converter to delegate to" {
        val project = jvmProject("kotlinxSerialization()")

        val result = project.buildAndFail("generateProto")

        result.output shouldContain "needs a converter generator alongside it"
    }

    "surfaces protoc's own error when an import does not resolve" {
        val project = jvmProject()
        project.write(
            "src/main/proto/demo/v1/broken.proto",
            """
            syntax = "proto3";
            package demo.v1;
            import "nope/missing.proto";
            """.trimIndent(),
        )

        val result = project.buildAndFail("generateProto")

        result.output shouldContain "missing.proto"
    }
})
