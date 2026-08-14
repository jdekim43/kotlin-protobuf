import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated

plugins {
    id("convention.protobuf-generator")
    id("convention.publish")
}

application {
    mainClass.set("kim.jade.kotlinx.protobuf.generator.KotlinGeneratorKt")
}

dependencies {
    api(project(":kotlinx-protobuf-core"))
    api(libs.protobuf.java)

    api(libs.kotlinpoet)
    api(libs.kasechange)
}

tasks.clean {
    subprojects.forEach { finalizedBy(it.tasks.clean) }
}

val generatedDirectory = layout.buildDirectory.dir("generated/sources/main/kotlin").get()
val writeGeneratorConstants = tasks.register("writeGeneratorConstants") {
    val generatorVersion = providers.provider { project.version.toString() }
    val outputDirectory = generatedDirectory.asFile
    val outputFile = outputDirectory.resolve("kim/jade/kotlinx/protobuf/generator/constants.kt")

    inputs.property("generatorVersion", generatorVersion)
    outputs.dir(outputDirectory)

    doLast {
        outputDirectory.deleteRecursively()
        val output = outputFile
        output.ensureParentDirsCreated()
        output.writeText(
            """
            package kim.jade.kotlinx.protobuf.generator

            /** Stamped into every generated file as @GeneratorVersion, so output can be traced back. */
            const val GENERATOR_VERSION: String = "${generatorVersion.get()}"
        """.trimIndent() + "\n"
        )
    }
}

sourceSets {
    main {
        java {
            srcDir(files(generatedDirectory).builtBy(writeGeneratorConstants))
        }
    }
}