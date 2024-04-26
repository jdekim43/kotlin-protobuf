package kr.jadekim.protobuf.grpc.gateway.ktor.plugin

import io.ktor.client.plugins.api.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kr.jadekim.protobuf.kotlinx.ProtobufJsonFormat
import kotlin.text.toByteArray

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
    val serialized = config.format.encodeToString(serializer as KSerializer<Any?>, content)

    val charset = request.charset() ?: Charset.defaultCharset()
    val encoded = if (charset == Charset.defaultCharset()) serialized else {
        serialized.toByteArray(Charset.defaultCharset()).toString(charset)
    }

    return TextContent(encoded, ContentType.Application.Json.withCharset(charset))
}

actual suspend fun TransformResponseBodyContext.transformResponseBody(
    config: BodyConverterConfig,
    response: HttpResponse,
    content: ByteReadChannel,
    requestedType: TypeInfo
): Any? {
    val serializer = config.format.serializer(requestedType)
    val charset = response.charset() ?: Charset.defaultCharset()
    val body = content.readRemaining().readText(charset)

    return config.format.decodeFromString(serializer, body)
}

private fun ProtobufJsonFormat.serializer(typeInfo: TypeInfo): KSerializer<*> {
    return typeInfo.kotlinType?.let { serializersModule.serializer(it) }
        ?: serializersModule.serializer(typeInfo.reifiedType)
}