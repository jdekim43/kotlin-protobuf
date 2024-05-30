kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                val grpcVersion: String by project
                val grpcKotlinVersion: String by project

                api(project(fullPath(":core")))

                implementation("io.grpc:grpc-protobuf:$grpcVersion")
                implementation("io.grpc:grpc-stub:$grpcVersion")
                implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
            }
        }
    }
}