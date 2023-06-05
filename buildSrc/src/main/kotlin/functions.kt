fun fullPath(originalPath: String): String = buildString {
    if (originalPath == ":") {
        return originalPath
    }

    val paths = originalPath.split(":").drop(1)

    repeat(paths.size) {
        append(":kotlin-protobuf-")
        append(paths.take(it + 1).joinToString("-"))
    }
}
