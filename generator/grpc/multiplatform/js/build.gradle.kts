plugins {
    id("convention.protobuf-generator")
    id("convention.publish")
}

application {
    mainClass.set("kim.jade.kotlinx.protobuf.generator.grpc.multiplatform.js.MultiplatformJsGrpcGeneratorKt")
}

dependencies {
    api(project(":kotlinx-protobuf-generator-grpc"))
    api(project(":kotlinx-protobuf-generator-converter"))
}
