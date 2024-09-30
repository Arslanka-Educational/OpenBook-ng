package org.example.config

import model.UserAuthority
import model.UserType
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring.security")
class UserAuthorities {
    lateinit var authorities: Map<UserType, List<UserAuthority>>
}