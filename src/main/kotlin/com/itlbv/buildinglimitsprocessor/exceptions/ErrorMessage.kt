package com.itlbv.buildinglimitsprocessor.exceptions

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class ErrorMessage(
    val statusCode: Int,
    val statusDescription: HttpStatus,
    val message: String,
    val description: String?,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)