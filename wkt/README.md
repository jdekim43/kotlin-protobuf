# well-known types

Pre-generated Kotlin types for the protobuf well-known types, so consumers get `google.protobuf.Timestamp`
and friends without running protoc themselves. They carry the kotlinx.serialization annotations, so the
same types work through `ProtobufFormat` as well as through the converters — there is no separate
kotlinx flavour of this module.

Nothing here is checked in. The module applies this repository's own Gradle plugin — `gradle-plugin` is
an included build, so it is available inside the build that produces it — and generates the Kotlin during
`./gradlew :kotlinx-protobuf-wkt:build`.

The `.proto` definitions are not vendored either: they are read straight out of the `protobuf-java` jar,
so they cannot drift away from the runtime these types are converted against. A hand-copied snapshot did
drift once — the old descriptor.proto still had `php_generic_services` and lacked `features`, and the
generated JVM converters stopped compiling against protobuf-java's own `DescriptorProtos`.

No Java is generated here: protobuf-java already ships `com.google.protobuf.*` for every one of these
types, and a second copy would shadow it.

### Includes

* google/protobuf/any.proto
* google/protobuf/api.proto
* google/protobuf/descriptor.proto
* google/protobuf/duration.proto
* google/protobuf/empty.proto
* google/protobuf/field_mask.proto
* google/protobuf/source_context.proto
* google/protobuf/struct.proto
* google/protobuf/timestamp.proto
* google/protobuf/type.proto
* google/protobuf/wrappers.proto
