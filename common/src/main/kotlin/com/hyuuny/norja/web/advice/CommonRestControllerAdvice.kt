package com.hyuuny.norja.web.advice

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.hyuuny.norja.web.model.ErrorResponse
import com.hyuuny.norja.web.model.FieldError
import com.hyuuny.norja.web.model.HttpStatusMessageException
import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.lang.Nullable
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*
import javax.servlet.http.HttpServletRequest

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations = [RestController::class])
class CommonRestControllerAdvice(
    private val messageSourceAccessor: MessageSourceAccessor,
    private val modelMapper: ModelMapper
) : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val bindingResult = ex.bindingResult
        val fieldErrors: List<FieldError> = getFieldErrors(bindingResult)
        val code = "exception.${ex.javaClass.simpleName}"
        return toErrorResponse(
            ex,
            status,
            code,
            messageSourceAccessor!!.getMessage(code),
            fieldErrors
        )
    }

    override fun handleBindException(
        ex: BindException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val bindingResult: BindingResult = ex.getBindingResult()
        val fieldErrors: List<FieldError> = getFieldErrors(bindingResult)
        val code = "exception.${ex.javaClass.simpleName}"
        return toErrorResponse(
            ex,
            status,
            code,
            messageSourceAccessor!!.getMessage(code),
            fieldErrors
        )
    }

    override fun handleExceptionInternal(
        ex: java.lang.Exception,
        @Nullable body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return toErrorResponse(
            ex,
            status,
            null,
            ex.message,
            emptyList()
        )
    }

    @ExceptionHandler(*[AccessDeniedException::class])
    protected fun handleAccessDeniedException(
        ex: Exception,
        request: WebRequest?
    ): ResponseEntity<Any> {
        return toErrorResponse(
            ex,
            HttpStatus.FORBIDDEN,
            null,
            ex.message,
            emptyList()
        )
    }

    @ExceptionHandler(HttpStatusCodeException::class)
    protected fun httpStatusCodeException(
        ex: HttpStatusCodeException
    ): ResponseEntity<Any> {
        return toErrorResponse(
            ex,
            ex.statusCode,
            null,
            ex.responseBodyAsString,
            emptyList()
        )
    }

    @ExceptionHandler(HttpStatusMessageException::class)
    protected fun handleHttpStatusMessageException(ex: HttpStatusMessageException): ResponseEntity<Any> {
        val code: String = ex.code
        val status: HttpStatus = ex.status
        val args: Array<Any> = ex.args
        return toErrorResponse(ex, status, code, ex.message, emptyList(), *args)
    }

    @JsonInclude(Include.NON_EMPTY)
    @ExceptionHandler(Exception::class)
    protected fun handleException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<Any> {
        val status = getStatus(request)
        return toErrorResponse(ex, status, null, ex.message, emptyList())
    }

    private fun toErrorResponse(
        ex: Exception,
        status: HttpStatus,
        @Nullable code: String?,
        message: String?,
        @Nullable fieldErrors: List<FieldError>,
        vararg args: Any
    ): ResponseEntity<Any> {
        var code = code
        code = Objects.toString(code, "exception.${ex.javaClass.simpleName}")
        var convertMessage: String? = null
        try {
            convertMessage = messageSourceAccessor.getMessage(code, args)
        } catch (e: Exception) {
            // log.error("No such message", ex);
        }
        return ResponseEntity
            .status(status)
            .body(
                ErrorResponse(
                    status = status,
                    code = code,
                    message = Objects.toString(convertMessage, message),
                    fieldErrors = fieldErrors
                )
            )
    }

    private fun getFieldErrors(bindingResult: BindingResult): List<FieldError> {
        return modelMapper!!.map(
            bindingResult.fieldErrors,
            object : TypeToken<List<FieldError?>?>() {}.type
        )
    }

    private fun getStatus(request: HttpServletRequest): HttpStatus {
        val statusCode = request.getAttribute("javax.servlet.error.status_code") as Int
        return HttpStatus.valueOf(statusCode)
    }
}