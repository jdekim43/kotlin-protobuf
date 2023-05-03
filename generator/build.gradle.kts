plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

allprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    kotlin {
        jvmToolchain(8)
    }

    dependencies {
        implementation(project(":"))

//        implementation(kotlin("reflect"))
    }
}

subprojects {
    apply {
        plugin("org.gradle.application")
        plugin("com.github.johnrengelman.shadow")
    }

    dependencies {
        implementation(project(":generator"))
    }
}

dependencies {
    val protobufVersion: String by project
    val kotlinPoetVersion: String by project
    val kasechangeVersion: String by project
    val grpcVersion: String by project
    val grpcKotlinVersion: String by project

    api("com.google.protobuf:protobuf-java:$protobufVersion")
    api("com.squareup:kotlinpoet:$kotlinPoetVersion")

    api("net.pearx.kasechange:kasechange:$kasechangeVersion")

//    implementation("io.grpc:grpc-stub:$grpcVersion")
//    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
}

tasks.clean {
    subprojects.forEach { finalizedBy(it.tasks.clean) }
}
