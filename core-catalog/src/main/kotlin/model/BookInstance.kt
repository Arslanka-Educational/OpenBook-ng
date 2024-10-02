package org.example.model

import ru.openbook.model.BookInstanceResponse
import java.util.UUID

data class BookInstance(
    val id: UUID,
    val bookContentId: UUID,
    var status: BookStatus,
) {
    fun mapToResponse() = BookInstanceResponse(
        id = this.id,
        bookContentId = this.bookContentId,
        status = this.status.name
    )
}
