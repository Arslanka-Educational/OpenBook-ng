package org.example.stream.kafka

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.example.model.Reservation
import org.example.model.ReservationRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class CoreCatalogEventsListener(
    private val reservationRepository: ReservationRepository,
    private val objectMapper: ObjectMapper,
) {

    @KafkaListener(topics = ["\${spring.kafka.consumer.topic.name}"], groupId = "\${spring.kafka.consumer.group-id}")
    internal fun resolvedReservationStatus(message: String) {
        val reservationEvent: ReservationEvent = objectMapper.readValue(message, ReservationEvent::class.java)

        val updatedStatus = when (reservationEvent.status) {
            "SUCCESS" -> Reservation.ReservationStatus.SUCCESS
            "FAILED" -> Reservation.ReservationStatus.FAILED
            else -> Reservation.ReservationStatus.IN_PROGRESS
        }

        reservationRepository.updateWithIdAndStatus(
            id = reservationEvent.id,
            uid = reservationEvent.userId,
            bookId = reservationEvent.bookId,
            updatedAt = Instant.now(),
            status = updatedStatus,
            reason = reservationEvent.reason,
            expectedStatus = Reservation.ReservationStatus.IN_PROGRESS,
        )!!
    }

    data class ReservationEvent(
        @JsonProperty("external_id")
        val id: UUID,
        @JsonProperty("user_id")
        val userId: UUID,
        @JsonProperty("book_id")
        val bookId: UUID,
        @JsonProperty("status")
        val status: String,
        @JsonProperty("reason")
        val reason: String?,
    )
}
