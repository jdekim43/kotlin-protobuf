plugins {
    id("convention.protobuf-generator")
    id("convention.publish")
}

application {
    mainClass.set("kim.jade.kotlinx.protobuf.generator.serialization.KotlinxSerializationGeneratorKt")
}

dependencies {
    api(project(":kotlinx-protobuf-generator"))
    api(project(":kotlinx-protobuf-serialization"))
    api(project(":kotlinx-protobuf-generator-converter"))
    api(kt.kotlinx.serialization)
}