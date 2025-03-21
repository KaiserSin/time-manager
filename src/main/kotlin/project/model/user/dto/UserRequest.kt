package project.model.user.dto

import jakarta.validation.constraints.*
import project.util.OnCreate
import project.util.OnUpdate

data class UserRequest(

    @field:NotBlank(
        message = "Name cannot be blank",
        groups = [OnCreate::class, OnUpdate::class]
    )
    @field:Size(
        max = 100,
        message = "Name length must be <= 100 characters",
        groups = [OnCreate::class, OnUpdate::class]
    )
    val name: String? = null
)