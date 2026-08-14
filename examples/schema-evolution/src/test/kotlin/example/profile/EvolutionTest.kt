package example.profile

import example.profile.v1.converter
import example.profile.v2.Contact
import example.profile.v2.converter
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import example.profile.v1.Profile as ProfileV1
import example.profile.v1.Tier as TierV1
import example.profile.v2.Profile as ProfileV2
import example.profile.v2.Tier as TierV2

/**
 * The compatibility rules, run rather than recited.
 *
 * Each case is one change to `profile.proto` and the question a rolling deploy asks about it: can the
 * other version still read this? The last two are the answers that are worth knowing before writing the
 * migration rather than after.
 */
class EvolutionTest : StringSpec({

    fun v1() = ProfileV1(
        id = "usr_7",
        displayName = "Amelie Boucher",
        email = "amelie@example.com",
        tags = listOf("beta", "eu"),
        tier = TierV1.TIER_PRO,
        credits = 120u,
    )

    fun v2() = ProfileV2(
        id = "usr_7",
        fullName = "Amelie Boucher",
        tags = listOf("beta", "eu"),
        tier = TierV2.TIER_PRO,
        credits = 120uL,
        contact = Contact(email = "amelie@example.com", phone = "+33 1 23 45 67 89"),
        verified = true,
    )

    "a renamed field still decodes, because the number is the identity" {
        // display_name in v1, full_name in v2, both at number 2. The name is never on the wire — it
        // exists for the code generator and for whoever reads the schema.
        val decoded = ProfileV2.converter.deserialize(ProfileV1.converter.serialize(v1()))

        decoded.fullName shouldBe "Amelie Boucher"
        decoded.id shouldBe "usr_7"
        decoded.tags shouldContainExactly listOf("beta", "eu")
    }

    "a uint32 widened to uint64 reads in both directions" {
        // Same wire type, so field 6 is the same bytes either way. Only a value that outgrows 32 bits is
        // a problem, and that is not protobuf's to solve.
        ProfileV2.converter.deserialize(ProfileV1.converter.serialize(v1())).credits shouldBe 120uL
        ProfileV1.converter.deserialize(ProfileV2.converter.serialize(v2())).credits shouldBe 120u
    }

    "a reader skips fields it has never heard of" {
        // v2 writes contact (7) and verified (8). v1 has no idea what they are and does not fail — this
        // is what makes it safe to deploy a new writer before every reader is upgraded.
        val decoded = ProfileV1.converter.deserialize(ProfileV2.converter.serialize(v2()))

        decoded.displayName shouldBe "Amelie Boucher"
        decoded.tier shouldBe TierV1.TIER_PRO
        // Number 3 is reserved in v2, so nothing was written there and email is simply absent.
        decoded.email shouldBe ""
    }

    "a new field arrives at its default in the other direction" {
        val decoded = ProfileV2.converter.deserialize(ProfileV1.converter.serialize(v1()))

        // A singular message carries presence, so a field the old writer never wrote arrives as absent
        // rather than as an empty Contact the reader would have to guess about…
        decoded.contact shouldBe null
        // …and so does a scalar that asked for presence with `optional`.
        decoded.verified shouldBe null
    }

    "read-modify-write through an old schema drops the fields it did not know" {
        // The trap in every rolling deploy that has an old service updating records a new one wrote. The
        // unknown fields are skipped on the way in and are not carried on the Kotlin data class, so on
        // the way back out they are gone — permanently, and without an error to notice.
        val throughV1 = ProfileV1.converter.deserialize(ProfileV2.converter.serialize(v2()))
        val backToV2 = ProfileV2.converter.deserialize(ProfileV1.converter.serialize(throughV1))

        v2().contact?.email shouldBe "amelie@example.com"
        backToV2.contact shouldBe null
        backToV2.verified shouldBe null
        // What the old schema does understand is intact, which is why this is a trap and not a crash.
        backToV2.fullName shouldBe "Amelie Boucher"
    }

    "an enum value the reader does not know fails the decode" {
        // protobuf itself allows adding an enum value: proto3 keeps the unknown number on the wire and
        // the reader gets something for it. This generator maps enums by number through a lookup that
        // has no case for a number it was not generated with, so the decode throws instead.
        //
        // The consequence for a deploy: an added enum value is not a writer-first change here. Upgrade
        // the readers, then start writing the new value.
        val enterprise = v2().copy(tier = TierV2.TIER_ENTERPRISE)

        val failure = runCatching {
            ProfileV1.converter.deserialize(ProfileV2.converter.serialize(enterprise))
        }.exceptionOrNull()

        failure.shouldNotBeNull()
        failure.shouldBeInstanceOf<IllegalArgumentException>()

        // The same bytes are fine for every value v1 does know about.
        ProfileV1.converter
            .deserialize(ProfileV2.converter.serialize(v2().copy(tier = TierV2.TIER_FREE)))
            .tier shouldBe TierV1.TIER_FREE
    }
})
