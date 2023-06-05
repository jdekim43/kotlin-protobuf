application {
    mainClass.set("kr.jadekim.protobuf.generator.converter.multiplatform.jvm.MultiplatformJvmConverterGeneratorKt")
}

dependencies {
    api(project(":kotlin-protobuf-generator:kotlin-protobuf-generator-converter:kotlin-protobuf-generator-converter-jvm"))
}
