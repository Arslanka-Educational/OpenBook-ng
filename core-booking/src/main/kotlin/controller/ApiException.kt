package org.example.controller

open class ApiException(
    val code: String,
    message: String,
    val httpStatusCode: Int,
    cause: Throwable? = null,
) : Exception(message, cause)