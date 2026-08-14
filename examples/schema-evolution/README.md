# schema-evolution

**Two versions of the same message, deployed at the same time.**

During a rolling deploy the old service and the new one are both running, both reading each other's bytes,
and neither can go first. This module generates two versions of one message side by side, so the
cross-version decodes can be run rather than reasoned about.

- [`profile/v1`](src/main/proto/example/profile/v1/profile.proto) and
  [`profile/v2`](src/main/proto/example/profile/v2/profile.proto) — the same message either side of five
  changes.
- [`Demo.kt`](src/main/kotlin/example/profile/Demo.kt) — every decode in both directions, including the
  read-modify-write that loses data and the enum value that throws.
- [`EvolutionTest.kt`](src/test/kotlin/example/profile/EvolutionTest.kt) — the same claims as assertions.

```bash
./gradlew :examples:schema-evolution:run
./gradlew :examples:schema-evolution:test
```

## The changes, and what each one costs

| Change | v1 → v2 | v2 → v1 |
|---|---|---|
| `display_name` renamed to `full_name`, still number 2 | reads fine | reads fine |
| `credits` widened from `uint32` to `uint64` | reads fine | reads fine, small values |
| `contact` and `verified` added at 7 and 8 | arrive as `null` — both have presence | skipped, not an error |
| `email` removed and number 3 reserved | value dropped | field never written |
| `TIER_ENTERPRISE` added to the enum | reads fine | **throws** |

The name is never on the wire — the number is the field's identity, which is why a rename is free and
reusing a number is the one thing `reserved` exists to prevent.

## The two answers worth knowing before the migration

**Read-modify-write through the old schema loses data.** A v1 reader skips fields it does not know, and
those fields are not carried on the Kotlin data class, so writing the message back out drops them
permanently — with no error to notice. If an old service updates records a new one wrote, that is a
migration problem, not a protobuf one.

**An added enum value is not a writer-first change here.** protobuf allows it: proto3 keeps the unknown
number on the wire and every runtime hands the reader something for it. This generator maps enums by
number through a lookup with no case for a number it was not generated with, so a v1 reader decoding
`TIER_ENTERPRISE` throws `IllegalArgumentException`. Deploy the readers first, then start writing the new
value.
