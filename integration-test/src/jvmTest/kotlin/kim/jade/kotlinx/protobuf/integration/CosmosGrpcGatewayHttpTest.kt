package kim.jade.kotlinx.protobuf.integration

import cosmos.bank.v1beta1.QueryBalanceRequest
import cosmos.bank.v1beta1.grpc.gateway.QueryGrpcGateway
import io.ktor.client.engine.cio.CIO as ClientCIO
import io.ktor.http.ContentType
import io.ktor.server.cio.CIO as ServerCIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.request.uri
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kim.jade.kotlinx.protobuf.grpc.gateway.GrpcGatewayClientOption
import kim.jade.kotlinx.protobuf.grpc.gateway.ktor.GrpcGatewayClient
import kim.jade.kotlinx.protobuf.grpc.gateway.ktor.GrpcGatewayClientConfigVariables
import java.util.concurrent.atomic.AtomicReference

/**
 * The gateway clients over an actual socket.
 *
 * [CosmosGrpcGatewayTest] stops at Ktor's engine boundary, which leaves the request line, the header
 * round trip and the response charset untested — the parts a MockEngine hands over rather than
 * performs. Here a real server answers a real client, so the only thing standing in for production is
 * the routing table.
 */
class CosmosGrpcGatewayHttpTest : StringSpec({

    "carries a gateway call over real HTTP" {
        val seen = AtomicReference<String>()
        val server = embeddedServer(ServerCIO, port = 0) {
            routing {
                get("/cosmos/bank/v1beta1/balances/{address}/by_denom") {
                    seen.set(call.request.uri)
                    call.respondText(
                        """{"balance":{"denom":"uatom","amount":"1234"}}""",
                        ContentType.Application.Json,
                    )
                }
            }
        }.start(wait = false)

        try {
            val port = server.engine.resolvedConnectors().first().port
            val client = GrpcGatewayClient(
                ClientCIO,
                GrpcGatewayClientConfigVariables("http://127.0.0.1:$port"),
            )

            val response = try {
                QueryGrpcGateway.createClient(GrpcGatewayClientOption(client))
                    .balance(QueryBalanceRequest(address = "cosmos1abc", denom = "uatom"))
            } finally {
                client.close()
            }

            // What actually went down the socket, path template and query parameter included.
            seen.get() shouldContain "/cosmos/bank/v1beta1/balances/cosmos1abc/by_denom"
            seen.get() shouldContain "denom=uatom"

            // And the body came back through protobuf's JSON mapping into the generated type.
            response.balance?.denom shouldBe "uatom"
            response.balance?.amount shouldBe "1234"
        } finally {
            server.stop(gracePeriodMillis = 0, timeoutMillis = 1_000)
        }
    }
})
