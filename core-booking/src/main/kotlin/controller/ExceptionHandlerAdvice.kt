package org.example.controller

import io.klogging.Klogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlerAdvice {

    private companion object : Klogging

    private fun createErrorResponse(
        httpStatusCode: Int,
        code: String?,
        message: String?,
    ): ResponseEntity<Map<String, Any?>> = ResponseEntity.status(httpStatusCode).body(
        mapOf(
            "code" to code,
            "message" to message,
        ),
    )

    @ExceptionHandler(ApiException::class)
    internal suspend fun onApiException(ex: ApiException): ResponseEntity<out Any> {
        if (ex.httpStatusCode in 500..599) {
            logger.error(ex) { "5xx error occurred" }
        } else {
            logger.warn(ex) { "error occurred" }
        }
        return createErrorResponse(
            httpStatusCode = ex.httpStatusCode,
            code = ex.code,
            message = ex.message,
        )
    }
}