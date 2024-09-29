package org.example.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
@ComponentScan
class SpringJdbcConfig(
    @Value("\${spring.datasource.driver}")
    private val driver: String,
    @Value("\${spring.datasource.url}")
    private val url: String,
    @Value("\${spring.datasource.username}")
    private val username: String,
    @Value("\${spring.datasource.password}")
    private val password: String,
) {
    @Primary
    @Bean(name = ["dataSource"])
    @ConfigurationProperties(prefix = "spring.datasource")
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(driver)
        dataSource.url = url
        dataSource.username = username
        dataSource.password = password
        return dataSource
    }

    @Primary
    @Bean(name = ["jdbcTemplate"])
    fun jdbcTemplate(@Qualifier("dataSource") dataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }
}