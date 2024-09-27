package model

import jakarta.persistence.Entity
import java.util.UUID

@Entity
data class User(
    val id: UUID? = null,
    val name: String,
    val password: String,
    val email: String,
    val userType: UserType
)
