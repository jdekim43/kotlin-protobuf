import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
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
        implementation(project(":kotlin-protobuf-generator"))
    }

    tasks.getByName<Jar>("shadowJar") {
        archiveClassifier.set("jdk8")
    }

    publishing {
        publications {
            create<MavenPublication>("artifacts") {
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()

                artifact(tasks.getByName("shadowJar"))
            }
        }
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

    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
}

val generatedDirectory = File(buildDir, "/generated/source/main/kotlin")

sourceSets {
    main {
        java {
            srcDir(generatedDirectory)
        }
    }
}

tasks.clean {
    subprojects.forEach { finalizedBy(it.tasks.clean) }
}

val writeBuildConstants = tasks.register("writeBuildConstants") {
    doLast {
        val output = File(generatedDirectory, "/kr/jadekim/protobuf/generator/build.constants.kt")
        output.ensureParentDirsCreated()
        output.writeText("""
            package kr.jadekim.protobuf.generator
            
            const val BUILD_VERSION: String = "$version"
        """.trimIndent())
    }
}

tasks.getByName("build") {
    dependsOn(writeBuildConstants)
}
