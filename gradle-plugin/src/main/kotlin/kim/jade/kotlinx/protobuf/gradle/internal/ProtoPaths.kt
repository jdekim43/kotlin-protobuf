package kim.jade.kotlinx.protobuf.gradle.internal

import java.io.File

internal object ProtoPaths {

    /**
     * Names each proto relative to the root it lives under. protoc identifies files by exactly that
     * path, so it is also what `file_to_generate` must contain.
     *
     * Sorted, because the registry generators accumulate into insertion-ordered maps — unstable input
     * order would rewrite the registry file on every run and defeat up-to-date checks and the build cache.
     */
    fun relativeNames(protoFiles: Iterable<File>, roots: Iterable<File>): List<String> {
        val directories = roots.filter { it.isDirectory }

        return protoFiles
            .filter { it.isFile }
            .mapNotNull { file ->
                directories.asSequence()
                    .map { root -> file.relativeToOrNull(root)?.invariantSeparatorsPath }
                    .filterNotNull()
                    .firstOrNull { !it.startsWith("..") }
            }
            .distinct()
            .sorted()
    }

    /** Encodes options the way protoc passes them to a plugin: `key=value` pairs separated by commas. */
    fun encodeOptions(options: Map<String, String>): String = options.entries
        .sortedBy { it.key }
        .joinToString(",") { (key, value) -> if (value.isEmpty()) key else "$key=$value" }
}
