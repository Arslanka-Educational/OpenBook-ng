package org.example.service

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.example.model.Reservation
import org.springframework.stereotype.Component

@Component
class BookingMetricsService(meterRegistry: MeterRegistry) {
    private val successCounter: Counter = Counter.builder("booking_status_success_count")
        .description("Count of successful bookings")
        .tags("status", "SUCCESS")
        .register(meterRegistry)

    private val failedCounter: Counter = Counter.builder("booking_status_failed_count")
        .description("Count of failed bookings")
        .tags("status", "FAILED")
        .register(meterRegistry)

    internal fun incrementBookingMetric(status: Reservation.ReservationStatus) {
        when (status) {
            Reservation.ReservationStatus.SUCCESS -> successCounter.increment()
            Reservation.ReservationStatus.FAILED -> failedCounter.increment()
            else -> {}
        }
    }
}