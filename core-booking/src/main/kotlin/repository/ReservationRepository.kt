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
            "INSERT INTO reservations (id, user_id, book_id, created_at, updated_at, status, reason) VALUES (?, ?, ?, ?, ?, ?, ?)"
    }

    internal fun findReservationRequestById(id: UUID, uid: UUID): Reservation? {
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

    internal fun insertReservationRequest(
        reservation: Reservation,
    ): Reservation {
        return try {
            jdbcTemplate.update { connection ->
                val preparedStatement = connection.prepareStatement(INSERT_RESERVATION_REQUEST)
                preparedStatement.setString(1, reservation.id.toString())
                preparedStatement.setString(2, reservation.userId.toString())
                preparedStatement.setString(3, reservation.bookId.toString())
                preparedStatement.setTimestamp(4, Timestamp.from(reservation.createdAt))

                preparedStatement.setTimestamp(5, Timestamp.from(reservation.updatedAt))
                preparedStatement.setString(6, reservation.status.toString())
                preparedStatement.setString(7, reservation.reason)
                preparedStatement
            }
            reservation
        } catch (e: DuplicateKeyException) {
            findReservationRequestById(
                id = reservation.id,
                uid = reservation.userId,
            )!!
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