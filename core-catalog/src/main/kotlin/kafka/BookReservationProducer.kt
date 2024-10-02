package org.example.kafka

import org.example.model.BookInstance
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class BookReservationProducer(
    private val kafkaTemplate: KafkaTemplate<String, BookInstance>
) {

    fun sendMessage(bookInstance: BookInstance) {
        kafkaTemplate.send(BOOK_RESERVATION_TOPIC, bookInstance.id.toString(), bookInstance)
    }

    private companion object {
        const val BOOK_RESERVATION_TOPIC = "book-reservation"
    }
}