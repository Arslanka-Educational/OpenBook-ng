package org.example.kafka

import org.example.model.BookInstance
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class BookReservationProducer(
    private val kafkaTemplate: KafkaTemplate<String, BookInstance>,
    @Value("\${spring.kafka.producer.topic.name}") private val topic: String,
) {

    fun sendMessage(bookInstance: BookInstance) {
        kafkaTemplate.send(topic, bookInstance.id.toString(), bookInstance)
    }

    private companion object {
//        const val BOOK_RESERVATION_TOPIC = "core-catalog-reservations"
    }
}