import com.google.protobuf.gradle.id

plugins {
    kotlin("plugin.serialization") version "1.9.23"
}

dependencies {
    val kotlinxSerializationVersion: String by project

    implementation(project(fullPath(":kotlinx")))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
}

protobuf {
    plugins {
        id("kotlin-protobuf-kotlinx") {
            val targetProject = project(fullPath(":generator:kotlinx"))
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.dependsOn(fullPath(":generator:kotlinx") + ":shadowJar")

            it.plugins {
                id("kotlin-protobuf-kotlinx") {
                    outputSubDir = "commonMain/kotlin"

                    option("kotlin-protobuf.prefix=type.googleapis.com")
                    option("kotlin-protobuf.include_prebuilt=true")
                }
            }
        }
    }
}
