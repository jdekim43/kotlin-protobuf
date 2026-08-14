plugins {
    `kotlin-dsl`
}

repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(enforcedPlatform("org.bouncycastle:bc-jdk18on-bom:1.84"))

    implementation(kt.kotlin.gradlePlugin)
    implementation(libs.android.kmp.library.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.kotest.gradlePlugin)
    implementation(kt.dokka)
    implementation(kt.dokka.javadoc)
    implementation(libs.shadowJar.gradlePlugin)
}
