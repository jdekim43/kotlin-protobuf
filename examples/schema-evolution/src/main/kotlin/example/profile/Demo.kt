package example.profile

import example.profile.v1.converter
import example.profile.v2.Contact
import example.profile.v2.converter
import example.profile.v1.Profile as ProfileV1
import example.profile.v1.Tier as TierV1
import example.profile.v2.Profile as ProfileV2
import example.profile.v2.Tier as TierV2

/**
 * The two versions reading each other's bytes.
 *
 * Run it with `./gradlew :examples:schema-evolution:run`.
 */
fun main() {
    // --- A v1 writer, a v2 reader. The upgrade nobody can order: the new service is already deployed
    // --- and the queue is still full of messages the old one wrote.
    val old = ProfileV1(
        id = "usr_7",
        displayName = "Amelie Boucher",
        email = "amelie@example.com",
        tags = listOf("beta", "eu"),
        tier = TierV1.TIER_PRO,
        credits = 120u,
    )
    val asV2 = ProfileV2.converter.deserialize(ProfileV1.converter.serialize(old))

    println("v1 → v2")
    println("  fullName  : ${asV2.fullName}          (field 2, renamed from display_name)")
    println("  credits   : ${asV2.credits}           (field 6, uint32 widened to uint64)")
    println("  tier      : ${asV2.tier}")
    println("  contact   : ${asV2.contact}           (field 7, a message — has presence, so absent)")
    println("  verified  : ${asV2.verified}          (field 8, optional and absent — null)")
    println("  email     : gone; v2 reserved number 3, so the value is skipped")

    // --- A v2 writer, a v1 reader. The other direction, and the one that decides whether the new
    // --- service can be deployed before the old one is retired.
    val new = ProfileV2(
        id = "usr_7",
        fullName = "Amelie Boucher",
        tags = listOf("beta", "eu"),
        tier = TierV2.TIER_PRO,
        credits = 120uL,
        contact = Contact(email = "amelie@example.com", phone = "+33 1 23 45 67 89"),
        verified = true,
    )
    val asV1 = ProfileV1.converter.deserialize(ProfileV2.converter.serialize(new))

    println()
    println("v2 → v1")
    println("  displayName : ${asV1.displayName}")
    println("  credits     : ${asV1.credits}")
    println("  email       : \"${asV1.email}\"        (field 3 was never written by v2)")
    println("  contact and verified: skipped, not an error")

    // --- Read-modify-write with the old schema. The trap: the fields a v1 reader skipped are not
    // --- carried on the Kotlin data class, so writing the message back out loses them.
    val reencoded = ProfileV2.converter.deserialize(ProfileV1.converter.serialize(asV1))

    println()
    println("v2 → v1 → v2 (round-tripped through the old schema)")
    println("  contact  : ${reencoded.contact}")
    println("  verified : ${reencoded.verified}")

    // --- An enum value v1 has never heard of. Adding one is a wire-compatible change in protobuf —
    // --- proto3 keeps the unknown number around and every language's runtime hands the reader something
    // --- for it. This generator does not: the decode throws, so an added enum value is the one change
    // --- in this file that has to be deployed to readers first.
    val enterprise = new.copy(tier = TierV2.TIER_ENTERPRISE)
    val result = runCatching { ProfileV1.converter.deserialize(ProfileV2.converter.serialize(enterprise)) }

    println()
    println("v2 → v1 with TIER_ENTERPRISE (number 3, unknown to v1)")
    println("  ${result.exceptionOrNull()?.let { "threw ${it::class.simpleName}: ${it.message}" } ?: "decoded as ${result.getOrNull()?.tier}"}")
}
