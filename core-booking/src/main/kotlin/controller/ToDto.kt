package org.example.controller

import org.example.model.Reservation
import ru.openbook.model.BookReservationStatus
import ru.openbook.model.ReservationRequestFailure
import ru.openbook.model.ReservationRequestProcessing
import ru.openbook.model.ReservationRequestSuccess

internal fun Reservation.toReservationRequestDto() =
    when (this.status) {
        Reservation.ReservationStatus.SUCCESS -> ReservationRequestSuccess(
            status = BookReservationStatus.SUCCESS,
            reservationId = this.id,
            bookId = this.bookId,
            userId = this.userId,
        )

        Reservation.ReservationStatus.FAILED -> ReservationRequestFailure(
            status = BookReservationStatus.FAILED,
            reason = this.reason.toString()
        )

        else -> ReservationRequestProcessing(
            status = BookReservationStatus.IN_PROGRESS,
            reservationId = this.id,
            bookId = this.bookId,
            userId = this.userId,
        )
    }