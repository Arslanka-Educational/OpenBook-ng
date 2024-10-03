package org.example.service

import org.example.service.schedule.RetryableTaskScheduler
import org.example.controller.ApiException
import org.example.model.Reservation
import org.example.model.Reservation.ReservationStatus
import org.example.model.ReservationRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID
import kotlin.math.log

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val taskScheduler: RetryableTaskScheduler,
    @Value("\${spring.timeout.core-catalog-callback-timeout-millis}")
    private val tpsTimeoutMillis: Long,
    private val restRequestSendService: RestRequestSendService,
) {

    private val logger = LoggerFactory.getLogger(ReservationService::class.java)

    @Throws(ApiException::class)
    internal fun getReservationRequest(uid: UUID, reservationId: UUID): Reservation {
        return reservationRepository.findByIdAndUid(
            id = reservationId,
            uid = uid,
        ) ?: throw ApiException(
            code = "RESERVATION_REQUEST_NOT_FOUND",
            message = "Reservation request with id: '$reservationId' wasn't found",
            httpStatusCode = 404,
        ).also {
            logger.error("Reservation request with id: '$reservationId' wasn't found for uid: $uid")
        }
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

        logger.debug("Created reservation: $reservation")

        taskScheduler.scheduleWithRetry(
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
            logger.debug("Reservation already in progress, success, or failed: $originalReservation.status")
            return
        }

        var reservation = originalReservation
        logger.debug("Sending to core-catalog reservation: $reservation")
        restRequestSendService.sendBookInstance(
            bookInstanceId = reservation.bookId,
            reservationId = reservation.id,
            userId = reservation.userId
        ) ?: throw IllegalArgumentException("Couldn't send request to CORE-CATALOG reservation: $reservation")

        reservation = reservationRepository.updateWithIdAndStatus(
            id = reservation.id,
            uid = reservation.userId,
            bookId = reservation.bookId,
            status = ReservationStatus.IN_PROGRESS,
            updatedAt = Instant.now(),
            reason = null,
            expectedStatus = ReservationStatus.NEW,
        )!!
        taskScheduler.scheduleWithRetry(
            { onCoreCatalogTimeout(reservation) },
            Instant.now().plusMillis(tpsTimeoutMillis),
        )
    }

    private fun onCoreCatalogTimeout(reservation: Reservation) {
        if (reservation.status in listOf(ReservationStatus.SUCCESS, ReservationStatus.FAILED)) {
            logger.debug("Reservation already completed or failed: $reservation.status")
            return
        }

        val reason = "Core-catalog timeout"
        val now = Instant.now()

        reservationRepository.updateWithIdAndStatus(
            id = reservation.id,
            uid = reservation.userId,
            bookId = reservation.bookId,
            status = ReservationStatus.FAILED,
            updatedAt = now,
            reason = reason,
            expectedStatus = ReservationStatus.IN_PROGRESS,
        )!!.also {
            logger.info("Updated reservation to FAILED due to core catalog timeout: $it")
        }
    }
}