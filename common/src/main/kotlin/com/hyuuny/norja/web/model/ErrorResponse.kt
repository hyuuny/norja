package com.hyuuny.norja.web.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import org.springframework.http.HttpStatus
import java.time.LocalDateTime


@JsonInclude(Include.NON_EMPTY)
data class ErrorResponse(
    val status: HttpStatus? = null,
    val code: String? = null,
    val message: String? = null,
    val timestamp: LocalDateTime? = LocalDateTime.now(),
    val fieldErrors: List<FieldError>? = null
) {
    constructor(
        status: HttpStatus? = null,
        code: String? = null,
        message: String? = null,
        fieldErrors: List<FieldError>? = null
    ) : this(
        status = status,
        code = code,
        message = message,
        timestamp = LocalDateTime.now(),
        fieldErrors = fieldErrors
    )
}

data class FieldError(
    private val objectName: String? = null,
    private val field: String? = null,
    private val rejectedValue: Any? = null,
    private val defaultMessage: String? = null,
)
