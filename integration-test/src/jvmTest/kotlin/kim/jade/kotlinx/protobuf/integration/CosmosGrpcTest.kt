package kim.jade.kotlinx.protobuf.integration

import cosmos.bank.v1beta1.QueryBalanceRequest
import cosmos.bank.v1beta1.QueryBalanceResponse
import cosmos.bank.v1beta1.grpc.QueryGrpc
import cosmos.base.v1beta1.Coin
import io.grpc.Server
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kim.jade.kotlinx.protobuf.grpc.GrpcClientOption

/**
 * A real gRPC call over the generated stubs, end to end.
 *
 * The serialization tests check the codec in isolation. This one puts the whole stack in the path —
 * generated client, converters, grpc-java's own `MethodDescriptor`s from protoc-gen-grpc-java, the
 * marshallers, and the generated server — over an in-JVM transport, so nothing is stubbed except the
 * service body itself.
 */
class CosmosGrpcTest : StringSpec({

    /** Echoes the request back as a balance, so both directions carry something worth checking. */
    class EchoQuery : QueryGrpc.Server() {
        override suspend fun balance(request: QueryBalanceRequest): QueryBalanceResponse =
            QueryBalanceResponse(Coin(denom = request.denom, amount = "${request.address.length}"))
    }

    suspend fun <T> withServer(block: suspend (QueryGrpc.Client) -> T): T {
        val name = InProcessServerBuilder.generateName()
        val server: Server = InProcessServerBuilder.forName(name)
            .directExecutor()
            .addService(EchoQuery())
            .build()
            .start()
        val channel = InProcessChannelBuilder.forName(name).directExecutor().build()

        try {
            return block(QueryGrpc.createClient(GrpcClientOption(channel)))
        } finally {
            channel.shutdownNow()
            server.shutdownNow()
        }
    }

    "carries a request and a response over a real gRPC call" {
        val response = withServer { client ->
            client.balance(QueryBalanceRequest(address = "cosmos1abc", denom = "uatom"))
        }

        // Both fields came back through grpc-java's marshallers and this project's converters.
        response.balance?.denom shouldBe "uatom"
        response.balance?.amount shouldBe "10"
    }

    "carries an all-defaults request without losing the response" {
        // Nothing on the wire in either direction but the framing, which is where a codec that confuses
        // "empty" with "absent" comes apart. The server sets a Coin that is entirely at its defaults, so
        // the balance has to arrive present — a codec that dropped it would hand back null here.
        val response = withServer { client -> client.balance(QueryBalanceRequest()) }

        response.balance shouldNotBe null
        response.balance?.denom shouldBe ""
        response.balance?.amount shouldBe "0"
    }
})
