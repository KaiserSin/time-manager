package project.model.setting.dto

data class SettingResponse(
    val id: Long,
    val userId: Long?,
    val text: String
)

