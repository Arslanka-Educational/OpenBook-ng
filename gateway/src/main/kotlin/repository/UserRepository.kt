package org.example.repository

import model.User
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    private val databaseClient: NamedParameterJdbcTemplate
) {
    fun getByUsername(username: String): User? {
        val namedParameters: SqlParameterSource = MapSqlParameterSource("username", username)
        return databaseClient.queryForObject(GET_BY_USERNAME, namedParameters, User::class.java)
    }

    fun registerUser(user: User) {
        val namedParameters: SqlParameterSource = MapSqlParameterSource().addValues(
            mapOf(
                "id" to user.id,
                "username" to user.name,
                "password" to user.password,
                "email" to user.email,
                "userType" to user.userType,
            )
        )

        databaseClient.update(INSERT_USER, namedParameters)
    }

    private companion object {
        val GET_BY_USERNAME = """
            SELECT id, name, password, email, user_type FROM users WHERE name LIKE CONCAT('%', :username, '%')
        """.trimIndent()

        val INSERT_USER = """
            INSERT INTO users(id, name, password, email, user_type) VALUES (:id, :username, :password, :email, :user_type)
        """.trimIndent()
    }
}