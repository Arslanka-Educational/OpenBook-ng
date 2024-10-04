package org.example.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.openbook.model.BookInstanceResponse
import ru.openbook.model.BookReservationInitializationRequest
import java.util.UUID

@Service
class CoreCatalogAdapterService(
    private val restTemplate: RestTemplate,
    @Value("\${spring.clients.core-catalog.url}")
    private val clientUrl: String,
) {

    internal fun reserveBook(bookId: UUID, reservationId: UUID, userId: UUID): BookInstanceResponse? {
        val url = "$clientUrl/v1/books/$bookId/reservation"
        val requestBody = BookReservationInitializationRequest(
            externalId = reservationId,
            userId = userId
        )
        return restTemplate.postForEntity(url, requestBody, BookInstanceResponse::class.java).body
    }
}