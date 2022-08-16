package com.itlbv.buildinglimitsprocessor.exceptions

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(Exception::class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    fun exception(e: Exception, request: WebRequest?): ErrorMessage {
        logger.error(e.message, e)

        val errorStatus = HttpStatus.INTERNAL_SERVER_ERROR

        return ErrorMessage(
            errorStatus.value(),
            errorStatus,
            e.message!!,
            request!!.getDescription(false)
        )
    }
}