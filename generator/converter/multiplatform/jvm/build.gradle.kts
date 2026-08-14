plugins {
    id("convention.protobuf-generator")
    id("convention.publish")
}

application {
    mainClass.set("kim.jade.kotlinx.protobuf.generator.converter.multiplatform.jvm.MultiplatformJvmConverterGeneratorKt")
}

dependencies {
    api(project(":kotlinx-protobuf-generator-converter-jvm"))
}
