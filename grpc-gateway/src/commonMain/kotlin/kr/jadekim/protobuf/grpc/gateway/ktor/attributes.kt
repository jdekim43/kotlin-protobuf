package kr.jadekim.protobuf.grpc.gateway.ktor

import io.ktor.util.*

val BODY_EXCLUDE_FIELDS = AttributeKey<List<String>>("grpc-gateway.bodyExcludedFields")