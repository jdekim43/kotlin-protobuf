plugins {
    id("convention.protobuf-generator")
    id("convention.publish")
}

application {
    mainClass.set("kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.MultiplatformGrpcGeneratorKt")
}

dependencies {
    api(project(":kotlinx-protobuf-generator-grpc"))
}
