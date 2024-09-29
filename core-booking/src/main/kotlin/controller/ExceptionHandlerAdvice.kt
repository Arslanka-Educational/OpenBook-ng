package org.example.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlerAdvice {
    private fun createErrorResponse(
        httpStatusCode: Int,
        code: String?,
        message: String?,
    ): ResponseEntity<Map<String, Any?>> = 
        ResponseEntity.status(httpStatusCode).body(
            mapOf(
                "code" to code,
                "message" to message,
            ),
        )

    @ExceptionHandler(ApiException::class)
    fun onApiException(ex: ApiException): ResponseEntity<out Any> {
        return createErrorResponse(
            httpStatusCode = ex.httpStatusCode,
            code = ex.code,
            message = ex.message,
        )
    }
}