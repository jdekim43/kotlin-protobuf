import kim.jade.gradle.plugin.cleanarch.plugin.module

rootProject.name = "kotlinx-protobuf"

pluginManagement {
    includeBuild("build-logic")
    includeBuild("gradle-plugin")

    repositories {
        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

plugins {
    id("kim.jade.gradle.plugin.cleanarch") version "0.1.18"
}

module("core")
module("grpc")
module("grpc-gateway", "grpc-gateway")
module("serialization")

module("generator")
module("generator-converter")
module("generator-converter-jvm")
module("generator-converter-multiplatform")
module("generator-converter-multiplatform-jvm")
module("generator-grpc")
module("generator-grpc-jvm")
module("generator-grpc-multiplatform")
module("generator-grpc-multiplatform-jvm")
module("generator-grpc-gateway", "generator/grpc-gateway")
module("generator-serialization")

module("wkt")

include(":integration-test")

include(":examples:serialization")
include(":examples:schema-evolution")
include(":examples:event-envelope")
include(":examples:grpc")
include(":examples:rest-gateway")

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()

        exclusiveContent {
            forRepository {
                ivy("https://codeload.github.com/") {
                    patternLayout { artifact("[organisation]/[module]/zip/refs/tags/[revision]") }
                    metadataSources { artifact() }
                }
            }
            filter { includeGroup("cosmos") }
        }
    }

    versionCatalogs {
        create("kotlinWrappers") {
            from("org.jetbrains.kotlin-wrappers:kotlin-wrappers-catalog:2025.11.12")
        }

        create("kt") {
            from(files("gradle/kotlin.versions.toml"))
        }

        create("kotlincrypto") {
            from("org.kotlincrypto:version-catalog:0.8.0")
        }
    }
}