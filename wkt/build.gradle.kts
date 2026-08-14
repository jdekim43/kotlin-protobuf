import kim.jade.kotlinx.protobuf.gradle.proto

plugins {
    id("convention.kotlin-multiplatform")
    id("convention.publish")
    id("kim.jade.kotlinx-protobuf")
    alias(kt.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":kotlinx-protobuf-core"))
            api(project(":kotlinx-protobuf-serialization"))
        }
        jvmMain.dependencies {
            api(libs.protobuf.java)
        }
    }
}

val serializationGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val converterGenerator: Configuration by configurations.creating { isCanBeConsumed = false }
val converterJvmGenerator: Configuration by configurations.creating { isCanBeConsumed = false }

val wellKnownTypeProtos: Configuration by configurations.creating {
    isCanBeConsumed = false
    isTransitive = false
}

dependencies {
    serializationGenerator(project(":kotlinx-protobuf-generator-serialization"))
    converterGenerator(project(":kotlinx-protobuf-generator-converter-multiplatform"))
    converterJvmGenerator(project(":kotlinx-protobuf-generator-converter-multiplatform-jvm"))

    wellKnownTypeProtos(libs.protobuf.java)
}

val extractWellKnownTypeProtos by tasks.registering(Sync::class) {
    from(zipTree(wellKnownTypeProtos.elements.map { it.single() })) {
        include("google/protobuf/*.proto")
        exclude("google/protobuf/java_features.proto")
    }
    into(layout.buildDirectory.dir("wktProtos"))
}

kotlinxProtobuf {
    typeUrlPrefix("type.googleapis.com")

    includeWellKnownTypes = false
}

kotlin {
    sourceSets {
        commonMain {
            proto {
                srcDirs.setFrom(extractWellKnownTypeProtos.map { it.destinationDir })

                kotlinxSerialization { classpath.setFrom(serializationGenerator) }
                converterMultiplatform { classpath.setFrom(converterGenerator) }
            }
        }

        jvmMain {
            proto {
                srcDirs.setFrom(extractWellKnownTypeProtos.map { it.destinationDir })
                descriptorSetFrom = "commonMain"

                converterMultiplatformJvm { classpath.setFrom(converterJvmGenerator) }

                builtin("java") { enabled = false }
            }
        }
    }
}
