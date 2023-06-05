application {
    mainClass.set("kr.jadekim.protobuf.generator.converter.jvm.JvmConverterGeneratorKt")
}

dependencies {
    val grpcVersion: String by project
    val grpcKotlinVersion: String by project

    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
}
