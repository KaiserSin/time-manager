package project.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.model.executor.dto.ExecutorRequest
import project.model.executor.dto.ExecutorResponse
import project.service.ExecutorService
import project.util.toResponse

@RestController
@RequestMapping("/executors")
class ExecutorController(
    private val executorService: ExecutorService
) {

    @PostMapping
    fun createExecutor(@RequestBody executorRequest: ExecutorRequest): ResponseEntity<ExecutorResponse> {
        val createdExecutor = executorService.createExecutor(executorRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExecutor.toResponse())
    }

    @DeleteMapping("/{id}")
    fun deleteExecutor(@PathVariable id: Long): ResponseEntity<String> {
        executorService.deleteExecutor(id)
        return ResponseEntity.ok("Executor with ID $id has been successfully deleted.")
    }

    @GetMapping("/{id}")
    fun getExecutorById(@PathVariable id: Long): ResponseEntity<ExecutorResponse> {
        val executor = executorService.getExecutorByIdOrThrow(id)
        return ResponseEntity.ok(executor.toResponse())
    }

    @GetMapping
    fun getAllExecutors(
        @PageableDefault(size = 10) pageable: Pageable
    ): ResponseEntity<Page<ExecutorResponse>> {
        val executorPage = executorService.getAllExecutors(pageable)
        val responsePage = executorPage.map { it.toResponse() }
        return ResponseEntity.ok(responsePage)
    }

    @PutMapping("/{id}")
    fun updateExecutor(
        @PathVariable id: Long,
        @RequestBody executorRequest: ExecutorRequest
    ): ResponseEntity<ExecutorResponse> {
        val updatedExecutor = executorService.updateExecutor(id, executorRequest)
        return ResponseEntity.ok(updatedExecutor.toResponse())
    }
}
