package org.example.stream.kafka

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.example.model.Reservation
import org.example.model.ReservationRepository
import org.example.service.BookingMetricsService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class CoreCatalogEventsListener(
    private val reservationRepository: ReservationRepository,
    private val objectMapper: ObjectMapper,
    private val bookingMetricsService: BookingMetricsService,
) {

    @KafkaListener(topics = ["\${spring.kafka.consumer.topic.name}"], groupId = "\${spring.kafka.consumer.group-id}")
    internal fun resolvedReservationStatus(message: String) {
        println("======CONSUMED A MESSAGE $message")
        val reservationEvent: ReservationEvent = objectMapper.readValue(message, ReservationEvent::class.java)
        val updatedStatus = fromCoreCatalogStatus(reservationEvent.status)

        reservationRepository.updateWithIdAndStatus(
            id = reservationEvent.id,
            uid = reservationEvent.userId,
            bookId = reservationEvent.bookInstanceId,
            updatedAt = Instant.now(),
            status = updatedStatus,
            reason = reservationEvent.reason,
            expectedStatus = Reservation.ReservationStatus.IN_PROGRESS,
        )

        bookingMetricsService.incrementBookingMetric(updatedStatus)
    }

    internal fun fromCoreCatalogStatus(status: String) =
        when (status) {
            "RESERVED" -> Reservation.ReservationStatus.SUCCESS
            "FREE" -> Reservation.ReservationStatus.FAILED
            else -> Reservation.ReservationStatus.IN_PROGRESS
        }

    data class ReservationEvent(
        @JsonProperty("external_id")
        val id: UUID,
        @JsonProperty("user_id")
        val userId: UUID,
        @JsonProperty("book_id")
        val bookInstanceId: UUID,
        @JsonProperty("status")
        val status: String,
        @JsonProperty("reason")
        val reason: String?,
    )
}
