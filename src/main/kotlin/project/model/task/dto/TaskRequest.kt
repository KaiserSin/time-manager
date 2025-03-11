package project.model.task.dto

data class TaskRequest(
    val name: String,
    val description: String,
    val startTime: String? = null,
    val duration: Long,
    val gptDescription: String? = null,
    val executorId: Long,
    val isDone: Boolean
)
