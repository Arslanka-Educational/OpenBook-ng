package org.example.kafka

import org.example.model.ReservationEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class BookReservationProducer(
    private val kafkaTemplate: KafkaTemplate<String, ReservationEvent>,
    @Value("\${spring.kafka.producer.topic.name}") private val topic: String,
) {

    fun sendMessage(reservationEvent: ReservationEvent) {
        kafkaTemplate.send(topic, reservationEvent.id.toString(), reservationEvent)
    }

    private companion object {

    }
}