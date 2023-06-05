application {
    mainClass.set("kr.jadekim.protobuf.generator.grpc.GrpcGeneratorKt")
}

dependencies {
    implementation(project(":kotlin-protobuf-generator:kotlin-protobuf-generator-converter:kotlin-protobuf-generator-converter-jvm"))
}