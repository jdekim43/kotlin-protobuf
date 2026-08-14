import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.jreleaser.model.Active
import org.jreleaser.model.Signing

plugins {
    id("convention.kotlin-multiplatform")
    id("convention.publish")
    alias(libs.plugins.jreleaser)
}

val releaseVersion = providers.gradleProperty("releaseVersion").getOrElse("0.7.0-SNAPSHOT")
val nonJvmArtifactIds = listOf(
    "js",
    "macosarm64",
    "iosarm64",
    "iosx64",
    "iossimulatorarm64",
    "watchosarm64",
    "watchossimulatorarm64",
    "tvosarm64",
    "tvossimulatorarm64",
    "linuxx64",
    "linuxarm64",
    "mingwx64",
    "android"
)

allprojects {
    group = "kim.jade"
    version = releaseVersion
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":kotlinx-protobuf-core"))
            api(project(":kotlinx-protobuf-wkt"))
        }
        jvmMain.dependencies {
            api(libs.protobuf.java)
        }
    }
}

val publishedProjects = subprojects.filter {
    it.path != ":integration-test" && !it.path.startsWith(":examples")
}

jreleaser {
    project {
        author("Jade Kim")
        license.set("Apache-2.0")
        links {
            vcsBrowser.set("https://github.com/jdekim43/kotlinx-protobuf")
        }
        inceptionYear.set("2026")
    }

    signing {
        active.set(Active.ALWAYS)
        armored.set(true)
        mode.set(Signing.Mode.FILE)
    }

    deploy {
        maven {
            val gradlePluginStagingDirectory: String =
                layout.projectDirectory.dir("gradle-plugin/build/staging-deploy").asFile.absolutePath

            mavenCentral {
                create("release") {
                    active.set(Active.RELEASE)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    skipPublicationCheck.set(true)

                    publishedProjects.forEach {
                        stagingRepository(it.layout.buildDirectory.dir("staging-deploy").get().asFile.absolutePath)

                        nonJvmArtifactIds.forEach { target ->
                            artifactOverride {
                                artifactId = "${it.name}-$target"
                                jar = false
                                verifyPom = false
                                sourceJar = false
                                javadocJar = false
                            }
                        }
                    }

                    stagingRepository(layout.buildDirectory.dir("staging-deploy").get().asFile.absolutePath)
                    stagingRepository(gradlePluginStagingDirectory)
                }
            }
            nexus2 {
                create("snapshot") {
                    active.set(Active.SNAPSHOT)
                    url.set("https://central.sonatype.com/repository/maven-snapshots")
                    snapshotUrl.set("https://central.sonatype.com/repository/maven-snapshots")
                    applyMavenCentralRules.set(true)
                    snapshotSupported.set(true)
                    closeRepository.set(true)
                    releaseRepository.set(true)

                    publishedProjects.forEach {
                        stagingRepository(it.layout.buildDirectory.dir("staging-deploy").get().asFile.absolutePath)

                        nonJvmArtifactIds.forEach { target ->
                            artifactOverride {
                                artifactId = "${it.name}-$target"
                                jar = false
                                verifyPom = false
                                sourceJar = false
                                javadocJar = false
                            }
                        }
                    }

                    stagingRepository(layout.buildDirectory.dir("staging-deploy").get().asFile.absolutePath)
                    stagingRepository(gradlePluginStagingDirectory)
                }
            }
        }
    }

    release {
        github {
            repoOwner = "jdekim43"
            prerelease.pattern = ".*-*"
        }
    }
}

listOf("publish", "publishToMavenLocal").forEach { name ->
    tasks.named(name) {
        dependsOn(publishedProjects.map { "${it.path}:$name" })
        dependsOn(gradle.includedBuild("gradle-plugin").task(":$name"))
    }
}

tasks.register("checkGradlePlugin") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Publishes every module locally, then runs the Gradle plugin's TestKit suite."
    dependsOn(tasks.named("publishToMavenLocal"))
    dependsOn(gradle.includedBuild("gradle-plugin").task(":check"))
}

val clearStagingDirectories = tasks.register<Delete>("clearStagingDirectories") {
    description = "Deletes the staged publications, so a release carries only what this build produced."
    delete(layout.buildDirectory.dir("staging-deploy"))
    delete(publishedProjects.map { it.layout.buildDirectory.dir("staging-deploy") })
}

allprojects {
    tasks.withType<PublishToMavenRepository>().configureEach {
        dependsOn(clearStagingDirectories)
    }
}
