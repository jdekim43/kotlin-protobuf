import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm")
    id("com.google.protobuf") version "0.9.2"
}

dependencies {
    val protobufVersion: String by project
    val kotlinxSerializationVersion: String by project

    implementation(project(":"))

    implementation("com.google.protobuf:protobuf-java:$protobufVersion")

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
    }

    generateProtoTasks {
        all().forEach {
            it.dependsOn(":generator:clean")
            it.dependsOn(":generator:kotlinx:shadowJar")

            it.plugins {
                id("kotlin-protobuf-kotlinx")
            }
        }
    }
}
