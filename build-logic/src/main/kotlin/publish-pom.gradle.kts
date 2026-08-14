package convention

plugins {
    id("maven-publish")
}

configure<PublishingExtension> {
    repositories {
        maven {
            setUrl(layout.buildDirectory.dir("staging-deploy"))
        }
    }

    publications.withType<MavenPublication> {
        pom {
            name.set(project.name)
            description.set("Kotlin Protobuf Compiler")
            url.set("https://github.com/jdekim43/kotlinx-protobuf")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("jdekim43")
                    name.set("Jade Kim")
                }
            }
            scm {
                connection.set("scm:git:https://github.com/jdekim43/kotlinx-protobuf.git")
                developerConnection.set("scm:git:ssh://git@github.com/jdekim43/kotlinx-protobuf.git")
                url.set("https://github.com/jdekim43/kotlinx-protobuf")
            }
        }
    }
}
