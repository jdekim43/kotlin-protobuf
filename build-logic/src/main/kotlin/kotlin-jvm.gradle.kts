package convention

plugins {
    kotlin("jvm")
    id("maven-publish")
}

kotlin {
    jvmToolchain(8)
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
dependencies {
    testImplementation(libs.findLibrary("kotest-framework-engine").get())
    testImplementation(libs.findLibrary("kotest-assertions-core").get())
    testRuntimeOnly(libs.findLibrary("kotest-runner-junit5").get())
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
            artifact(sourcesJar)
        }
    }
}
