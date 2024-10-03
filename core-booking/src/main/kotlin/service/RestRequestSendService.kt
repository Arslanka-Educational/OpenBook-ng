package org.example.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.openbook.model.BookInstanceResponse
import ru.openbook.model.BookReservationInitializationRequest
import java.util.*

@Service
class RestRequestSendService(
    private val restTemplate: RestTemplate,
) {
    private val logger = LoggerFactory.getLogger(RestRequestSendService::class.java)

    fun sendBookInstance(bookInstanceId: UUID, reservationId: UUID, userId: UUID): BookInstanceResponse? {
        try {
            val url = "http://core-catalog-app-1:8081/v1/books/$bookInstanceId/reservation"
            val requestBody = BookReservationInitializationRequest(
                externalId = reservationId,
                userId = userId
            )
            val response = restTemplate.postForEntity(url, requestBody, BookInstanceResponse::class.java)
            return response.body

        } catch (e: Exception) {
            e.printStackTrace()
            logger.error(e.message)
            throw e
        }
    }
}