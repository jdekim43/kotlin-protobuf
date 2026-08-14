rootProject.name = "kotlinx-protobuf-gradle-plugin"

pluginManagement {
    includeBuild("../build-logic")

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
        create("kt") {
            from(files("../gradle/kotlin.versions.toml"))
        }
    }
}
