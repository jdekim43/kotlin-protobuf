application {
    mainClass.set("kr.jadekim.protobuf.generator.grpc.jvm.JvmGrpcGeneratorKt")
}

dependencies {
    val grpcVersion: String by project
    val grpcKotlinVersion: String by project

    implementation(project(fullPath(":generator:converter:jvm")))

    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
}