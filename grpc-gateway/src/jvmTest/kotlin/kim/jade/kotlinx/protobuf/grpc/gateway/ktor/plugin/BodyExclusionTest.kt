package kim.jade.kotlinx.protobuf.grpc.gateway.ktor.plugin

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

/**
 * The pruning behind `BODY_EXCLUDE_FIELDS`.
 *
 * protobuf's HTTP mapping says a `body: "*"` carries the request message *except* what the path template
 * already bound, and a server handed both has two answers to choose between. The generated client says
 * which fields those are; this is the half that acts on it.
 *
 * The names are `json_name`s, not `.proto` field names: what goes on the wire is protobuf's JSON
 * mapping, so that is what the printed body is keyed by.
 */
class BodyExclusionTest : StringSpec({

    "drops a top-level field" {
        """{"address":"cosmos1abc","denom":"uatom"}""".withoutFields(listOf("address")) shouldBe
            """{"denom":"uatom"}"""
    }

    "drops a nested field without disturbing its siblings" {
        val body = """{"page":{"key":"AQID","limit":"10"},"denom":"uatom"}"""

        body.withoutFields(listOf("page.key")) shouldBe """{"page":{"limit":"10"},"denom":"uatom"}"""
    }

    "leaves a body alone when nothing is excluded" {
        val body = """{"denom":"uatom"}"""

        body.withoutFields(emptyList()) shouldBe body
    }

    "ignores a path the body does not have" {
        // A field at its default is omitted by protobuf's JSON printer, so a path bound in the template
        // is routinely absent from the body. That is not an error.
        val body = """{"denom":"uatom"}"""

        body.withoutFields(listOf("address", "page.key")) shouldBe body
    }

    "leaves a body it cannot parse untouched" {
        // Nothing here should be able to turn a request into a differently-broken request.
        "not json".withoutFields(listOf("address")) shouldBe "not json"
    }
})
