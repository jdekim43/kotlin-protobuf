rootProject.name = "build-logic"

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        google()
    }

    // The same catalogs the builds themselves use, so a version is declared once for the whole repository.
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
        create("kt") {
            from(files("../gradle/kotlin.versions.toml"))
        }
    }
}
