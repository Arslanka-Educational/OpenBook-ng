package org.example.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.model.ReservationEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class BookReservationProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    @Value("\${spring.kafka.producer.topic.name}") private val topic: String,
    private val objectMapper: ObjectMapper,
) {

    fun sendMessage(reservationEvent: ReservationEvent) {
        kafkaTemplate.send(topic, reservationEvent.id.toString(), objectMapper.writeValueAsString(reservationEvent))
    }
}