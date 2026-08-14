plugins {
    id("convention.protobuf-generator")
    id("convention.publish")
}

application {
    mainClass.set("kim.jade.kotlinx.protobuf.generator.grpc.jvm.JvmGrpcGeneratorKt")
}

dependencies {
    api(project(":kotlinx-protobuf-generator-grpc"))
    api(project(":kotlinx-protobuf-generator-converter-jvm"))
}