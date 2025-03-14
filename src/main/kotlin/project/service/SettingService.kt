package project.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
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
        val executor = executorRepository.findById(settingRequest.executorId).orElseThrow {
            NoSuchElementException("Executor with id ${settingRequest.executorId} not found")
        }

        val setting = Setting(executor = executor, text = settingRequest.text)
        return settingRepository.save(setting)
    }

    fun getSettingById(id: Long): Setting? {
        return settingRepository.findById(id).orElse(null)
    }

    fun deleteSettingInDb(id: Long) {
        if (settingRepository.existsById(id)) {
            settingRepository.deleteById(id)
        } else {
            throw NoSuchElementException("Setting with id $id not found")
        }
    }

    fun getSettingsByExecutorId(executorId: Long, pageable: Pageable): Page<Setting> {
        return settingRepository.findByExecutorId(executorId, pageable)
    }

    fun getSettingsForChatGPT(executorId: Long, pageable: Pageable): Page<String> {
        return settingRepository.findByExecutorId(executorId, pageable).map { setting ->
            "**Customization**: ${setting.text}"
        }
    }

    fun updateSetting(id: Long, settingRequest: SettingRequest): Setting {
        val existingSetting = settingRepository.findById(id).orElseThrow {
            NoSuchElementException("Setting with id $id not found")
        }

        val executor = executorRepository.findById(settingRequest.executorId).orElseThrow {
            NoSuchElementException("Executor with id ${settingRequest.executorId} not found")
        }

        val updatedEntity = existingSetting.copy(
            executor = executor,
            text = settingRequest.text
        )

        return settingRepository.save(updatedEntity)
    }
}
