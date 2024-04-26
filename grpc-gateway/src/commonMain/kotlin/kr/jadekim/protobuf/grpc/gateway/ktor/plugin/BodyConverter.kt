package kr.jadekim.protobuf.grpc.gateway.ktor.plugin

import io.ktor.client.plugins.api.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.content.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*

expect class BodyConverterConfig constructor() {
}

val BodyConverter = createClientPlugin("RequestBodyFieldFilter", ::BodyConverterConfig) {
    transformRequestBody { request, content, bodyType ->
        transformRequestBody(
            this@createClientPlugin.pluginConfig,
            request,
            content,
            bodyType,
        )
    }
    transformResponseBody { response, content, requestedType ->
        transformResponseBody(
            this@createClientPlugin.pluginConfig,
            response,
            content,
            requestedType,
        )
    }
}

expect suspend fun TransformRequestBodyContext.transformRequestBody(
    config: BodyConverterConfig,
    request: HttpRequestBuilder,
    content: Any,
    bodyType: TypeInfo?
): OutgoingContent?

expect suspend fun TransformResponseBodyContext.transformResponseBody(
    config: BodyConverterConfig,
    response: HttpResponse,
    content: ByteReadChannel,
    requestedType: TypeInfo
): Any?
