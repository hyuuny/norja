package com.hyuuny.norja.web.interceptor

import mu.KotlinLogging
import org.springframework.web.servlet.HandlerInterceptor
import java.util.UUID.randomUUID
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val LOG_ID = "logId"

class LogInterceptor : HandlerInterceptor {

    private val log = KotlinLogging.logger {}

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val requestURI = request.requestURI
        val uuid = randomUUID().toString()
        request.setAttribute(LOG_ID, uuid)
        log.info("REQUEST  [{}][{}][{}][{}]", uuid, request.dispatcherType, requestURI, handler)
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val requestURI = request.requestURI
        val logId = request.getAttribute(LOG_ID)
        log.info("RESPONSE [{}][{}][{}]", logId, request.dispatcherType, requestURI)
        if (ex != null) {
            log.error("afterCompletion error!!", ex)
        }
    }
}