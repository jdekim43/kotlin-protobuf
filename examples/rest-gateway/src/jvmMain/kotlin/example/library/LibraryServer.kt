package example.library

import example.library.v1.Book
import example.library.v1.CreateBookRequest
import example.library.v1.ListBooksResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.request.receiveText
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kim.jade.kotlinx.protobuf.serialization.ProtobufJsonFormat
import kotlinx.coroutines.runBlocking
import io.ktor.server.cio.CIO as ServerCIO

/**
 * The REST surface `library.proto` describes, served by hand.
 *
 * In a deployment this is not code anybody writes: a gateway reads the same `google.api.http` options out
 * of the descriptor set and transcodes to gRPC. It is written out here so the example is one process and
 * one `./gradlew run`, and because seeing both halves side by side is the point — every route below is
 * dictated by an annotation in the proto, and the generated client derives the same routes from the same
 * annotations.
 */
class LibraryServer(port: Int = 0) : AutoCloseable {

    private val books = mutableMapOf<String, MutableList<Book>>(
        "sci-fi" to mutableListOf(
            Book(id = "1", title = "Solaris", author = "Stanisław Lem", year = 1961u),
            Book(id = "2", title = "Roadside Picnic", author = "Strugatsky", year = 1972u),
        ),
    )

    // The same JSON mapping the client uses, so both ends read and write protobuf's JSON rather than
    // Kotlin's — camelCase members, uint32 as a number, uint64 as a string.
    private val format = ProtobufJsonFormat()

    private val server: EmbeddedServer<*, *> = embeddedServer(ServerCIO, port = port) {
        routing {
            // rpc GetBook — option (google.api.http).get = "/v1/shelves/{shelf}/books/{book}"
            get("/v1/shelves/{shelf}/books/{book}") {
                val shelf = call.parameters["shelf"].orEmpty()
                val id = call.parameters["book"].orEmpty()
                val book = books[shelf]?.firstOrNull { it.id == id }

                if (book == null) {
                    // A gateway reports a gRPC status as an HTTP one; NOT_FOUND (5) is a 404. The client
                    // has expectSuccess on, so this surfaces as an exception rather than as an empty Book.
                    call.notFound("no book $id on shelf $shelf")
                } else {
                    call.respondJson(format.encodeToString(Book.KotlinxSerializer, book))
                }
            }

            // rpc ListBooks — the fields the path does not bind arrive as query parameters, under their
            // proto names.
            get("/v1/shelves/{shelf}/books") {
                val shelf = call.parameters["shelf"].orEmpty()
                // page_size has implicit presence, so the client cannot distinguish "no preference" from
                // zero — an unset uint32 arrives as `page_size=0`. Treating 0 as "the server decides" is
                // what Google's own API guidelines settle on, and why a page size of 0 is never a
                // legitimate request.
                val pageSize = call.request.queryParameters["page_size"]
                    ?.toIntOrNull()
                    ?.takeIf { it > 0 }
                    ?: 50
                val tags = call.request.queryParameters.getAll("tag").orEmpty()

                val page = books[shelf].orEmpty()
                    .filter { tags.isEmpty() || tags.any { tag -> it.title.contains(tag, ignoreCase = true) } }
                    .take(pageSize)

                call.respondJson(
                    format.encodeToString(
                        ListBooksResponse.KotlinxSerializer,
                        ListBooksResponse(books = page),
                    ),
                )
            }

            // rpc CreateBook, first binding — `body: "book"`, so the body is the Book and the shelf is
            // in the path.
            post("/v1/shelves/{shelf}/books") {
                val shelf = call.parameters["shelf"].orEmpty()
                val book = format.decodeFromString(Book.KotlinxSerializer, call.receiveText())

                call.respondJson(format.encodeToString(Book.KotlinxSerializer, store(shelf, book)))
            }

            // rpc CreateBook, additional binding — `body: "*"`, so the body is the whole request message
            // and the URL carries nothing.
            post("/v1/books") {
                val request =
                    format.decodeFromString(CreateBookRequest.KotlinxSerializer, call.receiveText())
                // `book` is a singular message, so a body that omits it is absent rather than empty and
                // the server gets to say so instead of storing a blank book.
                val book = request.book

                if (book == null) {
                    call.invalidArgument("book is required")
                    return@post
                }

                call.respondJson(
                    format.encodeToString(Book.KotlinxSerializer, store(request.shelf, book)),
                )
            }

            // rpc DeleteBook — returns google.protobuf.Empty, which JSON-maps to {}.
            delete("/v1/shelves/{shelf}/books/{book}") {
                val shelf = call.parameters["shelf"].orEmpty()
                val id = call.parameters["book"].orEmpty()
                val removed = books[shelf]?.removeIf { it.id == id } ?: false

                if (removed) call.respondJson("{}") else call.notFound("no book $id on shelf $shelf")
            }
        }
    }.start(wait = false)

    /** The port actually bound; 0 asks the OS for a free one. */
    val port: Int = runBlocking { server.engine.resolvedConnectors().first().port }

    private fun store(shelf: String, book: Book): Book {
        val shelved = books.getOrPut(shelf) { mutableListOf() }
        val stored = if (book.id.isEmpty()) book.copy(id = "${shelved.size + 1}") else book
        shelved += stored
        return stored
    }

    override fun close() {
        server.stop(gracePeriodMillis = 0, timeoutMillis = 0)
    }
}

private suspend fun ApplicationCall.respondJson(body: String) {
    respondText(body, ContentType.Application.Json)
}

/** The shape a gateway gives a gRPC status: the code, a message, and the matching HTTP status. */
private suspend fun ApplicationCall.notFound(message: String) {
    respondText(
        """{"code":5,"message":"$message"}""",
        ContentType.Application.Json,
        HttpStatusCode.NotFound,
    )
}

private suspend fun ApplicationCall.invalidArgument(message: String) {
    respondText(
        """{"code":3,"message":"$message"}""",
        ContentType.Application.Json,
        HttpStatusCode.BadRequest,
    )
}
