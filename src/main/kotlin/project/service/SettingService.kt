package project.service

import org.springframework.stereotype.Service
import project.controller.SettingController.SettingResponse
import project.controller.SettingController.SettingRequest
import project.model.Setting
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

    fun getAllSettings(): List<SettingResponse> {
        return settingRepository.findAll().map { setting ->
            SettingResponse(
                id = setting.id,
                executorId = setting.executor.id,
                text = setting.text
            )
        }
    }

    fun getSettingById(id: Long): SettingResponse? {
        return settingRepository.findById(id).map { setting ->
            SettingResponse(
                id = setting.id,
                executorId = setting.executor.id,
                text = setting.text
            )
        }.orElse(null)
    }


    fun deleteSettingInDb(id: Long) {
        if (settingRepository.existsById(id)) {
            settingRepository.deleteById(id)
        } else {
            throw NoSuchElementException("Setting with id $id not found")
        }
    }

    fun getSettingsForChatGPT(executorId: Long): List<String> {
        return settingRepository.findByExecutorId(executorId).map { setting ->
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
