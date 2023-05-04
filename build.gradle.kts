plugins {
    kotlin("multiplatform") version "1.8.20"
}

allprojects {
    group = "kr.jadekim"
    version = "0.1.0"

    repositories {
        mavenCentral()
    }

//    tasks.test {
//        useJUnitPlatform()
//    }
}

configure(allprojects.filterNot { it.path.startsWith(":kotlin-protobuf-generator") || it.path == ":kotlin-protobuf-example" }) {
    apply {
        plugin("kotlin-multiplatform")
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
