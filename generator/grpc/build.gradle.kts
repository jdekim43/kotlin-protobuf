plugins {
    id("convention.protobuf-generator")
    id("convention.publish")
}

dependencies {
    api(project(":kotlinx-protobuf-generator"))
    api(project(":kotlinx-protobuf-grpc"))
}
