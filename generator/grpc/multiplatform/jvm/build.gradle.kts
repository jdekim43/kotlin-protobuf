application {
    mainClass.set("kr.jadekim.protobuf.generator.grpc.multiplatform.jvm.MultiplatformJvmGrpcGeneratorKt")
}

dependencies {
    api(project(fullPath(":generator:grpc:jvm")))
}
