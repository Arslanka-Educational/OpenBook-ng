package org.example.config

import model.UserAuthority
import model.UserType
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.security")
data class UserAuthorities (
    val authorities: Map<UserType, List<UserAuthority>>
)