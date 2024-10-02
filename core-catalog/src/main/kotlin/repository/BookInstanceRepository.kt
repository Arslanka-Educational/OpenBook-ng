package org.example.repository

import org.example.model.BookInstance
import org.example.model.BookStatus
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
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

    fun getBookInstance(id: UUID): BookInstance? {
        val namedParameters: SqlParameterSource = MapSqlParameterSource("id", id)
        return try {
            databaseClient.queryForObject(SELECT_BOOK_INSTANCE, namedParameters, BookInstanceRowMapper())
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    fun persist(bookInstance: BookInstance) {
        val namedParameters: SqlParameterSource = MapSqlParameterSource().addValues(
            mapOf(
                "id" to bookInstance.id,
                "book_content_id" to bookInstance.bookContentId,
                "book_status" to bookInstance.status.name
            )
        )
        try {
            databaseClient.update(INSERT_BOOK_INSTANCE, namedParameters)
        } catch (_: DuplicateKeyException) {

        }
    }

    fun update(bookInstance: BookInstance) {
        val namedParameters: SqlParameterSource = MapSqlParameterSource().addValues(
            mapOf(
                "id" to bookInstance.id,
                "status" to bookInstance.status.name
            )
        )
        databaseClient.query(UPDATE_BOOK_INSTANCE, namedParameters, BookInstanceRowMapper())

    }

    class BookInstanceRowMapper : RowMapper<BookInstance> {
        override fun mapRow(rs: ResultSet, rowNum: Int): BookInstance {
            return BookInstance(
                id = UUID.fromString(rs.getString("id")),
                bookContentId = UUID.fromString(rs.getString("book_content_id")),
                status = BookStatus.valueOf(rs.getString("status")),
            )
        }
    }

    private companion object {
        val INSERT_BOOK_INSTANCE = """
            INSERT INTO book_instance (id, book_content_id, status) VALUES (:id, :book_content_id, :book_status)
        """.trimIndent()

        val UPDATE_BOOK_INSTANCE = """
            UPDATE book_instance 
            SET status = :status
            WHERE id = :id
            RETURNING id, book_content_id, status;
        """.trimIndent()

        val SELECT_BOOK_INSTANCE = """
            SELECT id, book_content_id, status from book_instance where id = :id
        """.trimIndent()
    }
}