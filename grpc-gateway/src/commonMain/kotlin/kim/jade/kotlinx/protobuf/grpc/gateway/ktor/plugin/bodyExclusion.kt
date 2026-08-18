package kim.jade.kotlinx.protobuf.grpc.gateway.ktor.plugin

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

//todo: use config.format. It's required that ProtobufJsonFormat would be extends JsonFormat.
internal fun String.withoutFields(paths: List<String>): String {
    if (paths.isEmpty()) return this

    val root = runCatching { Json.parseToJsonElement(this) }.getOrNull() as? JsonObject ?: return this

    return paths.fold(root) { pruned, path -> pruned.removePath(path.split('.')) }.toString()
}

private fun JsonObject.removePath(path: List<String>): JsonObject {
    val head = path.firstOrNull() ?: return this
    val value = this[head] ?: return this

    if (path.size == 1) {
        return JsonObject(this - head)
    }

    if (value !is JsonObject) {
        return this
    }

    return JsonObject(this + (head to value.removePath(path.drop(1))))
}