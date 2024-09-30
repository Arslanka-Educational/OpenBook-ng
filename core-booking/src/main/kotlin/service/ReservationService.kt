package org.example.service

import org.example.controller.ApiException
import org.example.model.Reservation
import org.example.model.ReservationRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.Instant
import java.util.UUID
import kotlin.jvm.Throws

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
) {
    @Throws(ApiException::class)
    internal fun getReservationRequest(uid: UUID, reservationId: UUID): Reservation {
        return reservationRepository.findReservationRequestById(
            id = reservationId,
            uid = uid,
        ) ?: throw ApiException(
            code = "RESERVATION_REQUEST_NOT_FOUND",
            message = "Reservation request with id: '$reservationId' wasn't found",
            httpStatusCode = 404,
        )
    }

    internal fun reserveBook(uid: UUID, reservationId: UUID, bookId: UUID): Reservation {
        val now = Instant.now()
        return reservationRepository.insertReservationRequest(
            reservation = Reservation(
                id = reservationId,
                userId = uid,
                bookId = bookId,
                status = Reservation.ReservationStatus.NEW,
                createdAt = now,
                updatedAt = now,
                reason = null,
            ),
            onConflict = { e ->
                throw ApiException(
                    code = "REQUEST_CONFLICT",
                    message = "Reservation request with id: '$reservationId' was already created for user: '$uid' ",
                    httpStatusCode = 409,
                    cause = e
                )
            },
        )
    }
}