package org.example.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.openbook.model.BookContentResponse
import ru.openbook.model.BookInstanceResponse
import ru.openbook.model.BookReservationInitializationRequest
import java.util.UUID

@Component
class CoreCatalogClient(
    private val restTemplate: RestTemplate,
) {
    @Value("\${spring.clients.core-catalog.host}")
    lateinit var coreCatalogHost: String

    @Value("\${spring.clients.core-catalog.port}")
    lateinit var coreCatalogPort: String

    fun getBookContentByTitle(title: String): List<BookContentResponse> {
        TODO("Not yet implemented")
    }

    fun getBooksContentById(bookContentId: String): List<BookContentResponse> {
        TODO("Not yet implemented")
    }

    fun initializeBookReservation(
        bookInstanceId: UUID,
        bookReservationInitializationRequest: BookReservationInitializationRequest
    ): BookInstanceResponse? {
        val url = "$coreCatalogHost:$coreCatalogPort/v1/books/$bookInstanceId/reservation"
        val response = restTemplate.postForEntity(url, bookReservationInitializationRequest, BookInstanceResponse::class.java)
        return response.body
    }

    fun postBookInstance(bookContentId: UUID): BookInstanceResponse? {
        val url = "$coreCatalogHost:$coreCatalogPort/v1/books/$bookContentId/instance"
        val response = restTemplate.postForEntity(url, null, BookInstanceResponse::class.java)
        return response.body
    }

    fun getBookInstancesByBookContentId(bookContentId: String): List<BookInstanceResponse> {
        TODO("Not yet implemented")
    }

}