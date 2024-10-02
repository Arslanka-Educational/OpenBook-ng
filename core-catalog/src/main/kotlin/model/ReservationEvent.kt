package org.example.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ReservationEvent(
    @JsonProperty("external_id")
    val id: UUID,
    @JsonProperty("user_id")
    val userId: UUID,
    @JsonProperty("book_id")
    val bookInstanceId: UUID,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("reason")
    val reason: String?,
)
