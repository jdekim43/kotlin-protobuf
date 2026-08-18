package kim.jade.kotlinx.protobuf.grpc.node

internal val isNode: Boolean =
    js("typeof process !== 'undefined' && process.versions != null && process.versions.node != null")
        .unsafeCast<Boolean>()

internal fun requireNode(what: String) {
    check(isNode) {
        "$what needs Node: @grpc/grpc-js speaks HTTP/2 over a socket, which a browser cannot open. " +
            "Generate a REST client with grpcGateway() for browser targets — it is Ktor, and runs anywhere."
    }
}
