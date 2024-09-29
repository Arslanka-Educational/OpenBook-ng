import model.Reservation
import org.example.controller.ApiException
import org.springframework.stereotype.Service
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
}