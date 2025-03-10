package project.service

import org.springframework.stereotype.Service
import project.repository.SettingRepository

@Service
class SettingService(private val settingRepository: SettingRepository) {

    fun getSettingsForChatGPT(executorId: Long): List<String>{
        return settingRepository.findByExecutorId(executorId).map {
            setting ->
            """
               **Customization**: ${setting.text} 
            """.trimIndent()
        }
    }
}