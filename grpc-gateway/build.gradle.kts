kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                val ktorVersion: String by project

                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
            }
        }
    }
}