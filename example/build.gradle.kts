import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.8.20"
    id("com.google.protobuf") version "0.9.3"
}

dependencies {
    val protobufVersion: String by project
    val grpcVersion: String by project
    val grpcKotlinVersion: String by project
    val kotlinxSerializationVersion: String by project

    implementation(project(":"))
    implementation(project(":kotlin-protobuf-kotlinx"))

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
            val targetProject = project(":kotlin-protobuf-generator:kotlin-protobuf-generator-kotlinx")
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
        }
        id("kotlin-protobuf-converter-jvm") {
            val targetProject = project(":kotlin-protobuf-generator:kotlin-protobuf-generator-converter:kotlin-protobuf-generator-converter-jvm")
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
        }

        id("grpc") {
            val grpcVersion: String by project
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("kotlin-protobuf-grpc") {
            val targetProject = project(":kotlin-protobuf-generator:kotlin-protobuf-generator-grpc")
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.dependsOn(":kotlin-protobuf-generator:clean")
            it.dependsOn(":kotlin-protobuf-generator:kotlin-protobuf-generator-grpc:shadowJar")
            it.dependsOn(":kotlin-protobuf-generator:kotlin-protobuf-generator-kotlinx:shadowJar")
            it.dependsOn(":kotlin-protobuf-generator:kotlin-protobuf-generator-converter:kotlin-protobuf-generator-converter-jvm:shadowJar")

            it.plugins {
                id("kotlin-protobuf-kotlinx")
                id("kotlin-protobuf-converter-jvm")

                id("grpc")
                id("kotlin-protobuf-grpc")
            }
        }
    }
}

tasks.withType<Copy> {
    filesMatching("**/*.proto") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

tasks.withType<AbstractPublishToMaven> {
    enabled = false
}
