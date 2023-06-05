application {
    mainClass.set("kr.jadekim.protobuf.generator.converter.multiplatform.jvm.MultiplatformJvmConverterGeneratorKt")
}

dependencies {
    api(project(fullPath(":generator:converter:jvm")))
}
