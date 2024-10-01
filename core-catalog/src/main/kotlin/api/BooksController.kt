package org.example.api

import org.example.kafka.Producer
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.openbook.api.BooksApi
import ru.openbook.model.BookContentResponse
import ru.openbook.model.BookInstanceResponse
import java.util.*

@RestController
class BooksController(
    private val producer: Producer,
) : BooksApi {
    override fun getBookContentByTitle(title: String): ResponseEntity<List<BookContentResponse>> {
        producer.sendMessage("my-topic", "my message")
        println("sended message")
        return ResponseEntity.ok(emptyList())
//        TODO("Not yet implemented")
    }

    override fun getBooksContentById(bookContentId: String): ResponseEntity<List<BookContentResponse>> {
        TODO("Not yet implemented")
    }

    override fun getBookInstancesByBookContentId(bookContentId: String): ResponseEntity<List<BookInstanceResponse>> {
        TODO("Not yet implemented")
    }

    override fun initializeBookReservation(bookInstanceId: UUID): ResponseEntity<BookInstanceResponse> {
        TODO("Not yet implemented")
    }
}