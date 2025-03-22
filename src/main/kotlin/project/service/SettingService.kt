package project.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import project.exception.EntityNotFoundException
import project.model.setting.Setting
import project.model.setting.dto.SettingRequest
import project.repository.SettingRepository
import project.repository.UserRepository

@Service
class SettingService(
    private val settingRepository: SettingRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun createSettingInDb(settingRequest: SettingRequest): Setting {
        val user = userRepository.findById(settingRequest.userId)
            .orElseThrow {
                EntityNotFoundException("User with id ${settingRequest.userId} not found")
            }
        val setting = Setting(user = user, text = settingRequest.text ?: "")
        return settingRepository.save(setting)
    }

    fun getSettingById(id: Long): Setting? {
        return settingRepository.findById(id).orElse(null)
    }

    fun getSettingByIdOrThrow(id: Long): Setting {
        return getSettingById(id)
            ?: throw EntityNotFoundException("Setting with id $id not found")
    }

    @Transactional
    fun deleteSettingInDb(id: Long) {
        if (settingRepository.existsById(id)) {
            settingRepository.deleteById(id)
        } else {
            throw EntityNotFoundException("Setting with id $id not found")
        }
    }

    fun getSettingsByUserId(userId: Long, pageable: Pageable): Page<Setting> {
        return settingRepository.findByUserId(userId, pageable)
    }

    fun getSettingsForChatGPT(userId: Long): List<String> {
        return settingRepository.findByUserId(userId).map { setting ->
            "**Customization**: ${setting.text}"
        }
    }

    @Transactional
    fun updateSetting(id: Long, settingRequest: SettingRequest): Setting {
        val existingSetting = getSettingByIdOrThrow(id)
        val user = userRepository.findById(settingRequest.userId)
            .orElseThrow {
                EntityNotFoundException("User with id ${settingRequest.userId} not found")
            }
        val updatedEntity = existingSetting.copy(
            user = user,
            text = settingRequest.text ?: ""
        )
        return settingRepository.save(updatedEntity)
    }
}
