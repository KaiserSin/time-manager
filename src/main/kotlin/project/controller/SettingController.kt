package project.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.model.setting.dto.SettingRequest
import project.model.setting.dto.SettingResponse
import project.service.SettingService

@RestController
@RequestMapping("/settings")
class SettingController(private val settingService: SettingService) {

    @PostMapping
    fun createSetting(@RequestBody setting: SettingRequest): ResponseEntity<SettingResponse> {
        return try {
            val createdSetting = settingService.createSettingInDb(setting)
            val response = SettingResponse(
                id = createdSetting.id,
                executorId = createdSetting.executor.id,
                text = createdSetting.text
            )
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteSetting(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            settingService.deleteSettingInDb(id)
            ResponseEntity.ok("Setting with ID $id has been successfully deleted.")
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Setting with ID $id not found.")
        }
    }

    @GetMapping("/{id}")
    fun getSettingById(@PathVariable id: Long): ResponseEntity<SettingResponse> {
        val setting = settingService.getSettingById(id)
            ?: return ResponseEntity.notFound().build()

        val response = SettingResponse(
            id = setting.id,
            executorId = setting.executor.id,
            text = setting.text
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/executor/{executorId}")
    fun getSettingsByExecutorId(
        @PathVariable executorId: Long,
        @PageableDefault(size = 10) pageable: Pageable
    ): ResponseEntity<Page<SettingResponse>> {
        val settingPage = settingService.getSettingsByExecutorId(executorId, pageable)
        val responsePage = settingPage.map { setting ->
            SettingResponse(
                id = setting.id,
                executorId = setting.executor?.id,
                text = setting.text
            )
        }
        return ResponseEntity.ok(responsePage)
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
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}
