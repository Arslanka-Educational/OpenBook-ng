package model

import java.time.LocalDateTime
import java.util.UUID

data class ReservationRequest(
    val id: UUID,
    val userId: UUID,
    val bookId: UUID,
    val createdAt: LocalDateTime,
    val status: String,
)