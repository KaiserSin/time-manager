package project.util

import project.model.user.User
import project.model.user.dto.UserResponse
import project.model.setting.Setting
import project.model.setting.dto.SettingResponse
import project.model.task.Task
import project.model.task.dto.TaskResponse


fun User.toResponse(): UserResponse {
    return UserResponse(
        id = this.id ?: 0L,
        name = this.name ?: ""
    )
}


fun Setting.toResponse(): SettingResponse {
    return SettingResponse(
        id = this.id,
        userId = this.user.id,
        text = this.text
    )
}


fun Task.toResponse(): TaskResponse {
    return TaskResponse(
        id = this.id,
        name = this.name,
        description = this.description,
        startTime = this.startTime,
        duration = this.duration.seconds,
        isDone = this.isDone
    )
}
