package org.example.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
class DataSourceConfiguration {
    @Value("\${spring.datasource.url}")
    lateinit var url: String

    @Value("\${spring.datasource.username}")
    lateinit var username: String

    @Value("\${spring.datasource.password}")
    lateinit var password: String

    @Value("\${spring.datasource.driver-class-name}")
    lateinit var driverClassName: String

    @Bean
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(driverClassName)
        dataSource.url = url
        dataSource.username = username
        dataSource.password = password
        return dataSource
    }

    @Bean
    fun namedParameterJdbcTemplate(dataSource: DataSource): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource)
    }
}