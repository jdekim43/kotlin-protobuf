# grpc

**A service contract two teams build against.**

[`chat.proto`](src/main/proto/example/chat/v1/chat.proto) declares four RPCs, one of each streaming shape.
The generator turns the service into an interface of `suspend` functions and `Flow`s; the server implements
that interface and the client *is* that interface, so a caller cannot tell a local implementation from a
remote one — and neither side can drift from the schema without the compiler saying so.

- [`InMemoryChatService.kt`](src/main/kotlin/example/chat/InMemoryChatService.kt) — the service. No
  `StreamObserver`, no `MethodDescriptor`, no marshaller.
- [`ChatServer.kt`](src/main/kotlin/example/chat/ChatServer.kt) — grpc-java's server and channel, which
  stay grpc-java's business.
- [`ChatServiceTest.kt`](src/test/kotlin/example/chat/ChatServiceTest.kt) — every shape over a real socket.

```bash
./gradlew :examples:grpc:run
./gradlew :examples:grpc:test
```

## The four shapes

| proto | Kotlin | In this service |
|---|---|---|
| `rpc F(Req) returns (Resp)` | `suspend fun f(request: Req): Resp` | `SendMessage` |
| `rpc F(Req) returns (stream Resp)` | `fun f(request: Req): Flow<Resp>` | `Subscribe` |
| `rpc F(stream Req) returns (Resp)` | `suspend fun f(requests: Flow<Req>): Resp` | `ImportHistory` |
| `rpc F(stream Req) returns (stream Resp)` | `fun f(requests: Flow<Req>): Flow<Resp>` | `Converse` |

The two that return a `Flow` are **not** `suspend`: a `Flow` is cold, so building one does no work and
collecting it is what makes the call. `Subscribe` returns a flow that never completes — the subscription
ends when the client stops collecting, and the cancellation travels back over the RPC.

## What it shows

**Errors are statuses.** proto3 has no `required`, so validating a request is the service's job.
`InMemoryChatService` throws `StatusException(INVALID_ARGUMENT)` for a request with no message and for a
message with no room, and the client sees that status rather than an exception of the server's process.
The first of those two checks is only writable because `message` is a singular message field and therefore
nullable — "sent nothing" is a state the service can see rather than an empty one it cannot distinguish.

**Unimplemented methods answer `UNIMPLEMENTED`.** The generated `Server` implements every RPC that way, so
a service can ship one method at a time — while overriding the wrong shape, or a method the schema does not
declare, does not compile.

**The channel is yours.** `GrpcClientOption(channel)` wraps a channel you configured;
`DefaultGrpcClientOption(host, port, useTls)` builds a plaintext or TLS one for you. Transports, deadlines,
interceptors and TLS stay grpc-java's API — this project generates the stubs, not a replacement for it.
