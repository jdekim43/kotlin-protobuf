plugins {
    id("convention.kotlin-multiplatform")
    id("convention.publish")
}

val javaToolchains = extensions.getByType<JavaToolchainService>()

tasks.withType<Test>().configureEach {
    javaLauncher.set(javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(17)) })
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":kotlinx-protobuf-core"))
            api(project(":kotlinx-protobuf-serialization"))
            api(kt.ktor.client.core)
            implementation(kt.kotlinx.json)
        }
    }
}