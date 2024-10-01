package org.example.model

import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import java.util.UUID

@Repository
class ReservationRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    private companion object {
        private const val FIND_RESERVATION_REQUEST_BY_ID_AND_UID =
            "SELECT id, user_id, book_id, created_at, updated_at, status, reason FROM reservations WHERE id = ? AND user_id = ? LIMIT 1"
        private const val INSERT_RESERVATION_REQUEST =
            """
            INSERT INTO reservations (id, user_id, book_id, created_at, updated_at, status, reason)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT DO NOTHING
            RETURNING id, user_id, book_id, created_at, updated_at, status, reason;
            """
        private const val UPDATE_RESERVATION_REQUEST_CHECK_STATUS =
            """
            UPDATE reservations 
            SET user_id = ?, book_id = ?, created_at = ?, updated_at = ?, status = ?, reason = ?
            WHERE id = ? AND user_id = ? AND status = ?
            RETURNING id, user_id, book_id, created_at, updated_at, status, reason;
            """
    }

    internal fun findByIdAndUid(id: UUID, uid: UUID): Reservation? {
        return try {
            jdbcTemplate.queryForObject(
                FIND_RESERVATION_REQUEST_BY_ID_AND_UID,
                ReservationRequestRowMapper,
                id.toString(),
                uid.toString(),
            )
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    internal fun insertIfMissing(reservation: Reservation): Reservation {
        return try {
            jdbcTemplate.queryForObject(
                INSERT_RESERVATION_REQUEST,
                ReservationRequestRowMapper,
                reservation.id.toString(),
                reservation.userId.toString(),
                reservation.bookId.toString(),
                Timestamp.from(reservation.createdAt),
                reservation.updatedAt.let { Timestamp.from(it) },
                reservation.status.toString(),
                reservation.reason
            )!!
        } catch (e: DuplicateKeyException) {
            return findByIdAndUid(
                id = reservation.id,
                uid = reservation.userId,
            )!!
        }
    }

    internal fun updateWithIdAndStatus(
        reservation: Reservation,
        expectedStatus: Reservation.ReservationStatus,
    ): Reservation? {
        return try {
            jdbcTemplate.queryForObject(
                UPDATE_RESERVATION_REQUEST_CHECK_STATUS,
                ReservationRequestRowMapper,
                reservation.id.toString(),
                reservation.userId.toString(),
                reservation.bookId.toString(),
                reservation.createdAt.let { Timestamp.from(it) },
                reservation.updatedAt.let { Timestamp.from(it) },
                reservation.status.toString(),
                reservation.reason,

                reservation.id.toString(),
                reservation.userId.toString(),
                expectedStatus.toString(),
            )
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    private object ReservationRequestRowMapper : RowMapper<Reservation?> {
        @Throws(SQLException::class)
        override fun mapRow(rs: ResultSet, rowNum: Int): Reservation {
            return Reservation(
                id = rs.getString("id").let { UUID.fromString(it) },
                userId = rs.getString("user_id").let { UUID.fromString(it) },
                bookId = rs.getString("book_id").let { UUID.fromString(it) },
                createdAt = rs.getTimestamp("created_at").toInstant(),
                updatedAt = rs.getTimestamp("updated_at").toInstant(),
                status = rs.getString("status").let { Reservation.ReservationStatus.valueOf(it) },
                reason = rs.getString("reason"),
            )
        }
    }
}