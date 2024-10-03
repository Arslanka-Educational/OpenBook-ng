package org.example.service

import org.example.kafka.BookReservationProducer
import org.example.model.BookInstance
import org.example.model.BookStatus
import org.example.model.ReservationEvent
import org.example.repository.BookInstanceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.openbook.model.BookReservationInitializationRequest
import java.util.UUID

@Service
class BookInstanceService(
    private val bookInstanceRepository: BookInstanceRepository,
    private val bookReservationProducer: BookReservationProducer,
) {

    @Transactional
    fun initializeBookInstanceReservation(
        bookInstanceId: UUID,
        bookReservationInitializationRequest: BookReservationInitializationRequest,
    ): BookInstance? {
        val bookInstance: BookInstance = bookInstanceRepository.getBookInstance(bookInstanceId) ?: return null

        bookInstance.status = BookStatus.RESERVED
        bookInstanceRepository.update(bookInstance)

        val reservationEvent = ReservationEvent(
            id = bookReservationInitializationRequest.externalId,
            userId = bookReservationInitializationRequest.userId,
            bookInstanceId = bookInstance.id,
            status = bookInstance.status.name,
            reason = "OK"
        )

        bookReservationProducer.sendMessage(reservationEvent)
        return bookInstance
    }

    @Transactional
    fun createBookInstance(bookContentId: UUID): BookInstance {
        val id = UUID.randomUUID()
        val bookInstance = BookInstance(
            id = id,
            bookContentId = bookContentId,
            status = BookStatus.FREE
        )

        bookInstanceRepository.persist(bookInstance)
        return bookInstance
    }
}