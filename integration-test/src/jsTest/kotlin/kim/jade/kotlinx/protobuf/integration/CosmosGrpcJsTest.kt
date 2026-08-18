package kim.jade.kotlinx.protobuf.integration

import cosmos.bank.v1beta1.QueryBalanceRequest
import cosmos.bank.v1beta1.QueryBalanceResponse
import cosmos.bank.v1beta1.grpc.QueryGrpc
import cosmos.base.v1beta1.Coin
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kim.jade.kotlinx.protobuf.grpc.DefaultGrpcClientOption
import kim.jade.kotlinx.protobuf.grpc.GrpcStatus
import kim.jade.kotlinx.protobuf.grpc.GrpcStatusException
import kim.jade.kotlinx.protobuf.grpc.addService
import kim.jade.kotlinx.protobuf.grpc.node.Server
import kim.jade.kotlinx.protobuf.grpc.node.ServerCredentials
import kotlinx.coroutines.await
import kotlin.js.Promise

/**
 * A real gRPC call on JS, end to end.
 *
 * The JVM twin of this runs over grpc-java's in-process transport; `@grpc/grpc-js` has no such thing, so
 * this one binds an actual socket on an ephemeral port. Everything between the two ends is the generated
 * stack: the client, the `GrpcMethod`s the generator declared, the protobuf.js converters that marshal
 * them, and the generated server.
 */
class CosmosGrpcJsTest : StringSpec({

    /** Echoes the request back as a balance, so both directions carry something worth checking. */
    class EchoQuery : QueryGrpc.Server() {
        override suspend fun balance(request: QueryBalanceRequest): QueryBalanceResponse =
            QueryBalanceResponse(Coin(denom = request.denom, amount = "${request.address.length}"))
    }

    class FailingQuery : QueryGrpc.Server() {
        override suspend fun balance(request: QueryBalanceRequest): QueryBalanceResponse =
            throw IllegalStateException("no such account")
    }

    fun listen(server: Server): Promise<Int> = Promise { resolve, reject ->
        server.bindAsync("127.0.0.1:0", ServerCredentials.createInsecure()) { error, port ->
            if (error != null) reject(Throwable("$error")) else resolve(port)
        }
    }

    suspend fun <T> withServer(service: QueryGrpc.Server, block: suspend (QueryGrpc.Client) -> T): T {
        val server = Server()
        server.addService(service.bindService())

        val port = listen(server).await()
        val option = DefaultGrpcClientOption("127.0.0.1", port)

        try {
            return block(QueryGrpc.createClient(option))
        } finally {
            option.channel.close()
            server.forceShutdown()
        }
    }

    "carries a request and a response over a real gRPC call" {
        val response = withServer(EchoQuery()) { client ->
            client.balance(QueryBalanceRequest(address = "cosmos1abc", denom = "uatom"))
        }

        // Both fields came back through @grpc/grpc-js and this project's protobuf.js converters.
        response.balance shouldNotBe null
        response.balance?.denom shouldBe "uatom"
        response.balance?.amount shouldBe "10"
    }

    "reports a failing handler as a status the caller can branch on" {
        val failure = withServer(FailingQuery()) { client ->
            runCatching { client.balance(QueryBalanceRequest(address = "cosmos1abc", denom = "uatom")) }
                .exceptionOrNull()
        }

        (failure is GrpcStatusException) shouldBe true
        (failure as GrpcStatusException).status shouldBe GrpcStatus.UNKNOWN
        failure.details shouldBe "no such account"
    }

    "reports a method the server never implemented as UNIMPLEMENTED" {
        // QueryGrpc.Server leaves every RPC throwing until it is overridden, which is what a caller sees
        // when a service is only half built.
        val bare = object : QueryGrpc.Server() {}

        val failure = withServer(bare) { client ->
            runCatching { client.balance(QueryBalanceRequest(address = "cosmos1abc", denom = "uatom")) }
                .exceptionOrNull()
        }

        (failure as GrpcStatusException).status shouldBe GrpcStatus.UNIMPLEMENTED
    }
})
