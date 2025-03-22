package project.model.task.dto

import jakarta.validation.constraints.*
import project.util.OnCreate
import project.util.OnUpdate

data class TaskRequest(

    @field:NotBlank(
        message = "Name cannot be blank",
        groups = [OnCreate::class, OnUpdate::class]
    )
    @field:Size(
        max = 100,
        message = "Name length must be <= 100 characters",
        groups = [OnCreate::class, OnUpdate::class]
    )
    val name: String? = null,


    @field:NotBlank(
        message = "Description is required",
        groups = [OnCreate::class]
    )
    val description: String? = null,


    @field:Pattern(
        regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(:\\d{2})?$",
        message = "startTime must be in ISO-8601 format (yyyy-MM-ddTHH:mm[:ss])",
        groups = [OnCreate::class, OnUpdate::class]
    )
    val startTime: String? = null,


    @field:NotNull(
        message = "Duration cannot be null",
        groups = [OnCreate::class, OnUpdate::class]
    )
    @field:Min(
        value = 60,
        message = "Duration must be at least 60 seconds",
        groups = [OnCreate::class, OnUpdate::class]
    )
    val duration: Long? = null,


    val gptDescription: String? = null,


    @field:NotNull(
        message = "userId cannot be null",
        groups = [OnCreate::class, OnUpdate::class]
    )
    @field:Min(
        value = 1,
        message = "userrId must be a positive number",
        groups = [OnCreate::class]
    )
    val userId: Long? = null,


    val isDone: Boolean? = null
)
