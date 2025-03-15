package project.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import project.exception.EntityNotFoundException
import project.model.setting.Setting
import project.model.setting.dto.SettingRequest
import project.repository.ExecutorRepository
import project.repository.SettingRepository

@Service
class SettingService(
    private val settingRepository: SettingRepository,
    private val executorRepository: ExecutorRepository
) {

    fun createSettingInDb(settingRequest: SettingRequest): Setting {
        val executor = executorRepository.findById(settingRequest.executorId)
            .orElseThrow {
                EntityNotFoundException("Executor with id ${settingRequest.executorId} not found")
            }
        val setting = Setting(executor = executor, text = settingRequest.text)
        return settingRepository.save(setting)
    }

    fun getSettingById(id: Long): Setting? {
        return settingRepository.findById(id).orElse(null)
    }

    fun getSettingByIdOrThrow(id: Long): Setting {
        return getSettingById(id)
            ?: throw EntityNotFoundException("Setting with id $id not found")
    }

    fun deleteSettingInDb(id: Long) {
        if (settingRepository.existsById(id)) {
            settingRepository.deleteById(id)
        } else {
            throw EntityNotFoundException("Setting with id $id not found")
        }
    }

    fun getSettingsByExecutorId(executorId: Long, pageable: Pageable): Page<Setting> {
        return settingRepository.findByExecutorId(executorId, pageable)
    }

    fun getSettingsForChatGPT(executorId: Long): List<String> {
        return settingRepository.findByExecutorId(executorId).map { setting ->
            "**Customization**: ${setting.text}"
        }
    }

    fun updateSetting(id: Long, settingRequest: SettingRequest): Setting {
        val existingSetting = getSettingByIdOrThrow(id)
        val executor = executorRepository.findById(settingRequest.executorId)
            .orElseThrow {
                EntityNotFoundException("Executor with id ${settingRequest.executorId} not found")
            }
        val updatedEntity = existingSetting.copy(
            executor = executor,
            text = settingRequest.text
        )
        return settingRepository.save(updatedEntity)
    }
}
