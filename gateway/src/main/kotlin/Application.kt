package org.example

import org.example.config.UserAuthorities
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(UserAuthorities::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}