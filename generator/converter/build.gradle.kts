subprojects {
    dependencies {
        api(project(fullPath(":generator:converter")))
    }
}

dependencies {
    implementation(project(fullPath(":prebuilt")))
}
