plugins {
    id("com.google.protobuf") version "0.9.4"
}

application {
    mainClass.set("kr.jadekim.protobuf.generator.grpc.gateway.GrpcGatewayGeneratorKt")
}

dependencies {
    val grpcVersion: String by project
    val ktorVersion: String by project

    implementation(project(fullPath(":grpc-gateway")))

    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
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
        val protobufVersion: String by project

        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
}

tasks.getByName("compileJava") {
    dependsOn(tasks.getByName("generateProto"))
}