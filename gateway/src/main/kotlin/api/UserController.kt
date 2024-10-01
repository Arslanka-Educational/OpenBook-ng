package org.example.api

import model.AuthorizationUserDetails
import org.example.service.security.AuthorizationUserDetailsService
import org.example.service.security.JwtService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ru.openbook.api.UsersApi
import ru.openbook.model.UserInfoResponse
import ru.openbook.model.UserRegisterRequest
import ru.openbook.model.UserRegisterResponse
import java.util.*

@RestController
class UserController(
    private val userService: AuthorizationUserDetailsService,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
) : UsersApi {
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/v1/users/login"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun loginUser(authentication: Authentication): ResponseEntity<String> {
        val userDetails = authentication.principal as AuthorizationUserDetails
        return ResponseEntity.ok().body(jwtService.generateToken(userDetails))
    }

     override fun registerUser(
        userRegisterRequest: UserRegisterRequest
    ): ResponseEntity<UserRegisterResponse> {
         return ResponseEntity.ok().body(UserRegisterResponse(userService.registerUser(userRegisterRequest).id.toString()))
    }

    override fun userInfo(userId: UUID): ResponseEntity<UserInfoResponse> {
//        return ResponseEntity.ok(UserInfoResponse("name", "email"))
        TODO("Not yet implemented")
    }
}