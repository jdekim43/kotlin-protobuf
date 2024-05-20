import com.google.protobuf.gradle.id

plugins {
    kotlin("multiplatform")
    id("com.google.protobuf") version "0.9.4"
    `java-library`
}

allprojects {
    apply {
        plugin("kotlin-multiplatform")
        plugin("com.google.protobuf")
        plugin("org.gradle.java-library")
    }

    kotlin {
        jvm {
            withJava()
            jvmToolchain(8)
        }

        sourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(project(fullPath(":core")))
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

    dependencies {
        val protobufVersion: String by project

        implementation("com.google.protobuf:protobuf-java:$protobufVersion")
    }

    sourceSets {
        main {
            proto {
                srcDir(File(projectDir, "src/proto"))
            }
        }
    }

    protobuf {
        protoc {
            val protobufVersion: String by project

            artifact = "com.google.protobuf:protoc:$protobufVersion"
        }

        plugins {
            id("kotlin-protobuf-converter-multiplatform") {
                val targetProject = project(fullPath(":generator:converter:multiplatform"))
                path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
            }
            id("kotlin-protobuf-converter-multiplatform-jvm") {
                val targetProject = project(fullPath(":generator:converter:multiplatform:jvm"))
                path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
            }
        }

        generateProtoTasks {
            all().forEach {
                it.dependsOn(fullPath(":generator") + ":clean")
                it.dependsOn(fullPath(":generator:converter:multiplatform") + ":shadowJar")
                it.dependsOn(fullPath(":generator:converter:multiplatform:jvm") + ":shadowJar")

                it.builtins {
                    getByName("java") {
                        outputSubDir = "jvmMain/java"
                    }
                }

                it.plugins {
                    id("kotlin-protobuf-converter-multiplatform") {
                        outputSubDir = "commonMain/kotlin"

                        option("kotlin-protobuf.prefix=type.googleapis.com")
                    }
                    id("kotlin-protobuf-converter-multiplatform-jvm") {
                        outputSubDir = "jvmMain/kotlin"

                        option("kotlin-protobuf.prefix=type.googleapis.com")
                    }
                }
            }
        }
    }

    tasks.withType<Copy> {
        filesMatching("**/*.proto") {
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }

    val copyTask = tasks.register<Copy>("moveProtoResults") {
        val generatedPath = "generated/source/proto/main"
        val sourcePath = "src"

        from(File(buildDir, generatedPath))
        into(File(projectDir, sourcePath))
    }

    val cleanProtoTask = tasks.create("cleanProto") {
        group = "other"

        doLast {
            delete(File(projectDir, "src/commonMain"))
            delete(File(projectDir, "src/jvmMain"))
        }
    }

    tasks.getByName("generateProto") {
        dependsOn(cleanProtoTask)
        finalizedBy(copyTask)
    }
}

protobuf {
    plugins {
        id("kotlin-protobuf") {
            val targetProject = project(fullPath(":generator"))
            path = "${targetProject.buildDir.absolutePath}/libs/${targetProject.name}-${targetProject.version}-jdk8.jar"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.dependsOn(fullPath(":generator") + ":shadowJar")

            it.plugins {
                id("kotlin-protobuf") {
                    outputSubDir = "commonMain/kotlin"

                    option("kotlin-protobuf.prefix=type.googleapis.com")
                }
            }
        }
    }
}
