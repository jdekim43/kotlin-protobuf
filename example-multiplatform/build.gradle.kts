import com.google.protobuf.gradle.id

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.8.20"
    id("com.google.protobuf") version "0.9.3"
    `java-library`
}

kotlin {
    jvm {
        withJava()
        jvmToolchain(8)
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(File(buildDir, "generated/source/proto/main/kotlin-protobuf-kotlinx"))
            kotlin.srcDir(File(buildDir, "generated/source/proto/main/kotlin-protobuf-converter-multiplatform"))
            kotlin.srcDir(File(buildDir, "generated/source/proto/main/kotlin-protobuf-grpc-multiplatform"))

            dependencies {
                val kotlinxSerializationVersion: String by project

                implementation(project(fullPath(":")))
                implementation(project(fullPath(":kotlinx")))
                implementation(project(fullPath(":grpc")))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
            }
        }

        val jvmMain by getting {
            kotlin.srcDir(File(buildDir, "generated/source/proto/main/java"))
            kotlin.srcDir(File(buildDir, "generated/source/proto/main/grpc"))
            kotlin.srcDir(File(buildDir, "generated/source/proto/main/kotlin-protobuf-converter-multiplatform-jvm"))
            kotlin.srcDir(File(buildDir, "generated/source/proto/main/kotlin-protobuf-grpc-multiplatform-jvm"))

            dependencies {
                val protobufVersion: String by project
                val grpcVersion: String by project
                val grpcKotlinVersion: String by project

                implementation("com.google.protobuf:protobuf-java:$protobufVersion")
                implementation("io.grpc:grpc-protobuf:$grpcVersion")
                implementation("io.grpc:grpc-stub:$grpcVersion")
                implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
            }
        }
    }
}

dependencies {
    val protobufVersion: String by project
    val grpcVersion: String by project

    implementation("com.google.protobuf:protobuf-java:$protobufVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
}

sourceSets {
    main {
        proto {
            srcDir(project.projectDir.absolutePath + "/src/commonMain/proto")
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
        id("kotlin-protobuf-converter-multiplatform") {
            val targetProject = project(fullPath(":generator:converter:multiplatform"))
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
        }
        id("kotlin-protobuf-converter-multiplatform-jvm") {
            val targetProject = project(fullPath(":generator:converter:multiplatform:jvm"))
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
        }

        id("grpc") {
            val grpcVersion: String by project
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("kotlin-protobuf-grpc-multiplatform") {
            val targetProject = project(fullPath(":generator:grpc:multiplatform"))
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
        }
        id("kotlin-protobuf-grpc-multiplatform-jvm") {
            val targetProject = project(fullPath(":generator:grpc:multiplatform:jvm"))
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.dependsOn(fullPath(":generator") + ":clean")
            it.dependsOn(fullPath(":generator:kotlinx") + ":shadowJar")
            it.dependsOn(fullPath(":generator:converter:multiplatform") + ":shadowJar")
            it.dependsOn(fullPath(":generator:converter:multiplatform:jvm") + ":shadowJar")
            it.dependsOn(fullPath(":generator:grpc:multiplatform") + ":shadowJar")
            it.dependsOn(fullPath(":generator:grpc:multiplatform:jvm") + ":shadowJar")

            it.plugins {
                id("kotlin-protobuf-kotlinx")
                id("kotlin-protobuf-converter-multiplatform")
                id("kotlin-protobuf-converter-multiplatform-jvm")

                id("grpc")
                id("kotlin-protobuf-grpc-multiplatform")
                id("kotlin-protobuf-grpc-multiplatform-jvm")
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
