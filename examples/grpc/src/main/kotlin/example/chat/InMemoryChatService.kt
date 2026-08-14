package example.chat

import example.chat.v1.ChatEvent
import example.chat.v1.ChatMessage
import example.chat.v1.ImportSummary
import example.chat.v1.Joined
import example.chat.v1.SendMessageRequest
import example.chat.v1.SendMessageResponse
import example.chat.v1.SubscribeRequest
import example.chat.v1.grpc.ChatServiceGrpc
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * The service, implemented against the generated interface.
 *
 * `ChatServiceGrpc.Server` is an abstract class whose unimplemented methods answer `UNIMPLEMENTED`, so a
 * server can ship one RPC at a time — but overriding the wrong shape does not compile, and neither does
 * a method the schema does not declare. Nothing below mentions gRPC: no `StreamObserver`, no
 * `MethodDescriptor`, no marshaller. Those are generated, and the four functions here are the service.
 */
class InMemoryChatService : ChatServiceGrpc.Server() {

    private val lock = Mutex()
    private val rooms = mutableMapOf<String, MutableList<ChatMessage>>()
    private val published = MutableSharedFlow<ChatMessage>(extraBufferCapacity = 64)

    /** How many subscriptions are open, so a caller can wait until the server is really listening. */
    val subscriberCount: Int get() = published.subscriptionCount.value

    /** Unary: one request in, one response out. Called as a plain `suspend` function. */
    override suspend fun sendMessage(request: SendMessageRequest): SendMessageResponse {
        // protobuf has no way to say "required" in proto3, so validation is the service's job. Throwing
        // a StatusException is how a gRPC service reports it, and the client sees that status rather than
        // an exception of this process's own. Presence is what makes the first of these two checks
        // possible at all: a singular message field is nullable, so "sent no message" is a state the
        // service can see rather than an empty one it cannot tell from a real one.
        val message = request.message
            ?: throw StatusException(Status.INVALID_ARGUMENT.withDescription("message is required"))

        if (message.room.isEmpty()) {
            throw StatusException(Status.INVALID_ARGUMENT.withDescription("room is required"))
        }

        val size = lock.withLock {
            val room = rooms.getOrPut(message.room) { mutableListOf() }
            room += message
            room.size
        }
        published.emit(message)

        return SendMessageResponse(
            messageId = "${message.room}-$size",
            roomSize = size.toULong(),
        )
    }

    /**
     * Server streaming: the response is a cold [Flow], so nothing happens until the caller collects it.
     *
     * The flow does not complete. A subscription ends when the client stops collecting — cancellation
     * travels back over the RPC and this coroutine is cancelled with it.
     */
    override fun subscribe(request: SubscribeRequest): Flow<ChatEvent> = flow {
        val backlog = lock.withLock { rooms[request.room]?.toList().orEmpty() }

        emit(ChatEvent(ChatEvent.EventOneOf.Joined(Joined(request.room, backlog.size.toULong()))))

        if (request.includeHistory) {
            backlog.forEach { emit(ChatEvent(ChatEvent.EventOneOf.Message(it))) }
        }

        published
            .filter { it.room == request.room }
            .collect { emit(ChatEvent(ChatEvent.EventOneOf.Message(it))) }
    }

    /**
     * Client streaming: a `suspend` function over the incoming [Flow]. It returns once the client has
     * closed its side, which is what makes one summary the right answer for many requests.
     */
    override suspend fun importHistory(requests: Flow<ChatMessage>): ImportSummary =
        requests
            .fold(ImportSummary()) { summary, message ->
                lock.withLock { rooms.getOrPut(message.room) { mutableListOf() } += message }
                ImportSummary(
                    imported = summary.imported + 1uL,
                    rooms = (summary.rooms + message.room).distinct(),
                )
            }

    /**
     * Bidirectional streaming: both directions open at once. Mapping the request flow is the whole
     * implementation — a reply is produced per message rather than after the last one.
     */
    override fun converse(requests: Flow<ChatMessage>): Flow<ChatMessage> = requests.map { incoming ->
        ChatMessage(
            room = incoming.room,
            author = "bot",
            text = "heard \"${incoming.text}\" from ${incoming.author}",
            sentAt = incoming.sentAt,
        )
    }
}
