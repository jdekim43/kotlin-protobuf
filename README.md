# Kotlin Protobuf
[![Maven Central](https://img.shields.io/maven-central/v/kr.jadekim/kotlin-protobuf)](https://search.maven.org/artifact/kr.jadekim/kotlin-protobuf)

Protocol Buffer generator for kotlin multiplatform or single platform.

## Overview
### Features
* Compile data types
  * Support optional, repeated, map, oneof features
* Compile service type (grpc)
* Plugin
  * kotlinx-serialization adapter (You can use protobuf serializer with ProtobufFormat)

### Backlog
* Type Registry : Require to specify type registry class name
* JSON
* Kotlin/JS
* Kotlin/Native
* Plain kotlin serializer
* Grpc Web
* google.api (Restful Api)
* Gradle Plugin
* Utilities
  * ProtobufMessage.toAny : optimize toAny usage
  * Type Registry
* Add option infos of protobuf to generated files.
* Improve kotlin type mapping
  * google.protobuf.Any to kotlin.Any
  * google.protobuf.Empty to kotlin.Unit
  * some prebuilt types
  * Kotlinx serialization annotations

### Note
* This will use 'package' option in proto file. (Not java_package option)

### Examples
* [JVM Example](example/build.gradle.kts)
* [Multiplatform Example](example-multiplatform/build.gradle.kts)

## Usage
### Gradle :: JVM
#### build.gradle.kts
```
plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23" //optional
    id("com.google.protobuf") version "0.9.4"
}

sourceSets {
    main {
        proto {
            srcDir(File(project.projectDir, "src/main/proto"))
        }
    }
}

protobuf {
    plugins {
        //If you want without kotlinx-serialization.
        //id("kotlin-protobuf-kotlin") {
        //    artifact = "kr.jadekim:kotlin-protobuf-generator:$kotlinProtobufVersion"
        //}
        id("kotlin-protobuf-kotlinx") {
            artifact = "kr.jadekim:kotlin-protobuf-generator-kotlinx:$kotlinProtobufVersion"
        }
        id("kotlin-protobuf-converter-jvm") {
            artifact = "kr.jadekim:kotlin-protobuf-generator-converter-jvm:$kotlinProtobufVersion"
        }
        
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("kotlin-protobuf-grpc-jvm") {
            artifact = "kr.jadekim:kotlin-protobuf-generator-grpc-jvm:$kotlinProtobufVersion"
        }
    }
    
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("kotlin-protobuf-kotlinx")
                id("kotlin-protobuf-converter-jvm")

                id("grpc")
                id("kotlin-protobuf-grpc-jvm")
            }
        }
    }
}

dependencies {
    implementation("kr.jadekim:kotlin-protobuf:$kotlinProtobufVersion")
    implementation("kr.jadekim:kotlin-protobuf-kotlinx:$kotlinProtobufVersion")
    implementation("kr.jadekim:kotlin-protobuf-grpc:$kotlinProtobufVersion")

    implementation("com.google.protobuf:protobuf-java:$protobufVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
}
```

### Gradle :: Multiplatform
#### build.gradle.kts
```
plugins {
    kotlin("multiplatform") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23" //optional
    id("com.google.protobuf") version "0.9.4"
    `java-library`
}

sourceSets {
    main {
        proto {
            srcDir(File(project.projectDir, "src/main/proto"))
        }
    }
}

protobuf {
    plugins {
        //If you want without kotlinx-serialization.
        //id("kotlin-protobuf-kotlin") {
        //    artifact = "kr.jadekim:kotlin-protobuf-generator:$kotlinProtobufVersion"
        //}
        id("kotlin-protobuf-kotlinx") {
            artifact = "kr.jadekim:kotlin-protobuf-generator-kotlinx:$kotlinProtobufVersion"
        }
        id("kotlin-protobuf-converter-multiplatform") {
            artifact = "kr.jadekim:kotlin-protobuf-generator-multiplatform:$kotlinProtobufVersion"
        }
        id("kotlin-protobuf-converter-multiplatform-jvm") {
            artifact = "kr.jadekim:kotlin-protobuf-generator-multiplatform-jvm:$kotlinProtobufVersion"
        }
        
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("kotlin-protobuf-grpc-multiplatform") {
            artifact = "kr.jadekim:kotlin-protobuf-generator-grpc-multiplatform:$kotlinProtobufVersion"
        }
        id("kotlin-protobuf-grpc-multiplatform-jvm") {
            artifact = "kr.jadekim:kotlin-protobuf-generator-grpc-multiplatform-jvm:$kotlinProtobufVersion"
        }
    }
    
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("kotlin-protobuf-kotlinx")
                id("kotlin-protobuf-converter-multiplatform")
                id("kotlin-protobuf-converter-multiplatform-jvm")

                id("grpc")
                id("kotlin-protobuf-grpc-multiplatform")
                id("kotlin-protobuf-grpc-multiplatform-jvm")
            }
        }
    }
}

kotlin {
    jvm {
        withJava()
    }
    
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(File(buildDir, "generated/source/proto/main/kotlin-protobuf-kotlinx"))
            kotlin.srcDir(File(buildDir, "generated/source/proto/main/kotlin-protobuf-converter-multiplatform"))
            kotlin.srcDir(File(buildDir, "generated/source/proto/main/kotlin-protobuf-grpc-multiplatform"))

            dependencies {
                implementation("kr.jadekim:kotlin-protobuf:$kotlinProtobufVersion")
                implementation("kr.jadekim:kotlin-protobuf-kotlinx:$kotlinProtobufVersion")
                implementation("kr.jadekim:kotlin-protobuf-grpc:$kotlinProtobufVersion")
                
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
            }
        }

        val jvmMain by getting {
            kotlin.srcDir(File(buildDir, "generated/source/proto/main/java"))
            kotlin.srcDir(File(buildDir, "generated/source/proto/main/grpc"))
            kotlin.srcDir(File(buildDir, "generated/source/proto/main/kotlin-protobuf-converter-multiplatform-jvm"))
            kotlin.srcDir(File(buildDir, "generated/source/proto/main/kotlin-protobuf-grpc-multiplatform-jvm"))

            dependencies {
                implementation("com.google.protobuf:protobuf-java:$protobufVersion")
                implementation("io.grpc:grpc-protobuf:$grpcVersion")
                implementation("io.grpc:grpc-stub:$grpcVersion")
                implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
            }
        }
    }
}
```

### Options
* Include prebuilt files : `kotlin-protobuf.include_prebuilt=true`
* Set prefix to type url : `kotlin-protobuf.prefix=[some_prefix]`

#### Note
Option value can't have ','.

#### How to apply options
```
protobuf {
  ...
  generateProtoTasks {
    all().forEach {
      it.plugins {
        id(...) {
          option("[option string]")
          
          //example
          //option("kotlin-protobuf.include_prebuilt=true")
        }
      }
    }
  }
}
```
