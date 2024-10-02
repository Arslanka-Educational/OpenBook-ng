package org.example.stream.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.model.Reservation
import org.example.model.ReservationRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class CoreCatalogEventsListener(
    private val reservationRepository: ReservationRepository,
    private val objectMapper: ObjectMapper,
) {

    @KafkaListener(topics = ["\${spring.kafka.topic.name}"], groupId = "\${spring.kafka.consumer.group-id}")
    internal fun consume(message: String) {
        val reservationEvent: ReservationEvent = objectMapper.readValue(message, ReservationEvent::class.java)

        val updatedStatus = when (reservationEvent.status) {
            "SUCCESS" -> Reservation.ReservationStatus.SUCCESS
            "FAILED" -> Reservation.ReservationStatus.FAILED
            else -> return // Unrecognized status, perhaps log it or handle it
        }

        reservationRepository.updateWithIdAndStatus(
            Reservation(
                reservationEvent.id,
                reservationEvent.userId,
                reservationEvent.bookId,
                reservationEvent.createdAt,
                reservationEvent.updatedAt,
                updatedStatus,
                reservationEvent.reason
            ),
            Reservation.ReservationStatus.IN_PROGRESS
        )!!
    }

    data class ReservationEvent(
        val id: UUID,
        val userId: UUID,
        val bookId: UUID,
        val createdAt: Instant,
        val updatedAt: Instant,
        val status: String,
        val reason: String?,
    )
}
