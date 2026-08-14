plugins {
    id("convention.kotlin-multiplatform")
    id("convention.publish")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            compileOnly(kt.kotlinx.coroutine)
        }
        commonTest.dependencies {
            api(kt.kotlinx.coroutine)
        }
        jvmMain.dependencies {
            implementation(libs.protobuf.java)
        }
    }
}