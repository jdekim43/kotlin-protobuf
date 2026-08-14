# Examples

One module per reason people reach for protobuf. Each is a small, complete build that applies the plugin
the way a consumer would, has a `main` that prints what it does, and a test suite that asserts the same
claims — so nothing here is a snippet that used to compile.

| Module | Use case | Run it |
|---|---|---|
| [serialization](serialization) | A record that outlives its process: compact bytes for the wire, JSON when a human reads it | `./gradlew :examples:serialization:run` |
| [schema-evolution](schema-evolution) | Two versions of a message deployed at once, reading each other's bytes | `./gradlew :examples:schema-evolution:run` |
| [event-envelope](event-envelope) | An event stream whose payload type is not known until it arrives — `google.protobuf.Any` | `./gradlew :examples:event-envelope:run` |
| [grpc](grpc) | A service contract two teams build against, with all four streaming shapes | `./gradlew :examples:grpc:run` |
| [rest-gateway](rest-gateway) | The same schema serving HTTP/JSON, driven by `google.api.http` | `./gradlew :examples:rest-gateway:run` |

```bash
./gradlew :examples:serialization:test :examples:schema-evolution:test :examples:event-envelope:test :examples:grpc:test :examples:rest-gateway:jvmTest
```

## Two things these build files do that yours will not

They are inside the repository that builds the plugin, which shows up in two places:

- **Dependencies are `project(…)`, not coordinates.** A consumer writes
  `implementation("kim.jade:kotlinx-protobuf:<version>")` — see [Runtime modules](../README.md#runtime-modules)
  for which artifact goes with which generator.
- **Each generator carries a `classpath.setFrom(…)`.** That points it at the sibling project that builds
  it, so the examples exercise the working tree instead of the last published version. Outside this
  repository the plugin resolves generators by coordinate and `kotlin()` or `grpcJvm()` on its own is the
  whole declaration.

Everything else — the `proto { }` blocks, the source layout, the generated API — is what a consumer gets.

## Which generators each module uses

| Module | Source set | Generators |
|---|---|---|
| serialization | `main` | `kotlinxSerialization()`, `converterJvm()` |
| schema-evolution | `main` | `kotlin()`, `converterJvm()` |
| event-envelope | `main` | `kotlin()` + `typeRegistry`, `converterJvm()` |
| grpc | `main` | `kotlin()`, `converterJvm()`, `grpcJvm()` |
| rest-gateway | `commonMain` / `jvmMain` | `kotlinxSerialization()`, `converterMultiplatform()`, `grpcGateway()` |

`kotlin()` is the default choice: plain data classes, and one dependency — `kotlinx-protobuf-core`. Two
modules ask for `kotlinxSerialization()` instead, and only because something they show needs the
serializers it adds: `serialization` puts a message through `ProtobufFormat` and `ProtobufJsonFormat`, and
`rest-gateway`'s generated REST clients encode their bodies through kotlinx.serialization. Everything else
— the converters, `toAny()`/`parse`, the gRPC clients and servers, `typeRegistry` — is the same either way.

`rest-gateway` is the multiplatform one: its protos live in `src/commonMain/proto` and the generated types
and REST client land in `commonMain`, which is the arrangement the plugin exists for. The other four are
plain `kotlin("jvm")` builds, because that is what their use case needs and it keeps them short.
