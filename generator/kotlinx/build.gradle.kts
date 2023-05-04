application {
    mainClass.set("kr.jadekim.protobuf.generator.kotlinx.KotlinxGeneratorKt")
}

dependencies {
    val kotlinxSerializationVersion: String by project

    implementation(project(":kotlin-protobuf-kotlinx"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
}