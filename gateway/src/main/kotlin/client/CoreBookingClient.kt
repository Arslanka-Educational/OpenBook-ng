package org.example.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.openbook.model.BookReservationRequestResponse
import java.util.*

@Component
class CoreBookingClient(
    private val restTemplate: RestTemplate
) {
    @Value("\${spring.clients.core-booking.host}")
    lateinit var coreBookingHost: String

    @Value("\${spring.clients.core-booking.port}")
    lateinit var coreBookingPort: String

    fun getReservationRequest(xUid: UUID, reservationId: UUID): BookReservationRequestResponse? {
        val url = "$coreBookingHost:$coreBookingPort/v1/booking/reservation/$reservationId"
        val response = restTemplate.getForEntity(url, BookReservationRequestResponse::class.java)
        return response.body
    }

    fun reserveBook(xIdempotencyToken: UUID, xUid: UUID, bookId: UUID): BookReservationRequestResponse? {
        val url = "$coreBookingHost:$coreBookingPort/v1/booking/$bookId/reservation"
        val headers = HttpHeaders().apply {
            add(X_IDEMPOTENCY_TOKEN_HEADER, xIdempotencyToken.toString())
            add(X_UID_HEADER, xUid.toString())
        }
        val entity = HttpEntity<Any>(null, headers)
        val response = restTemplate.postForEntity(url, entity, BookReservationRequestResponse::class.java)
        return response.body
    }

    private companion object {
        const val X_IDEMPOTENCY_TOKEN_HEADER = "X-Idempotency-Token"
        const val X_UID_HEADER = "X-Uid"
    }
}