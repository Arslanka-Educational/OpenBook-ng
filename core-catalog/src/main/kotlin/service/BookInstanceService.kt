package org.example.service

import org.example.kafka.BookReservationProducer
import org.example.model.BookInstance
import org.example.model.BookStatus
import org.example.repository.BookInstanceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class BookInstanceService (
    private val bookInstanceRepository: BookInstanceRepository,
    private val bookReservationProducer: BookReservationProducer,
) {

    @Transactional
    fun initializeBookInstanceReservation (bookInstanceId: UUID) : BookInstance? {
        val bookInstance: BookInstance = bookInstanceRepository.getBookInstance(bookInstanceId) ?: return null

        bookInstance.status = BookStatus.RESERVED
        bookInstanceRepository.persist(bookInstance)

        bookReservationProducer.sendMessage(bookInstance)
        return bookInstance
    }
}