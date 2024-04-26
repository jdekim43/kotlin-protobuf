plugins {
    kotlin("multiplatform") version "1.9.23"
    id("org.jetbrains.dokka") version "1.9.20"
    id("maven-publish")
    id("signing")
}

allprojects {
    group = "kr.jadekim"
    version = "0.4.0"

    repositories {
        mavenCentral()
    }

//    tasks.test {
//        useJUnitPlatform()
//    }
}

configure(allprojects.filterNot {
    it.name.startsWith("kotlin-protobuf-generator")
            || it.name.startsWith("kotlin-protobuf-example")
            || it.name.startsWith("kotlin-protobuf-prebuilt")
}) {
    apply {
        plugin("kotlin-multiplatform")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlin {
        jvm {
            jvmToolchain(8)
        }

        sourceSets {
            val commonMain by getting {
                dependencies {

                }
            }

            val jvmMain by getting {
                dependencies {
                    val protobufVersion: String by project

                    implementation("com.google.protobuf:protobuf-java:$protobufVersion")
                }
            }
        }
    }
}

configure(allprojects.filterNot { it.name.startsWith("kotlin-protobuf-example") }) {
    apply {
        plugin("org.jetbrains.dokka")
        plugin("maven-publish")
        plugin("signing")
    }

    val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)
    val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
        dependsOn(dokkaHtml)
        archiveClassifier.set("javadoc")
        from(dokkaHtml.outputDirectory)
    }

    publishing {
        publications.withType<MavenPublication> {
            artifact(javadocJar)
            pom {
                name.set(project.name)
                description.set("Kotlin Protobuf Compiler")
                url.set("https://github.com/jdekim43/kotlin-protobuf")
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
                    connection.set("scm:git:git://github.com/jdekim43/kotlin-protobuf.git")
                    developerConnection.set("scm:git:git://github.com/jdekim43/kotlin-protobuf.git")
                    url.set("https://github.com/jdekim43/kotlin-protobuf")
                }
            }
        }

        repositories {
            val ossrhUsername: String by project
            val ossrhPassword: String by project

            if (version.toString().endsWith("-SNAPSHOT", true)) {
                maven {
                    name = "mavenCentralSnapshot"
                    setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                    credentials {
                        username = ossrhUsername
                        password = ossrhPassword
                    }
                }
            } else {
                maven {
                    name = "mavenCentral"
                    setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = ossrhUsername
                        password = ossrhPassword
                    }
                }
            }
        }
    }

    signing {
        sign(publishing.publications)
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(fullPath(":core")))
                api(project(fullPath(":prebuilt")))
            }
        }
    }
}
