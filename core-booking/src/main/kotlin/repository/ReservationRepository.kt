package org.example.model

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types
import java.util.UUID

@Repository
class ReservationRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    private companion object {
        private const val FIND_RESERVATION_REQUEST_BY_ID_AND_UID =
            "SELECT id, user_id, book_id, created_at, status, reason FROM reservations WHERE id = ? AND user_id = ? LIMIT 1"
    }

    internal fun findReservationRequestById(id: UUID, uid: UUID): Reservation? {
        return jdbcTemplate.query(
                FIND_RESERVATION_REQUEST_BY_ID_AND_UID,
                ReservationRequestRowMapper(),
                id.toString(),
                uid.toString()
            ).firstOrNull()
    }

    private class ReservationRequestRowMapper : RowMapper<Reservation?> {
        @Throws(SQLException::class)
        override fun mapRow(rs: ResultSet, rowNum: Int): Reservation {
            return Reservation(
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