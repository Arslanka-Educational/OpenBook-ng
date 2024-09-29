package org.example.controller

import ReservationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.openbook.api.ReservationApi
import ru.openbook.model.BookReservationRequestResponse
import ru.openbook.model.BookReservationResponse
import java.util.UUID

@RestController
class ReservationController(
    private val reservationService: ReservationService,
) : ReservationApi {
    override fun getReservationRequest(
        xUid: UUID,
        reservationId: UUID,
    ): ResponseEntity<BookReservationRequestResponse> {
        return reservationService.getReservationRequest(
            uid = xUid,
            reservationId = reservationId,
        ).let {
            ResponseEntity.ok(it.toDto())
        }
    }

    override fun reserveBook(
        xIdempotencyToken: String,
        xUid: UUID,
        bookId: UUID,
    ): ResponseEntity<BookReservationResponse> {
        TODO("Not yet implemented")
    }
}