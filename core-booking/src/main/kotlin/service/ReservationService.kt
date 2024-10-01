package org.example.service

import org.example.controller.ApiException
import org.example.model.Reservation
import org.example.model.Reservation.ReservationStatus
import org.example.model.ReservationRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val taskScheduler: TaskScheduler,
    @Value("\${spring.timeout.core-catalog-callback-timeout-millis}")
    private val tpsTimeoutMillis: Long,
) {
    @Throws(ApiException::class)
    internal fun getReservationRequest(uid: UUID, reservationId: UUID): Reservation {
        return reservationRepository.findByIdAndUid(
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
        val reservation = reservationRepository.insertIfMissing(
            reservation = Reservation(
                id = reservationId,
                userId = uid,
                bookId = bookId,
                status = ReservationStatus.NEW,
                createdAt = now,
                updatedAt = now,
                reason = null,
            ),
        )

        taskScheduler.schedule(
            { scheduleCatalogReservationIfNew(reservation) },
            Instant.now(),
        )
        return reservation
    }

    private fun scheduleCatalogReservationIfNew(originalReservation: Reservation) {
        if (originalReservation.status in listOf(
                ReservationStatus.IN_PROGRESS,
                ReservationStatus.SUCCESS,
                ReservationStatus.FAILED,
            )
        ) {
            return
        }

        var reservation = originalReservation
        //TODO (core-catalog call with kafka notification)
        reservation = reservationRepository.updateWithIdAndStatus(
            reservation = reservation.copy(
                status = ReservationStatus.IN_PROGRESS,
                updatedAt = Instant.now(),
            ),
            expectedStatus = ReservationStatus.NEW,
        )!!
        taskScheduler.schedule(
            { onCoreCatalogTimeout(reservation) },
            Instant.now().plusMillis(tpsTimeoutMillis),
        )
    }

    private fun onCoreCatalogTimeout(reservation: Reservation) {
        if (reservation.status in listOf(ReservationStatus.SUCCESS, ReservationStatus.FAILED)) {
            return
        }

        val reason = "Core-catalog timeout"
        val now = Instant.now()

        reservationRepository.updateWithIdAndStatus(
            reservation = reservation.copy(
                status = ReservationStatus.FAILED,
                updatedAt = now,
                reason = reason,
            ),
            expectedStatus = ReservationStatus.IN_PROGRESS,
        )!!
    }
}