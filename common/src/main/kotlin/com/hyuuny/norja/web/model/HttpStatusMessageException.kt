package com.hyuuny.norja.web.model

import org.springframework.http.HttpStatus


class HttpStatusMessageException : RuntimeException {
    val status: HttpStatus
    val code: String
    val args: Array<Any>

    constructor(
        status: HttpStatus,
        code: String,
        vararg args: Any
    ) : super(code) {
        this.status = status
        this.code = code
        this.args = arrayOf(args as Array<*>)
    }

    constructor(
        status: HttpStatus,
        code: String,
        t: Throwable?,
        vararg args: Any
    ) : super(code, t) {
        this.status = status
        this.code = code
        this.args = arrayOf(args as Array<*>)
    }
}