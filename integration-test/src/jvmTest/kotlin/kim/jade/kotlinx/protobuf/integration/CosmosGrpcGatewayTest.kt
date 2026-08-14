package kim.jade.kotlinx.protobuf.integration

import cosmos.bank.v1beta1.QueryBalanceRequest
import cosmos.bank.v1beta1.grpc.gateway.QueryGrpcGateway
import cosmos.tx.v1beta1.SimulateRequest
import cosmos.tx.v1beta1.grpc.gateway.ServiceGrpcGateway
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kim.jade.kotlinx.protobuf.grpc.gateway.GrpcGatewayClientOption
import kim.jade.kotlinx.protobuf.grpc.gateway.ktor.GrpcGatewayClient
import kim.jade.kotlinx.protobuf.grpc.gateway.ktor.GrpcGatewayClientConfigVariables

/**
 * The REST clients `grpcGateway()` builds from `google.api.http`, driven end to end.
 *
 * cosmos-sdk annotates its query services with the same options a real gateway is configured from, so
 * this checks the parts a hand-written fixture would not reach: a path template with a `{address}`
 * placeholder, the fields left over becoming query parameters, a POST carrying its request as a body,
 * and responses decoded through protobuf's own JSON mapping rather than a plain kotlinx one.
 *
 * The transport is Ktor's MockEngine, so requests can be inspected and no server is involved.
 */
class CosmosGrpcGatewayTest : StringSpec({

    /** Answers every call with [responseBody] and records what was asked. */
    class Recorder(private val responseBody: String) {
        lateinit var request: HttpRequestData
            private set

        val client = GrpcGatewayClient(
            MockEngine,
            GrpcGatewayClientConfigVariables("https://api.example.com"),
        ) {
            engine {
                addHandler { data ->
                    request = data
                    respond(
                        content = responseBody,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", ContentType.Application.Json.toString()),
                    )
                }
            }
        }
    }

    "fills a path template and turns the leftover fields into query parameters" {
        val recorder = Recorder("""{"balance":{"denom":"uatom","amount":"1000"}}""")
        val query = QueryGrpcGateway.createClient(GrpcGatewayClientOption(recorder.client))

        val response = query.balance(QueryBalanceRequest(address = "cosmos1abc", denom = "uatom"))

        // option (google.api.http).get = "/cosmos/bank/v1beta1/balances/{address}/by_denom"
        recorder.request.method shouldBe HttpMethod.Get
        recorder.request.url.encodedPath shouldBe "/cosmos/bank/v1beta1/balances/cosmos1abc/by_denom"
        // address went into the path, so only denom is left to carry as a query parameter.
        recorder.request.url.parameters["denom"] shouldBe "uatom"
        recorder.request.url.parameters["address"] shouldBe null
        // A GET has no body, and the request message must not be sent as one.
        recorder.request.body shouldBe EmptyContent

        response.balance?.denom shouldBe "uatom"
        response.balance?.amount shouldBe "1000"
    }

    "sends the request as a protobuf JSON body on a POST endpoint" {
        val recorder = Recorder("""{"gasInfo":{"gasUsed":"42"}}""")
        val service = ServiceGrpcGateway.createClient(GrpcGatewayClientOption(recorder.client))

        val response = service.simulate(SimulateRequest(txBytes = byteArrayOf(1, 2, 3)))

        // option (google.api.http).post = "/cosmos/tx/v1beta1/simulate", with body: "*"
        recorder.request.method shouldBe HttpMethod.Post
        recorder.request.url.encodedPath shouldBe "/cosmos/tx/v1beta1/simulate"

        val body = recorder.request.body.shouldNotBeNull() as TextContent
        // protobuf's JSON mapping, not kotlinx's default: bytes are base64 and the field is camelCased.
        body.text shouldContain "txBytes"
        body.text shouldContain "AQID"

        response.gasInfo?.gasUsed shouldBe 42uL
    }

    "reports a gateway error rather than returning an empty message" {
        val client = GrpcGatewayClient(
            MockEngine,
            GrpcGatewayClientConfigVariables("https://api.example.com"),
        ) {
            engine {
                addHandler {
                    respond(
                        content = """{"code":5,"message":"not found"}""",
                        status = HttpStatusCode.NotFound,
                        headers = headersOf("Content-Type", ContentType.Application.Json.toString()),
                    )
                }
            }
        }
        val query = QueryGrpcGateway.createClient(GrpcGatewayClientOption(client))

        // expectSuccess is on in GrpcGatewayClient, so a 404 has to surface instead of being decoded
        // into a default-valued response.
        val failure = runCatching {
            query.balance(QueryBalanceRequest(address = "cosmos1abc", denom = "uatom"))
        }.exceptionOrNull()

        failure.shouldNotBeNull()
    }
})
