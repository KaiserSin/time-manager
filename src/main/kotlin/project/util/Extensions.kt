package project.util

import project.model.executor.Executor
import project.model.executor.dto.ExecutorResponse
import project.model.setting.Setting
import project.model.setting.dto.SettingResponse
import project.model.task.Task
import project.model.task.dto.TaskResponse


fun Executor.toResponse(): ExecutorResponse {
    return ExecutorResponse(
        id = this.id ?: 0L,
        name = this.name ?: ""
    )
}


fun Setting.toResponse(): SettingResponse {
    return SettingResponse(
        id = this.id,
        executorId = this.executor.id,
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
