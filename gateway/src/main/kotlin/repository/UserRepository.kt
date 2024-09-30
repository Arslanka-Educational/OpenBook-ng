package org.example.repository

import model.User
import model.UserType
import org.example.repository.util.mapUserType
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.*

@Repository
class UserRepository(
    private val databaseClient: NamedParameterJdbcTemplate
) {
    fun getByUsername(username: String): User? {
        val namedParameters: SqlParameterSource = MapSqlParameterSource("username", username)
        return databaseClient.queryForObject(GET_BY_USERNAME, namedParameters, UserRowMapper())
    }

    fun registerUser(user: User) {
        val namedParameters: SqlParameterSource = MapSqlParameterSource().addValues(
            mapOf(
                "id" to user.id,
                "username" to user.name,
                "password" to user.password,
                "email" to user.email,
                "user_type" to user.userType.typeName,
            )
        )

        databaseClient.update(INSERT_USER, namedParameters)
    }

    class UserRowMapper : RowMapper<User> {
        override fun mapRow(rs: ResultSet, rowNum: Int): User {
            return User(
                id = UUID.fromString(rs.getString("id")),
                name = rs.getString("name"),
                password = rs.getString("password"),
                email = rs.getString("email"),
                userType = mapUserType(rs.getString("user_type"))
            )
        }
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