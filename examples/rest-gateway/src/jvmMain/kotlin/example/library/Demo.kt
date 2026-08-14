package example.library

import example.library.v1.Book
import example.library.v1.CreateBookRequest
import example.library.v1.DeleteBookRequest
import example.library.v1.GetBookRequest
import example.library.v1.ListBooksRequest
import example.library.v1.grpc.gateway.LibraryGrpcGateway
import io.ktor.client.engine.cio.CIO
import kim.jade.kotlinx.protobuf.grpc.gateway.GrpcGatewayClientOption
import kim.jade.kotlinx.protobuf.grpc.gateway.ktor.GrpcGatewayClient
import kim.jade.kotlinx.protobuf.grpc.gateway.ktor.GrpcGatewayClientConfigVariables
import kotlinx.coroutines.runBlocking

/**
 * The generated REST client against the routes `library.proto` declares.
 *
 * Run it with `./gradlew :examples:rest-gateway:run`.
 */
fun main() = runBlocking {
    LibraryServer().use { server ->
        // GrpcGatewayClient is a Ktor HttpClient with the base URL and this project's body converter
        // installed — protobuf's JSON mapping in both directions, and expectSuccess on so a gateway
        // error is an exception rather than a default-valued message.
        val http = GrpcGatewayClient(
            CIO,
            GrpcGatewayClientConfigVariables("http://localhost:${server.port}"),
        )

        try {
            // createClient returns the concrete client, not the service interface — which is how the
            // additional bindings stay reachable.
            val library = LibraryGrpcGateway.createClient(GrpcGatewayClientOption(http))
            println("gateway on http://localhost:${server.port}")

            // GET /v1/shelves/sci-fi/books/1 — both fields bound by the path, so no body, no query.
            println()
            println("GetBook")
            val book = library.getBook(GetBookRequest(shelf = "sci-fi", book = "1"))
            println("  ${book.title} — ${book.author} (${book.year})")

            // GET /v1/shelves/sci-fi/books?page_size=1&tag=picnic — the fields the path does not bind.
            println()
            println("ListBooks")
            val page = library.listBooks(
                ListBooksRequest(shelf = "sci-fi", pageSize = 5u, tag = listOf("picnic")),
            )
            page.books.forEach { println("  ${it.id}  ${it.title}") }

            // POST /v1/shelves/sci-fi/books with `body: "book"` — the body is the Book alone, and the
            // shelf field is not repeated into it.
            println()
            println("CreateBook (body: \"book\")")
            val created = library.createBook(
                CreateBookRequest(
                    shelf = "sci-fi",
                    book = Book(title = "Hard to Be a God", author = "Strugatsky", year = 1964u),
                ),
            )
            println("  stored as id ${created.id}")

            // POST /v1/books with `body: "*"` — the same RPC, reached through its additional binding, so
            // the whole request message goes in the body.
            println()
            println("CreateBook (additional binding, body: \"*\")")
            val alternative = library.createBookBinding2(
                CreateBookRequest(
                    shelf = "poetry",
                    book = Book(title = "Selected Poems", author = "Szymborska", year = 1996u),
                ),
            )
            println("  stored as id ${alternative.id} on a new shelf")

            // DELETE /v1/shelves/sci-fi/books/2 — the response is google.protobuf.Empty, i.e. {}.
            println()
            println("DeleteBook")
            library.deleteBook(DeleteBookRequest(shelf = "sci-fi", book = "2"))
            // pageSize is left unset here, which reaches the server as `page_size=0` — implicit presence
            // has no way to say "unset". The server reads 0 as "you decide", which is what every paged
            // API ends up doing.
            println("  deleted; shelf now holds ${library.listBooks(ListBooksRequest(shelf = "sci-fi")).books.size}")

            // And an error, because a REST client that turns a 404 into an empty message is worse than
            // one that fails.
            println()
            println("GetBook, missing")
            val failure = runCatching { library.getBook(GetBookRequest(shelf = "sci-fi", book = "99")) }
            println("  ${failure.exceptionOrNull()?.message?.lineSequence()?.first()}")
        } finally {
            http.close()
        }
    }
}
