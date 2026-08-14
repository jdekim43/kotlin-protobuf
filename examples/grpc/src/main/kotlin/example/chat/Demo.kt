package example.chat

import example.chat.v1.ChatEvent
import example.chat.v1.ChatMessage
import example.chat.v1.SendMessageRequest
import example.chat.v1.SubscribeRequest
import google.protobuf.Timestamp
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

/**
 * All four RPC shapes over a socket, server and client in one process.
 *
 * Run it with `./gradlew :examples:grpc:run`.
 */
fun main() = runBlocking {
    ChatServer().use { server ->
        val client = server.client()
        println("server listening on ${server.port}")

        // --- Unary. A suspend call, and the response is a message like any other.
        println()
        println("unary — SendMessage")
        listOf("has anyone tried the new grinder?", "it is loud").forEach { text ->
            val response = client.sendMessage(
                SendMessageRequest(
                    ChatMessage(
                        room = "general",
                        author = "amelie",
                        text = text,
                        sentAt = Timestamp(seconds = 1_755_100_800L),
                    ),
                ),
            )
            println("  ${response.messageId}  room now holds ${response.roomSize}")
        }

        // --- Server streaming. The Flow is cold: nothing is sent until it is collected, and collecting
        // --- `take(3)` cancels the subscription — which travels back to the server as a cancelled RPC.
        println()
        println("server streaming — Subscribe with history")
        client.subscribe(SubscribeRequest(room = "general", includeHistory = true))
            .take(3)
            .toList()
            .forEach { println("  ${it.describe()}") }

        // --- Server streaming, live. Same call, no history: the events arrive as they are published.
        println()
        println("server streaming — Subscribe, live")
        coroutineScope {
            val events = async {
                client.subscribe(SubscribeRequest(room = "general")).take(2).toList()
            }
            // Wait for the subscription to reach the server before publishing, or the message races the
            // Subscribe call and the demo prints nothing.
            withTimeout(2_000) { while (server.service.subscriberCount == 0) delay(10) }

            client.sendMessage(
                SendMessageRequest(ChatMessage(room = "general", author = "theo", text = "buy earplugs")),
            )
            events.await().forEach { println("  ${it.describe()}") }
        }

        // --- Client streaming. Many messages up, one summary back when the client closes its side.
        println()
        println("client streaming — ImportHistory")
        val archive = listOf(
            ChatMessage(room = "archive-2024", author = "amelie", text = "first"),
            ChatMessage(room = "archive-2024", author = "theo", text = "second"),
            ChatMessage(room = "archive-2023", author = "amelie", text = "older"),
        )
        val summary = client.importHistory(archive.asFlow())
        println("  imported ${summary.imported} messages into ${summary.rooms}")

        // --- Bidirectional streaming. Both directions open at once; the reply to the first message
        // --- arrives while the client is still sending the rest.
        println()
        println("bidirectional streaming — Converse")
        client.converse(
            listOf(
                ChatMessage(room = "general", author = "amelie", text = "morning"),
                ChatMessage(room = "general", author = "theo", text = "coffee?"),
            ).asFlow(),
        ).toList().forEach { println("  ${it.author}: ${it.text}") }
    }
}

private fun ChatEvent.describe(): String = when (val event = event) {
    is ChatEvent.EventOneOf.Joined -> "joined ${event.value.room}, backlog ${event.value.backlog}"
    is ChatEvent.EventOneOf.Message -> "${event.value.author}: ${event.value.text}"
    null -> "an event with nothing set"
}
