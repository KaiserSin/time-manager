package project.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.model.Setting
import project.service.SettingService

@RestController
@RequestMapping("/settings")
class SettingController(private val settingService: SettingService) {

    @PostMapping
    fun createSetting(@RequestBody setting: SettingRequest): ResponseEntity<SettingResponse> {
        val createdSetting = settingService.createSettingInDb(setting)
        val response = SettingResponse(
            id = createdSetting.id,
            executorId = createdSetting.executor.id,
            text = createdSetting.text
        )
        return ResponseEntity.ok(response)
    }


    @DeleteMapping("/{id}")
    fun deleteSetting(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            settingService.deleteSettingInDb(id)
            ResponseEntity.ok("Setting with id $id has been successfully deleted.")
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAllSettings(): List<SettingResponse> {
        return settingService.getAllSettings()
    }

    @GetMapping("/{id}")
    fun getSetting(@PathVariable id: Long): ResponseEntity<SettingResponse> {
        val setting = settingService.getSettingById(id)
        return if (setting != null) ResponseEntity.ok(setting) else ResponseEntity.notFound().build()
    }


    @PutMapping("/{id}")
    fun updateSetting(@PathVariable id: Long, @RequestBody setting: SettingRequest): ResponseEntity<SettingResponse> {
        return try {
            val updatedSetting = settingService.updateSetting(id, setting)
            val response = SettingResponse(
                id = updatedSetting.id,
                executorId = updatedSetting.executor.id,
                text = updatedSetting.text
            )
            ResponseEntity.ok(response)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }


    data class SettingRequest(
        val executorId: Long,
        val text: String
    )

    data class SettingResponse(
        val id: Long,
        val executorId: Long?,
        val text: String
    )

}
