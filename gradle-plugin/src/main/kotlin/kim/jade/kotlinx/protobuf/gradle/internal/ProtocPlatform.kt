package kim.jade.kotlinx.protobuf.gradle.internal

import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory

/** Maps the host to the classifier of the `com.google.protobuf:protoc` binary artifact. */
internal object ProtocPlatform {

    /**
     * Read through [ProviderFactory] rather than `System.getProperty`, so the values are tracked as
     * configuration cache inputs instead of being silently baked into a cached entry.
     */
    fun classifier(providers: ProviderFactory): Provider<String> =
        providers.systemProperty("os.name").zip(providers.systemProperty("os.arch")) { osName, osArch ->
            classifier(osName, osArch)
        }

    fun classifier(osName: String, osArch: String): String {
        val os = osName.lowercase()
        val arch = osArch.lowercase()

        val osPart = when {
            os.contains("mac") || os.contains("darwin") -> "osx"
            os.contains("win") -> "windows"
            os.contains("linux") -> "linux"
            else -> unsupported(osName, osArch)
        }

        val archPart = when (arch) {
            "x86_64", "amd64", "x64" -> "x86_64"
            "aarch64", "arm64" -> "aarch_64"
            "x86", "i386", "i486", "i586", "i686" -> "x86_32"
            "ppc64le" -> "ppcle_64"
            "s390x" -> "s390_64"
            else -> unsupported(osName, osArch)
        }

        return "$osPart-$archPart"
    }

    private fun unsupported(osName: String, osArch: String): Nothing = throw IllegalStateException(
        "No protoc binary is published for $osName/$osArch. " +
            "Install protoc yourself and point the build at it with kotlinxProtobuf { protocPath = file(\"…\") }."
    )
}
