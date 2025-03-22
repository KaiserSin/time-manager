package project.model.setting.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import project.util.OnCreate
import project.util.OnUpdate

data class SettingRequest(

    @field:NotNull(
        message = "userId cannot be null",
        groups = [OnCreate::class, OnUpdate::class]
    )
    val userId: Long? = null,


    @field:NotBlank(
        message = "Text cannot be blank",
        groups = [OnCreate::class, OnUpdate::class]
    )
    @field:Size(
        max = 1000,
        message = "Text length must be <= 1000 characters",
        groups = [OnCreate::class, OnUpdate::class]
    )
    val text: String? = null
)
