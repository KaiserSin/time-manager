package project.model.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import project.model.user.Role

data class UserRequest(

    @field:NotBlank(message = "Name cannot be blank")
    @field:Size(max = 100, message = "Name length must be <= 100 characters")
    val name: String? = null,


    @field:Email(message = "Email must be valid format")
    @field:NotBlank(message = "Email cannot be blank")
    @field:Size(max = 255, message = "Email length must be <= 255 characters")
    val email: String? = null,

    @field:Size(min = 6, max = 255, message = "Password must be 6..255 characters long")
    val password: String? = null,


    val role: Role? = null
)
