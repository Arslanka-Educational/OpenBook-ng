package org.example.model

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ReservationRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    private companion object {
        private const val FIND_RESERVATION_REQUEST_BY_ID_AND_UID =
            "SELECT id, user_id, book_id, created_at, status, reason WHERE id = ? AND user_id = ?"
    }

    internal fun findReservationRequestById(id: UUID, uid: UUID): Reservation? {
        return jdbcTemplate.queryForObject(
            FIND_RESERVATION_REQUEST_BY_ID_AND_UID, arrayOf(id, uid),
        ) { rs, _ ->
            Reservation(
                id = rs.getString("id").let { UUID.fromString(it) },
                userId = rs.getString("user_id").let { UUID.fromString(it) },
                bookId = rs.getString("book_id").let { UUID.fromString(it) },
                createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
                status = rs.getString("status").let { Reservation.ReservationStatus.valueOf(it) },
                reason = rs.getString("reason"),
            )
        }
    }
}