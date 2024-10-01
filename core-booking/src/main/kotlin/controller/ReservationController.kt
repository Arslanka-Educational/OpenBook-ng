package org.example.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.openbook.api.ReservationApi
import ru.openbook.model.BookReservationRequestResponse
import org.example.service.ReservationService
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
            ResponseEntity.ok(it.toReservationRequestDto())
        }
    }

    override fun reserveBook(
        xIdempotencyToken: UUID,
        xUid: UUID,
        bookId: UUID,
    ): ResponseEntity<BookReservationRequestResponse> {
        return reservationService.reserveBook(
            uid = xUid,
            reservationId = xIdempotencyToken,
            bookId = bookId,
        ).let {
            ResponseEntity.ok(it.toReservationRequestDto())
        }
    }
}