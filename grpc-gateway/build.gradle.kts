kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                val kotlinxSerializationVersion: String by project
                val ktorVersion: String by project

                api(project(fullPath(":core")))

                implementation(project(fullPath(":kotlinx")))

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")

                api("io.ktor:ktor-client-core:$ktorVersion")
            }
        }
    }
}