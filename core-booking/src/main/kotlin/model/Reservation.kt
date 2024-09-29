package org.example.model

import java.time.LocalDateTime
import java.util.UUID

data class Reservation(
    val id: UUID,
    val userId: UUID,
    val bookId: UUID,
    val createdAt: LocalDateTime,
    val status: ReservationStatus,
    val reason: String?,
) {
    enum class ReservationStatus {
        SUCCESS,
        FAILED;
    }
}

