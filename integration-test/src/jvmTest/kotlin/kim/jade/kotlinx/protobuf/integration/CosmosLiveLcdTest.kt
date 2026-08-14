package kim.jade.kotlinx.protobuf.integration

import cosmos.JvmTypeRegistry
import cosmos.bank.v1beta1.MsgSend
import cosmos.bank.v1beta1.parse
import cosmos.base.tendermint.v1beta1.GetLatestBlockRequest
import cosmos.base.tendermint.v1beta1.grpc.gateway.ServiceGrpcGateway as TendermintGateway
import cosmos.tx.v1beta1.GetTxsEventRequest
import cosmos.tx.v1beta1.grpc.gateway.ServiceGrpcGateway as TxGateway
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kim.jade.kotlinx.protobuf.grpc.gateway.GrpcGatewayClientOption
import kim.jade.kotlinx.protobuf.grpc.gateway.ktor.GrpcGatewayClient
import kim.jade.kotlinx.protobuf.grpc.gateway.ktor.GrpcGatewayClientConfigVariables
import kim.jade.kotlinx.protobuf.serialization.ProtobufJsonFormat

/**
 * The generated clients against a live Cosmos Hub node.
 *
 * Every other test here decides what the server says. This one does not: it asks a public LCD for real
 * transactions and parses whatever comes back — messages wrapped in `Any` and resolved through the
 * generated `TypeRegistry`, a `oneof` for the signing mode, enums, `uint64` gas figures, and a memo
 * some stranger wrote. Production JSON has a way of containing things a fixture never would.
 *
 * Off by default; see the `liveTests` property in this module's build file.
 */
class CosmosLiveLcdTest : StringSpec({

    val enabled = System.getProperty("kotlinxProtobuf.liveTests").toBoolean()
    val lcdUrl = System.getProperty("kotlinxProtobuf.lcdUrl") ?: "https://cosmos-rest.publicnode.com"

    /** The `@type` values in a raw LCD response, however deeply they are nested. */
    fun typeUrlsIn(body: String): Set<String> =
        Regex("\"@type\"\\s*:\\s*\"([^\"]+)\"").findAll(body).map { it.groupValues[1] }.toSet()

    "reads real transactions from a public LCD and parses them".config(enabled = enabled) {
        // The registry the JVM converters generated. Without it protobuf's JSON parser has no descriptor
        // for what an `Any` holds and refuses the whole document.
        val http = GrpcGatewayClient(
            CIO,
            GrpcGatewayClientConfigVariables(lcdUrl, format = ProtobufJsonFormat(JvmTypeRegistry.messages)),
        )
        val plain = HttpClient(CIO)

        try {
            val tendermint = TendermintGateway.createClient(GrpcGatewayClientOption(http))
            val transactions = TxGateway.createClient(GrpcGatewayClientOption(http))

            val latest = tendermint.getLatestBlock(GetLatestBlockRequest())
            val height = latest.block?.header?.height.shouldNotBeNull()
            height shouldBeGreaterThan 0L

            // Recent heights only: a public node prunes, so an old one would 404 for reasons that have
            // nothing to do with this library.
            //
            // The scan is plain HTTP on purpose — it picks a block, it does not test anything. Cosmos Hub
            // blocks routinely carry IBC messages, and ibc-go's protos live in another repository: an
            // `Any` cannot be decoded without a descriptor for what is inside it, so a block carrying one
            // would fail here for a reason that is a fact about this module's proto set rather than a
            // defect. Blocks of nothing but cosmos-sdk messages are common enough to find within a dozen.
            val chosen = (2..14)
                .map { height - it }
                .firstOrNull { candidate ->
                    val body = plain
                        .get("$lcdUrl/cosmos/tx/v1beta1/txs?query=tx.height%3D$candidate")
                        .bodyAsText()
                    val types = typeUrlsIn(body)
                    types.isNotEmpty() && types.all { it.startsWith("/cosmos.") }
                }
                ?: error("No block in ${height - 14}..${height - 2} held only cosmos-sdk messages")

            // From here on everything goes through the generated client and the generated converters.
            val response = transactions.getTxsEvent(GetTxsEventRequest(query = "tx.height=$chosen"))

            response.txs.shouldNotBeEmpty()
            response.txResponses.shouldNotBeEmpty()

            // The response side: real gas figures and the height we asked for.
            val txResponse = response.txResponses.first()
            txResponse.height shouldBe chosen
            txResponse.gasUsed shouldBeGreaterThan 0L
            txResponse.txhash.length shouldBe 64

            // The transaction side: every message arrived inside an Any and resolved through the registry.
            val body = response.txs.first().body.shouldNotBeNull()
            body.messages.shouldNotBeEmpty()
            body.messages.forEach { it.typeUrl shouldNotBe "" }

            // The signing mode is a oneof holding an enum — both decoded from JSON, not from the wire.
            val signerInfo = response.txs.first().authInfo?.signerInfos?.firstOrNull()
            signerInfo shouldNotBe null

            // And a transfer, if one turned up, unpacked the rest of the way. The LCD writes an Any's
            // type as "/cosmos.bank.v1beta1.MsgSend" with no prefix, which parse has to accept.
            val send = response.txs
                .flatMap { it.body?.messages.orEmpty() }
                .firstOrNull { it.typeUrl.endsWith(".MsgSend") }

            if (send != null) {
                val message = MsgSend.parse(send)
                message.fromAddress shouldNotBe ""
                message.toAddress shouldNotBe ""
                message.amount.shouldNotBeEmpty()
            }
        } finally {
            plain.close()
            http.close()
        }
    }
})
