package org.example.api

import org.example.model.BookInstance
import org.example.service.BookInstanceService
import org.springframework.http.ResponseEntity
import ru.openbook.api.BooksApi
import ru.openbook.model.BookContentResponse
import ru.openbook.model.BookInstanceResponse
import ru.openbook.model.BookReservationInitializationRequest
import java.util.*

class BooksController(
    private val bookInstanceService: BookInstanceService,
) : BooksApi {
    override fun getBookContentByTitle(title: String): ResponseEntity<List<BookContentResponse>> {
        TODO("Not yet implemented")
    }

    override fun getBooksContentById(bookContentId: String): ResponseEntity<List<BookContentResponse>> {
        TODO("Not yet implemented")
    }

    override fun initializeBookReservation(
        bookInstanceId: UUID,
        bookReservationInitializationRequest: BookReservationInitializationRequest
    ): ResponseEntity<BookInstanceResponse> {
        val bookInstance: BookInstance? = bookInstanceService.initializeBookInstanceReservation(bookInstanceId)
        return bookInstance?.let {
            ResponseEntity.ok(bookInstance.mapToResponse())
        } ?: ResponseEntity.notFound().build();
    }

    override fun getBookInstancesByBookContentId(bookContentId: String): ResponseEntity<List<BookInstanceResponse>> {
        TODO("Not yet implemented")
    }
}