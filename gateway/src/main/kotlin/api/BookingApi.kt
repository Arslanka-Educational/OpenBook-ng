package org.example.api

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.function.ServerRequest.Headers
import ru.openbook.api.ReservationApi
import ru.openbook.model.BookReservationRequestResponse
import java.util.UUID

@RestController
class BookingApi(
    private val restTemplate: RestTemplate,
    @Value("\${spring.clients.core-booking.host}")
    private val clientHost: String,
) : ReservationApi {
    override fun getReservationRequest(
        xUid: UUID,
        reservationId: UUID,
    ): ResponseEntity<BookReservationRequestResponse> {
        val entity = HttpEntity(
            mapOf(
                "X-UID" to xUid.toString(),
            ),
        )

        return restTemplate.exchange(
            "$clientHost/v1/reservations/$reservationId",
            HttpMethod.GET,
            entity,
            BookReservationRequestResponse::class.java,
        )
    }

    override fun reserveBook(
        xIdempotencyToken: UUID,
        xUid: UUID,
        bookId: UUID,
    ): ResponseEntity<BookReservationRequestResponse> {
        val entity = HttpEntity(
            mapOf(
                "X-Idempotency-Token" to xIdempotencyToken.toString(),
                "X-UID" to xUid.toString(),
            ),
        )

        return restTemplate.exchange(
            "$clientHost/v1/booking/$bookId/reservation",
            HttpMethod.POST,
            entity,
            BookReservationRequestResponse::class.java,
        )
    }
}
