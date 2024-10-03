package org.example.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.openbook.api.ReservationApi
import ru.openbook.model.BookReservationRequestResponse
import java.util.*

@RestController
class ReservationController(

) : ReservationApi{
    override fun getReservationRequest(
        xUid: UUID,
        reservationId: UUID
    ): ResponseEntity<BookReservationRequestResponse> {
        TODO("Not yet implemented")
    }

    override fun reserveBook(
        xIdempotencyToken: UUID,
        xUid: UUID,
        bookId: UUID
    ): ResponseEntity<BookReservationRequestResponse> {
        TODO("Not yet implemented")
    }
}