package kim.jade.kotlinx.protobuf.grpc.gateway.ktor.plugin

import io.ktor.client.plugins.api.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.readText
import kim.jade.kotlinx.protobuf.grpc.gateway.ktor.BODY_EXCLUDE_FIELDS
import kim.jade.kotlinx.protobuf.serialization.ProtobufJsonFormat
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

actual class BodyConverterConfig {
    var format = ProtobufJsonFormat()
}

@Suppress("UNCHECKED_CAST")
actual suspend fun TransformRequestBodyContext.transformRequestBody(
    config: BodyConverterConfig,
    request: HttpRequestBuilder,
    content: Any,
    bodyType: TypeInfo?
): OutgoingContent? {
    if (bodyType == null) return null

    val serializer = config.format.serializer(bodyType)
    val printed = config.format.encodeToString(serializer as KSerializer<Any?>, content)
    val serialized = printed.withoutFields(request.attributes.getOrNull(BODY_EXCLUDE_FIELDS).orEmpty())

    return TextContent(serialized, ContentType.Application.Json.withCharset(Charsets.UTF_8))
}

actual suspend fun TransformResponseBodyContext.transformResponseBody(
    config: BodyConverterConfig,
    response: HttpResponse,
    content: ByteReadChannel,
    requestedType: TypeInfo
): Any? {
    val serializer = config.format.serializer(requestedType)

    return config.format.decodeFromString(serializer, content.readRemaining().readText())
}

private fun ProtobufJsonFormat.serializer(typeInfo: TypeInfo): KSerializer<*> {
    val kotlinType = requireNotNull(typeInfo.kotlinType) {
        "The grpc-gateway body converter needs a KType to pick a serializer, and $typeInfo carries none."
    }

    return serializersModule.serializer(kotlinType)
}
