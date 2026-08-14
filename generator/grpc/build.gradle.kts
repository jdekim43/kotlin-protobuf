plugins {
    id("convention.protobuf-generator")
}

dependencies {
    api(project(":kotlinx-protobuf-generator"))
    api(project(":kotlinx-protobuf-grpc"))
}
