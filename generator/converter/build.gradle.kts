plugins {
    id("convention.protobuf-generator")
}

dependencies {
    api(project(":kotlinx-protobuf-generator"))
}
