import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("convention.kotlin-multiplatform")
    id("convention.publish")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    jvmToolchain(17)

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":kotlinx-protobuf-core"))
        }
        jsMain.dependencies {
            api(kt.kotlinx.coroutine)
            api(npm("@grpc/grpc-js", libs.versions.grpcJs.get()))
        }
        jvmMain.dependencies {
            api(libs.grpc.protobuf)
            api(libs.grpc.stub)
            api(libs.grpc.kotlin.stub)
        }
    }
}