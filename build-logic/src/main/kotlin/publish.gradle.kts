package convention

import org.jetbrains.dokka.gradle.tasks.DokkaGeneratePublicationTask

plugins {
    id("convention.publish-pom")
    id("org.jetbrains.dokka")
    id("org.jetbrains.dokka-javadoc")
}

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(tasks.named<DokkaGeneratePublicationTask>("dokkaGeneratePublicationHtml"))
    archiveClassifier.set("javadoc")
    from(tasks.named<DokkaGeneratePublicationTask>("dokkaGeneratePublicationHtml").flatMap { it.outputDirectory })
}

configure<PublishingExtension> {
    publications.withType<MavenPublication> {
        artifact(javadocJar)
    }
}
