import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm")
    id("com.google.protobuf") version "0.9.2"
}

dependencies {
    val protobufVersion: String by project
    val grpcVersion: String by project
    val grpcKotlinVersion: String by project
    val kotlinxSerializationVersion: String by project

    implementation(project(":"))

    implementation("com.google.protobuf:protobuf-java:$protobufVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
}

sourceSets {
    main {
        proto {
            srcDir(project.projectDir.absolutePath + "/src/main/proto")
        }
    }
}

protobuf {
    protoc {
        val protobufVersion: String by project

        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }

    plugins {
        id("kotlin-protobuf-kotlinx") {
            val targetProject = project(":generator:kotlinx")
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-all.jar"
        }

        id("grpc") {
            val grpcVersion: String by project
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpc-kotlin") {
            val grpcKotlinVersion: String by project
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
        }
        id("kotlin-protobuf-grpc") {
            val targetProject = project(":generator:grpc")
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-all.jar"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.dependsOn(":generator:clean")
            it.dependsOn(":generator:grpc:shadowJar")
            it.dependsOn(":generator:kotlinx:shadowJar")

            it.plugins {
                id("kotlin-protobuf-kotlinx")

                id("grpc")
                id("grpc-kotlin")
                id("kotlin-protobuf-grpc")
            }
        }
    }
}
