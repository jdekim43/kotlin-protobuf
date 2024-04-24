plugins {
//    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.2.1"
}

gradlePlugin {
    website.set("https://github.com/jdekim43/kotlin-protobuf")
    vcsUrl.set("https://github.com/jdekim43/kotlin-protobuf.git")

    plugins {
        create("kotlin-protobuf") {
            id = "kr.jadekim.kotlin-protobuf"
            displayName = "Kotlin Protobuf"
            description = "Kotlin Protobuf gradle plugin"
            tags.set(listOf("kotlin", "protobuf", "grpc", "kotlinx"))
            implementationClass = "kr.jadekim.protobuf.gradle.GradlePlugin"
        }
    }
}

dependencies {
    implementation(gradleApi())
}

//todo: gradle-plugin for generator
//todo: prebuilt types for https://github.com/protocolbuffers/protobuf/blob/main/java/lite/generate-sources-build.xml
//todo: implement prebuilt types (Any) util functions
//todo: TransactionFactory
