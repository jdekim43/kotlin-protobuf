plugins {
    id("convention.kotlin-multiplatform")
    id("convention.publish")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":kotlinx-protobuf-core"))
            api(kt.kotlinx.serialization)
        }
        jvmMain.dependencies {
            implementation(libs.protobuf.java.util)
        }
    }
}