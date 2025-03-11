package project.model.task.dto

import java.time.LocalDateTime

data class TaskResponse(
    val id: Long?,
    val name: String,
    val description: String,
    val startTime: LocalDateTime,
    val duration: Long,
    val isDone: Boolean
)
