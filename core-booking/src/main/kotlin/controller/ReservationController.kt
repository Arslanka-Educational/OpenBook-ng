package org.example.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.openbook.api.ReservationApi
import ru.openbook.model.BookReservationRequestResponse
import ru.openbook.model.BookReservationResponse
import java.util.UUID

@RestController
class ReservationController(
) : ReservationApi {
    override fun getReservationRequest(
        xUid: UUID,
        reservationId: String,
    ): ResponseEntity<BookReservationRequestResponse> {
        TODO()
    }

    override fun reserveBook(
        xIdempotencyToken: String,
        xUid: UUID,
        bookId: UUID,
    ): ResponseEntity<BookReservationResponse> {
        TODO("Not yet implemented")
    }
}