package org.example.api

import org.example.client.CoreCatalogClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.openbook.api.BooksApi
import ru.openbook.model.BookContentResponse
import ru.openbook.model.BookInstanceResponse
import ru.openbook.model.BookReservationInitializationRequest
import ru.openbook.model.BookSearchResponse
import java.util.*

@RestController
class BookController(
    private val coreCatalogClient: CoreCatalogClient,
) : BooksApi {
    override fun getBookContentByTitle(title: String): ResponseEntity<List<BookContentResponse>> {
        TODO("Not yet implemented")
    }

    override fun getBookInstancesByBookContentId(bookContentId: String): ResponseEntity<List<BookInstanceResponse>> {
        TODO("Not yet implemented")
    }

    override fun getBooksContentById(bookContentId: String): ResponseEntity<List<BookContentResponse>> {
        TODO("Not yet implemented")
    }

    override fun initializeBookReservation(
        bookInstanceId: UUID,
        bookReservationInitializationRequest: BookReservationInitializationRequest
    ): ResponseEntity<BookInstanceResponse> {
        return ResponseEntity.ok(
            coreCatalogClient.initializeBookReservation(
                bookInstanceId,
                bookReservationInitializationRequest
            )
        )
    }

    override fun postBookInstance(bookContentId: UUID): ResponseEntity<BookInstanceResponse> {
        return ResponseEntity.ok(coreCatalogClient.postBookInstance(bookContentId))
    }

    override fun searchBooks(
        title: String?,
        author: String?,
        page: Int,
        pageSize: Int
    ): ResponseEntity<BookSearchResponse> {
        TODO("Not yet implemented")
    }
}