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
import project.util.toResponse

@RestController
@RequestMapping("/settings")
class SettingController(
    private val settingService: SettingService
) {

    @PostMapping
    fun createSetting(@RequestBody settingRequest: SettingRequest): ResponseEntity<SettingResponse> {
        val createdSetting = settingService.createSettingInDb(settingRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSetting.toResponse())
    }

    @DeleteMapping("/{id}")
    fun deleteSetting(@PathVariable id: Long): ResponseEntity<String> {
        settingService.deleteSettingInDb(id)
        return ResponseEntity.ok("Setting with ID $id has been successfully deleted.")
    }

    @GetMapping("/{id}")
    fun getSettingById(@PathVariable id: Long): ResponseEntity<SettingResponse> {
        val setting = settingService.getSettingByIdOrThrow(id)
        return ResponseEntity.ok(setting.toResponse())
    }

    @GetMapping("/executor/{executorId}")
    fun getSettingsByExecutorId(
        @PathVariable executorId: Long,
        @PageableDefault(size = 10) pageable: Pageable
    ): ResponseEntity<Page<SettingResponse>> {
        val settingPage = settingService.getSettingsByExecutorId(executorId, pageable)
        val responsePage = settingPage.map { it.toResponse() }
        return ResponseEntity.ok(responsePage)
    }

    @PutMapping("/{id}")
    fun updateSetting(
        @PathVariable id: Long,
        @RequestBody settingRequest: SettingRequest
    ): ResponseEntity<SettingResponse> {
        val updatedSetting = settingService.updateSetting(id, settingRequest)
        return ResponseEntity.ok(updatedSetting.toResponse())
    }
}
