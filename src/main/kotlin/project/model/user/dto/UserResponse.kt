package project.model.user.dto

import project.model.user.Role

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val role: Role
)
