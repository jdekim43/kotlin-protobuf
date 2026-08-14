plugins {
    id("convention.protobuf-generator")
    id("convention.publish")
    alias(libs.plugins.protobuf)
}

application {
    mainClass.set("kim.jade.kotlinx.protobuf.generator.grpc.gateway.GrpcGatewayGeneratorKt")
}

dependencies {
    api(project(":kotlinx-protobuf-generator"))
    api(project(":kotlinx-protobuf-grpc-gateway"))

    implementation(libs.grpc.protobuf)
    implementation(kt.ktor.client.core)
}

sourceSets {
    main {
        proto {
            srcDir(File(projectDir, "src/proto"))
        }
    }
}

protobuf {
    protoc {
        val protoc = extensions.getByType<VersionCatalogsExtension>().named("libs")
            .findLibrary("protobuf-compiler").get().get()

        artifact = "${protoc.group}:${protoc.name}:${protoc.version}"

//        val protobufVersion: String = extensions.getByType<VersionCatalogsExtension>().named("libs").findVersion("protobuf").get().preferredVersion
//        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
}

tasks.getByName("compileJava") {
    dependsOn(tasks.getByName("generateProto"))
}