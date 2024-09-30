package org.example.repository.util

import model.User
import model.UserType
import org.springframework.security.crypto.password.PasswordEncoder
import ru.openbook.model.UserRegisterRequest

object UserTypeUtil {
    fun fromValue(value: String): UserType {
        return UserType.values().find { it.typeName == value }
            ?: throw IllegalArgumentException("No enum constant for value: $value")
    }
}

fun UserRegisterRequest.toUser(passwordEncoder: PasswordEncoder) = User(
    name = this.firstName,
    password = passwordEncoder.encode(this.password),
    email = this.email,
    userType = UserType.values().first{ it.typeName == this.userType.value },
)