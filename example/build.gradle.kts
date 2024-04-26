import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.9.23"
    id("com.google.protobuf") version "0.9.4"
}

dependencies {
    val protobufVersion: String by project
    val grpcVersion: String by project
    val grpcKotlinVersion: String by project
    val kotlinxSerializationVersion: String by project
    val ktorVersion: String by project

    implementation(project(fullPath(":"))) {
        exclude(project.group.toString(), "kotlin-protobuf-prebuilt")
    }
    implementation(project(fullPath(":prebuilt:kotlinx")))
    implementation(project(fullPath(":kotlinx")))
    implementation(project(fullPath(":grpc")))
    implementation(project(fullPath(":grpc-gateway")))

    implementation("com.google.protobuf:protobuf-java:$protobufVersion")
    implementation("com.google.protobuf:protobuf-java-util:$protobufVersion")

    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.3.9")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
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

    plugins {
        id("kotlin-protobuf-kotlinx") {
            val targetProject = project(fullPath(":generator:kotlinx"))
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
        }
        id("kotlin-protobuf-converter-jvm") {
            val targetProject = project(fullPath(":generator:converter:jvm"))
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
        }

        id("grpc") {
            val grpcVersion: String by project
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("kotlin-protobuf-grpc-jvm") {
            val targetProject = project(fullPath(":generator:grpc:jvm"))
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
        }
        id("kotlin-protobuf-grpc-gateway") {
            val targetProject = project(fullPath(":generator:grpc-gateway"))
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.dependsOn(fullPath(":generator") + ":clean")
            it.dependsOn(fullPath(":generator:grpc:jvm") + ":shadowJar")
            it.dependsOn(fullPath(":generator:grpc-gateway") + ":shadowJar")
            it.dependsOn(fullPath(":generator:kotlinx") + ":shadowJar")
            it.dependsOn(fullPath(":generator:converter:jvm") + ":shadowJar")

            it.plugins {
                id("kotlin-protobuf-kotlinx") {
                    option("kotlin-protobuf.type_registry=TypeRegistry")
                    option("kotlin-protobuf.serializers_module=SerializersModules")
                }
                id("kotlin-protobuf-converter-jvm") {
                    option("kotlin-protobuf.jvm_type_registry=JvmTypeRegistry")
                }

                id("grpc")
                id("kotlin-protobuf-grpc-jvm")
                id("kotlin-protobuf-grpc-gateway")
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
