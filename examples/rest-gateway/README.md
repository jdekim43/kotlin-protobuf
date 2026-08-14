# rest-gateway

**The same schema serving HTTP/JSON, not only gRPC.**

[`library.proto`](src/commonMain/proto/example/library/v1/library.proto) carries `google.api.http` options —
the ones a grpc-gateway, an Envoy transcoder or an ESPv2 deployment is configured from. `grpcGateway()`
reads the same options and generates a Ktor client for that REST surface, so the client and the deployed
gateway follow one document instead of agreeing by convention.

This is also the multiplatform module: the protos live in `src/commonMain/proto` and the generated types,
converters and REST client land in `commonMain`.

- [`LibraryServer.kt`](src/jvmMain/kotlin/example/library/LibraryServer.kt) — a Ktor app serving the routes
  the annotations describe. In a deployment nobody writes this: the gateway transcodes to gRPC. It is here
  so the example is one process, and so both halves can be read side by side.
- [`Demo.kt`](src/jvmMain/kotlin/example/library/Demo.kt) — the generated client calling it.
- [`LibraryGatewayTest.kt`](src/jvmTest/kotlin/example/library/LibraryGatewayTest.kt) — the requests
  inspected over Ktor's `MockEngine`, then the same calls over a real socket.

```bash
./gradlew :examples:rest-gateway:run
./gradlew :examples:rest-gateway:jvmTest
```

## The mapping, as the four RPCs use it

| RPC | Annotation | What the client sends |
|---|---|---|
| `GetBook` | `get: "/v1/shelves/{shelf}/books/{book}"` | both fields in the path; no body, no query |
| `ListBooks` | `get: "/v1/shelves/{shelf}/books"` | `shelf` in the path, the rest as query parameters |
| `CreateBook` | `post: …` + `body: "book"` | the book alone as the body, `shelf` in the path |
| `CreateBook` binding 2 | `additional_bindings { post: "/v1/books" body: "*" }` | the whole request message as the body |
| `DeleteBook` | `delete: "/v1/shelves/{shelf}/books/{book}"` | nothing; the response is `Empty`, i.e. `{}` |

`body` decides what is sent, not the HTTP verb. `body: "book"` names a singular message, which is nullable,
so a `CreateBookRequest` with no book sends no body at all rather than an empty JSON object — and
`LibraryServer` answers that with `INVALID_ARGUMENT` instead of storing a blank book. `additional_bindings`
are alternative routes onto the same RPC and cannot also be the interface's method, so the generator puts
them beside it as `createBookBinding2`, `…Binding3`, in declaration order — which is why `createClient`
returns the concrete client rather than the interface.

## Two things worth knowing

**Query parameters carry the proto field name.** `page_size`, not the `pageSize` that protobuf's JSON
mapping uses inside a body. A hand-written server has to read the former.

**A `uint32` page size cannot say "unset".** Implicit presence means an unset `page_size` arrives as
`page_size=0`, so a server reading it literally returns an empty page. Treating 0 as "the server decides"
is what Google's own API guidelines settle on, and what `LibraryServer` does.

Errors surface: `GrpcGatewayClient` sets `expectSuccess`, so a 404 is an exception rather than a `Book`
with no title.
