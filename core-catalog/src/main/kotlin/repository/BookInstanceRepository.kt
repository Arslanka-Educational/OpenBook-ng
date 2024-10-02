package org.example.repository

import org.example.model.BookInstance
import org.example.model.BookStatus
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.UUID

@Repository
class BookInstanceRepository(
    private val databaseClient: NamedParameterJdbcTemplate
) {

    fun getBookInstance(id: UUID) : BookInstance? {
        val namedParameters: SqlParameterSource = MapSqlParameterSource("id", id)
        return databaseClient.queryForObject(SELECT_BOOK_INSTANCE, namedParameters, BookInstanceRowMapper())
    }

    fun persist(bookInstance: BookInstance) {
        val namedParameters: SqlParameterSource = MapSqlParameterSource().addValues(
            mapOf(
                "id" to bookInstance.id,
                "book_content_id" to bookInstance.bookContentId,
                "book_status" to bookInstance.status.name
            )
        )

        databaseClient.update(INSERT_BOOK_INSTANCE, namedParameters)
    }

    class BookInstanceRowMapper : RowMapper<BookInstance> {
        override fun mapRow(rs: ResultSet, rowNum: Int): BookInstance {
            return BookInstance(
                id = UUID.fromString(rs.getString("id")),
                bookContentId = UUID.fromString(rs.getString("name")),
                status = BookStatus.valueOf(rs.getString("password")),
            )
        }
    }

    private companion object {
        val INSERT_BOOK_INSTANCE = """
            INSERT INTO book_instance (id, book_content_id, status) VALUES (:id, :book_content_id, :book_status)
        """.trimIndent()

        val SELECT_BOOK_INSTANCE = """
            SELECT id, book_content_id, book_status from book_instance where id = :id
        """.trimIndent()
    }
}