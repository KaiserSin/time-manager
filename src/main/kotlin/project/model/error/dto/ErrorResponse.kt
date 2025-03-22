package project.model.error.dto

import java.time.LocalDateTime

data class ErrorResponse(
    val message: String,
    val errorCode: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)