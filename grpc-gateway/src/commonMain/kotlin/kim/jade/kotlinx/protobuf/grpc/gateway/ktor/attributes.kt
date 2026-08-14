package kim.jade.kotlinx.protobuf.grpc.gateway.ktor

import io.ktor.util.*

val BODY_EXCLUDE_FIELDS = AttributeKey<List<String>>("grpc-gateway.bodyExcludedFields")