package project.model.setting.dto

data class SettingResponse(
    val id: Long,
    val executorId: Long?,
    val text: String
)
