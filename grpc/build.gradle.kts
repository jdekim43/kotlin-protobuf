kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                val grpcVersion: String by project
                val grpcKotlinVersion: String by project

                implementation("io.grpc:grpc-protobuf:$grpcVersion")
                implementation("io.grpc:grpc-stub:$grpcVersion")
                implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
            }
        }
    }
}