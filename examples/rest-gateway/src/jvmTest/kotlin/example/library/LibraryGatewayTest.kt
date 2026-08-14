package example.library

import example.library.v1.Book
import example.library.v1.CreateBookRequest
import example.library.v1.DeleteBookRequest
import example.library.v1.GetBookRequest
import example.library.v1.ListBooksRequest
import example.library.v1.grpc.gateway.LibraryGrpcGateway
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import kim.jade.kotlinx.protobuf.grpc.gateway.GrpcGatewayClientOption
import kim.jade.kotlinx.protobuf.grpc.gateway.ktor.GrpcGatewayClient
import kim.jade.kotlinx.protobuf.grpc.gateway.ktor.GrpcGatewayClientConfigVariables

/**
 * The REST client the `google.api.http` options produced.
 *
 * Two halves. The first uses Ktor's MockEngine, so the request the client built can be inspected — which
 * is the only way to check that a path template was filled, that the leftover fields became query
 * parameters and that `body: "book"` sent the book rather than the request. The second runs the same
 * calls against [LibraryServer] over a real socket, where a mistake in either direction shows up as a
 * wrong answer instead of a wrong-looking request.
 */
class LibraryGatewayTest : StringSpec({

    /** Answers every call with [responseBody] and remembers what was asked. */
    class Recorder(private val responseBody: String) {
        lateinit var request: HttpRequestData
            private set

        val client = GrpcGatewayClient(
            MockEngine,
            GrpcGatewayClientConfigVariables("https://library.example.com"),
        ) {
            engine {
                addHandler { data ->
                    request = data
                    respond(
                        content = responseBody,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", ContentType.Application.Json.toString()),
                    )
                }
            }
        }

        val library = LibraryGrpcGateway.createClient(GrpcGatewayClientOption(client))
    }

    "fills the path template and sends nothing else" {
        val recorder = Recorder("""{"id":"1","title":"Solaris","author":"Lem","year":1961}""")

        val book = recorder.library.getBook(GetBookRequest(shelf = "sci-fi", book = "1"))

        // get: "/v1/shelves/{shelf}/books/{book}" — both fields are bound by the path…
        recorder.request.method shouldBe HttpMethod.Get
        recorder.request.url.encodedPath shouldBe "/v1/shelves/sci-fi/books/1"
        // …so nothing is left to put in the query string, and a GET carries no body.
        recorder.request.url.parameters.isEmpty() shouldBe true
        recorder.request.body shouldBe EmptyContent

        book.title shouldBe "Solaris"
        book.year shouldBe 1961u
    }

    "turns the fields the path does not bind into query parameters" {
        val recorder = Recorder("""{"books":[]}""")

        recorder.library.listBooks(
            ListBooksRequest(shelf = "sci-fi", pageSize = 5u, tag = listOf("classic", "translated")),
        )

        recorder.request.url.encodedPath shouldBe "/v1/shelves/sci-fi/books"
        recorder.request.url.parameters["page_size"] shouldBe "5"
        // A repeated scalar becomes a repeated parameter rather than a joined string.
        recorder.request.url.parameters.getAll("tag") shouldContainExactly listOf("classic", "translated")
        // shelf went into the path, so it must not also be sent as a parameter.
        recorder.request.url.parameters["shelf"] shouldBe null
    }

    "sends only the field named by body" {
        val recorder = Recorder("""{"id":"3","title":"Hard to Be a God"}""")

        recorder.library.createBook(
            CreateBookRequest(
                shelf = "sci-fi",
                book = Book(title = "Hard to Be a God", author = "Strugatsky", year = 1964u),
            ),
        )

        // post: "/v1/shelves/{shelf}/books" with body: "book"
        recorder.request.method shouldBe HttpMethod.Post
        recorder.request.url.encodedPath shouldBe "/v1/shelves/sci-fi/books"

        val body = (recorder.request.body.shouldNotBeNull() as TextContent).text
        // The body is the Book, in protobuf's JSON mapping…
        body shouldContain "\"title\": \"Hard to Be a God\""
        // …and not the request message, so the shelf appears in the URL only.
        body shouldNotContain "shelf"
    }

    "reaches an additional binding as a method of its own" {
        val recorder = Recorder("""{"id":"1","title":"Selected Poems"}""")

        // The first binding is the interface's method; the rest land beside it in declaration order. This
        // one is post: "/v1/books" with body: "*".
        recorder.library.createBookBinding2(
            CreateBookRequest(shelf = "poetry", book = Book(title = "Selected Poems")),
        )

        recorder.request.url.encodedPath shouldBe "/v1/books"
        val body = (recorder.request.body.shouldNotBeNull() as TextContent).text
        // body: "*" is the whole request message, so this time the shelf *is* in the body.
        body shouldContain "\"shelf\": \"poetry\""
        body shouldContain "\"title\": \"Selected Poems\""
    }

    "decodes an Empty response" {
        val recorder = Recorder("{}")

        recorder.library.deleteBook(DeleteBookRequest(shelf = "sci-fi", book = "2"))

        recorder.request.method shouldBe HttpMethod.Delete
        recorder.request.url.encodedPath shouldBe "/v1/shelves/sci-fi/books/2"
    }

    "carries every call over a real socket" {
        LibraryServer().use { server ->
            val http = GrpcGatewayClient(
                CIO,
                GrpcGatewayClientConfigVariables("http://localhost:${server.port}"),
            )

            try {
                val library = LibraryGrpcGateway.createClient(GrpcGatewayClientOption(http))

                library.getBook(GetBookRequest(shelf = "sci-fi", book = "1")).title shouldBe "Solaris"

                val created = library.createBook(
                    CreateBookRequest(
                        shelf = "sci-fi",
                        book = Book(title = "Hard to Be a God", author = "Strugatsky", year = 1964u),
                    ),
                )
                created.id shouldBe "3"
                created.year shouldBe 1964u

                library.listBooks(ListBooksRequest(shelf = "sci-fi", pageSize = 2u)).books.size shouldBe 2
                library.listBooks(
                    ListBooksRequest(shelf = "sci-fi", tag = listOf("picnic")),
                ).books.single().title shouldBe "Roadside Picnic"

                library.deleteBook(DeleteBookRequest(shelf = "sci-fi", book = "2"))
                library.listBooks(ListBooksRequest(shelf = "sci-fi")).books.map { it.id } shouldContainExactly
                    listOf("1", "3")
            } finally {
                http.close()
            }
        }
    }

    "surfaces a gateway error instead of an empty message" {
        LibraryServer().use { server ->
            val http = GrpcGatewayClient(
                CIO,
                GrpcGatewayClientConfigVariables("http://localhost:${server.port}"),
            )

            try {
                val library = LibraryGrpcGateway.createClient(GrpcGatewayClientOption(http))

                // expectSuccess is on in GrpcGatewayClient, so a 404 has to be an exception: decoding the
                // error body as a Book would hand the caller a book with no title and no error.
                val failure = runCatching {
                    library.getBook(GetBookRequest(shelf = "sci-fi", book = "99"))
                }.exceptionOrNull()

                failure.shouldNotBeNull()
                failure.message.shouldNotBeNull() shouldContain "404"
            } finally {
                http.close()
            }
        }
    }
})
