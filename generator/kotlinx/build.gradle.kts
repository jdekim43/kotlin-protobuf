application {
    mainClass.set("kr.jadekim.protobuf.generator.kotlinx.KotlinxGeneratorKt")
}

dependencies {
    val kotlinxSerializationVersion: String by project

    implementation(project(fullPath(":kotlinx")))
    implementation(project(fullPath(":generator:converter")))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
}