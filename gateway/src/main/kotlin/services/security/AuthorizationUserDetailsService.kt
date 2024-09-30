package org.example.services.security

import kotlinx.coroutines.runBlocking
import model.AuthorizationUserDetails
import org.example.config.UserAuthorities
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.openbook.model.UserRegisterRequest
import org.example.repository.UserRepository
import java.util.*
import org.example.repository.util.toUser

@Service
class AuthorizationUserDetailsService(
    private val userRepository: UserRepository,
    private val userAuthorities: UserAuthorities,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails = runBlocking {
        userRepository.getByUsername(username)
            ?.let { println(it)
            it}
            ?.let { AuthorizationUserDetails(it, userAuthorities) }
            ?: throw IllegalArgumentException("User with id $username doesn't exists")
    }

    fun registerUser(userRegisterRequest: UserRegisterRequest) = runBlocking {
        val user = userRegisterRequest.toUser(passwordEncoder)
            .copy(id = UUID.randomUUID())

        userRepository.registerUser(user)
        return@runBlocking user
    }
}