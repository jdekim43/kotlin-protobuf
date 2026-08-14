package kim.jade.kotlinx.protobuf.gradle

import java.io.File
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner

/** A throwaway Gradle project the TestKit tests build. */
class TestProject(val root: File) {

    val pluginVersion: String = System.getProperty("kotlinxProtobufVersion")
        ?: error("The functionalTest task must pass -DkotlinxProtobufVersion")

    init {
        // mavenLocal first: the plugin, the generators and the runtime modules are published there by the
        // publishToMavenLocal this task depends on.
        write(
            "settings.gradle.kts",
            """
            pluginManagement {
                repositories {
                    mavenLocal()
                    mavenCentral()
                    gradlePluginPortal()
                }
            }
            dependencyResolutionManagement {
                repositories {
                    mavenLocal()
                    mavenCentral()
                }
            }
            rootProject.name = "test-project"
            """.trimIndent(),
        )
        write("gradle.properties", "org.gradle.jvmargs=-Xmx2g")
    }

    fun write(path: String, content: String): File = root.resolve(path).apply {
        parentFile.mkdirs()
        writeText(content.trimIndent() + "\n")
    }

    fun file(path: String): File = root.resolve(path)

    fun build(vararg arguments: String): BuildResult = runner(*arguments).build()

    fun buildAndFail(vararg arguments: String): BuildResult = runner(*arguments).buildAndFail()

    // No withPluginClasspath(): the plugin is resolved from mavenLocal like any other consumer would
    // resolve it. The injected classpath cannot see the Kotlin Gradle plugin, which this plugin compiles
    // against, so decorating KotlinxProtobufPlugin fails there with NoClassDefFoundError.
    private fun runner(vararg arguments: String): GradleRunner = GradleRunner.create()
        .withProjectDir(root)
        .withArguments(*arguments, "--stacktrace")
        .forwardOutput()

    companion object {

        /** A proto with a one-of, a repeated field and a proto3 `optional`, to exercise the tricky bits. */
        val GREETER_PROTO = """
            syntax = "proto3";

            package demo.v1;

            message HelloRequest {
              string name = 1;
              optional int32 retries = 2;
              repeated string tags = 3;
            }

            message HelloReply {
              oneof result {
                string ok = 1;
                string error = 2;
              }
            }
        """.trimIndent()

        /** The same, plus a service, for the gRPC generators. */
        val SERVICE_PROTO = GREETER_PROTO + """

            service Greeter {
              rpc SayHello(HelloRequest) returns (HelloReply);
            }
        """.trimIndent()

        /** A service covering all four streaming shapes, which each generate a different signature. */
        val STREAMING_SERVICE_PROTO = GREETER_PROTO + """

            service Greeter {
              rpc SayHello(HelloRequest) returns (HelloReply);
              rpc WatchHello(HelloRequest) returns (stream HelloReply);
              rpc UploadHello(stream HelloRequest) returns (HelloReply);
              rpc ChatHello(stream HelloRequest) returns (stream HelloReply);
            }
        """.trimIndent()

        /**
         * proto2, where every singular field has presence and some of it is spelled differently.
         *
         * `required` and `group` exist nowhere else, `[default = …]` is the only way a proto carries a
         * value the zero value is not, and a proto2 enum is free to start somewhere other than zero.
         */
        val PROTO2_PROTO = """
            syntax = "proto2";

            package demo.v2;

            enum Shade {
              option allow_alias = true;
              LIGHT = 1;
              DARK = 2;
              DIM = 2;
            }

            message Legacy {
              required string id = 1;
              optional int32 retries = 2 [default = 3];
              optional Shade shade = 3 [default = DARK];
              repeated string tags = 4;

              optional group Nested = 5 {
                optional string note = 1;
              }
            }
        """.trimIndent()

        /**
         * Messages that reach their own type through a singular message field, directly and through a
         * second message.
         *
         * There is no default instance to fall back on here — one would have to contain another, and that
         * one another — so this is the shape that shows presence is the thing breaking the cycle rather
         * than a convenience.
         */
        val RECURSIVE_PROTO = """
            syntax = "proto3";

            package demo.r;

            message Node {
              string name = 1;
              Node next = 2;
            }

            message Left {
              Right right = 1;
            }

            message Right {
              Left left = 1;
            }
        """.trimIndent()

        /** An editions file, where presence is a feature rather than a keyword. */
        val EDITIONS_PROTO = """
            edition = "2023";

            package demo.e;

            message Modern {
              string implicit = 1 [features.field_presence = IMPLICIT];
              string explicit = 2;
              repeated string tags = 3;
            }
        """.trimIndent()

        /**
         * The `google.api.http` shapes cosmos-sdk never uses, and so the integration tests never reach:
         * a second binding onto one RPC, a body naming a field rather than the whole message, a
         * `response_body` naming one field of the reply, a `**` segment, and a `:verb` suffix.
         */
        val GATEWAY_PROTO = """
            syntax = "proto3";

            package demo.v1;

            import "google/api/annotations.proto";

            message Book {
              string title = 1;
            }

            message CreateBookRequest {
              string shelf = 1;
              string filter = 2;
              Book book = 3;
            }

            message CreateBookResponse {
              Book book = 1;
            }

            service Library {
              rpc CreateBook(CreateBookRequest) returns (CreateBookResponse) {
                option (google.api.http) = {
                  post: "/v1/{shelf=shelves/**}/books:create"
                  body: "book"
                  response_body: "book"
                  additional_bindings { get: "/v1/books/{shelf}" }
                  additional_bindings { put: "/v1/books" body: "*" }
                };
              }
            }
        """.trimIndent()
    }
}
