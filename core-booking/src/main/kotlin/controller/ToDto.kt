package org.example.controller

import model.Reservation
import ru.openbook.model.BookReservationStatus
import ru.openbook.model.ReservationRequestFailure
import ru.openbook.model.ReservationRequestSuccess

internal fun Reservation.toDto() =
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
    }