package convention

plugins {
    kotlin("jvm")
    id("application")
    id("com.gradleup.shadow")
    id("maven-publish")
}

tasks.getByName<Jar>("jar") {
    archiveClassifier.set("thin")
}

tasks.getByName<Jar>("shadowJar") {
    archiveClassifier.set("")
}

kotlin {
    jvmToolchain(17)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

publishing {
    publications {
        create<MavenPublication>("artifacts") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            artifact(tasks.getByName("shadowJar"))
            artifact(sourcesJar)
        }
    }
}