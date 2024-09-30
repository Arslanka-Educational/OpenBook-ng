package org.example.model

import java.time.Instant
import java.util.UUID

data class Reservation(
    val id: UUID,
    val userId: UUID,
    val bookId: UUID,
    val createdAt: Instant,
    val updatedAt: Instant,
    val status: ReservationStatus,
    val reason: String?,
) {
    enum class ReservationStatus {
        NEW,
        IN_PROGRESS,
        FAILED,
        SUCCESS;
    }
}

