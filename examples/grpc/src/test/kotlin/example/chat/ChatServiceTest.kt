package example.chat

import example.chat.v1.ChatEvent
import example.chat.v1.ChatMessage
import example.chat.v1.SendMessageRequest
import example.chat.v1.SubscribeRequest
import example.chat.v1.grpc.ChatServiceGrpc
import io.grpc.ServerBuilder
import io.grpc.Status
import io.grpc.StatusException
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kim.jade.kotlinx.protobuf.grpc.DefaultGrpcClientOption
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withTimeout

/**
 * The four RPC shapes over a real socket.
 *
 * Nothing here is stubbed except the service body: the calls go through the generated client, grpc-java's
 * marshallers, this project's converters and the generated server. A codec that is wrong is visible as a
 * wrong message, and a streaming shape that is wired to the unary entry point does not carry its stream.
 */
class ChatServiceTest : StringSpec({

    fun message(text: String, room: String = "general", author: String = "amelie") =
        ChatMessage(room = room, author = author, text = text)

    "carries a unary call and its response" {
        ChatServer().use { server ->
            val client = server.client()

            val first = client.sendMessage(SendMessageRequest(message("morning")))
            val second = client.sendMessage(SendMessageRequest(message("coffee?")))

            first.messageId shouldBe "general-1"
            second.roomSize shouldBe 2uL
        }
    }

    "streams a room's history and then follows it live" {
        ChatServer().use { server ->
            val client = server.client()
            client.sendMessage(SendMessageRequest(message("morning")))

            val events = client.subscribe(SubscribeRequest(room = "general", includeHistory = true))
                .take(2)
                .toList()

            // The first event is always the join, so a subscriber knows how much backlog to expect.
            events[0].event.shouldBeInstanceOf<ChatEvent.EventOneOf.Joined>().value.backlog shouldBe 1uL
            events[1].event.shouldBeInstanceOf<ChatEvent.EventOneOf.Message>().value.text shouldBe "morning"
        }
    }

    "pushes a message published after the subscription started" {
        ChatServer().use { server ->
            val client = server.client()

            coroutineScope {
                val events = async {
                    client.subscribe(SubscribeRequest(room = "general")).take(2).toList()
                }
                // The flow is cold, so the RPC only exists once `async` starts collecting. Publishing
                // before the server has the subscription would be a race, not a test.
                withTimeout(5_000) { while (server.service.subscriberCount == 0) delay(10) }

                client.sendMessage(SendMessageRequest(message("buy earplugs", author = "theo")))

                val received = events.await()
                received[0].event.shouldBeInstanceOf<ChatEvent.EventOneOf.Joined>()
                received[1].event
                    .shouldBeInstanceOf<ChatEvent.EventOneOf.Message>()
                    .value.author shouldBe "theo"
            }
        }
    }

    "answers a client-streamed upload with one summary" {
        ChatServer().use { server ->
            val client = server.client()

            val summary = client.importHistory(
                listOf(
                    message("first", room = "archive-2024"),
                    message("second", room = "archive-2024"),
                    message("older", room = "archive-2023"),
                ).asFlow(),
            )

            summary.imported shouldBe 3uL
            summary.rooms shouldContainExactly listOf("archive-2024", "archive-2023")
        }
    }

    "replies to each message of a bidirectional stream" {
        ChatServer().use { server ->
            val client = server.client()

            val replies = client.converse(
                listOf(message("morning"), message("coffee?")).asFlow(),
            ).toList()

            replies.map { it.text } shouldContainExactly listOf(
                "heard \"morning\" from amelie",
                "heard \"coffee?\" from amelie",
            )
            replies.forEach { it.author shouldBe "bot" }
        }
    }

    "reports a service-side status rather than a local exception" {
        ChatServer().use { server ->
            val client = server.client()

            val failure = runCatching {
                client.sendMessage(SendMessageRequest(message("no room", room = "")))
            }.exceptionOrNull()

            failure.shouldNotBeNull()
            val status = Status.fromThrowable(failure)
            status.code shouldBe Status.Code.INVALID_ARGUMENT
            status.description shouldBe "room is required"
        }
    }

    "answers UNIMPLEMENTED for an RPC the server has not overridden" {
        // The generated Server implements every method as UNIMPLEMENTED, so a service can ship one RPC
        // at a time without the schema and the code having to be updated in lockstep.
        val empty = object : ChatServiceGrpc.Server() {}
        val server = ServerBuilder.forPort(0).addService(empty).build().start()

        try {
            // DefaultGrpcClientOption is the shortcut for the common case: build a channel to a host and
            // port, plaintext unless asked for TLS.
            val client = ChatServiceGrpc.createClient(
                DefaultGrpcClientOption("localhost", server.port),
            )

            val failure = runCatching {
                client.sendMessage(SendMessageRequest(message("hello")))
            }.exceptionOrNull()

            failure.shouldBeInstanceOf<StatusException>()
            failure.status.code shouldBe Status.Code.UNIMPLEMENTED
        } finally {
            server.shutdownNow()
        }
    }
})
