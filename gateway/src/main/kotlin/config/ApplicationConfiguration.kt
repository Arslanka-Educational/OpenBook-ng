package org.example.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@EnableConfigurationProperties(UserAuthorities::class)
class ApplicationConfiguration {

    @Bean
    internal fun restTemplate() = RestTemplate()

}