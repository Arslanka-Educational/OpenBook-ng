package org.example.api

import org.example.client.CoreBookingClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.openbook.api.ReservationApi
import ru.openbook.model.BookReservationRequestResponse
import java.util.*

@RestController
class ReservationController(
    private val coreBookingClient: CoreBookingClient,
) : ReservationApi{
    override fun getReservationRequest(
        xUid: UUID,
        reservationId: UUID
    ): ResponseEntity<BookReservationRequestResponse> {
        return ResponseEntity.ok(coreBookingClient.getReservationRequest(xUid, reservationId))
    }

    override fun reserveBook(
        xIdempotencyToken: UUID,
        xUid: UUID,
        bookId: UUID
    ): ResponseEntity<BookReservationRequestResponse> {
        return ResponseEntity.ok(coreBookingClient.reserveBook(xIdempotencyToken, xUid, bookId))
    }
}