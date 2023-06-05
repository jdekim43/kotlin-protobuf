application {
    mainClass.set("kr.jadekim.protobuf.generator.grpc.GrpcGeneratorKt")
}

dependencies {
    implementation(project(fullPath(":generator:converter:jvm")))
}