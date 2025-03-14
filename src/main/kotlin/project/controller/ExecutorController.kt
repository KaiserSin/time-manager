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

@RestController
@RequestMapping("/executors")
class ExecutorController(
    private val executorService: ExecutorService
) {

    @PostMapping
    fun createExecutor(@RequestBody executorRequest: ExecutorRequest): ResponseEntity<ExecutorResponse> {
        return try {
            val createdExecutor = executorService.createExecutor(executorRequest)
            val response = ExecutorResponse(
                id = createdExecutor.id!!,
                name = createdExecutor.name ?: ""
            )
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteExecutor(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            executorService.deleteExecutor(id)
            ResponseEntity.ok("Executor with ID $id has been successfully deleted.")
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Executor with ID $id not found.")
        }
    }

    @GetMapping("/{id}")
    fun getExecutorById(@PathVariable id: Long): ResponseEntity<ExecutorResponse> {
        val executor = executorService.getExecutorById(id)
            ?: return ResponseEntity.notFound().build()

        val response = ExecutorResponse(
            id = executor.id!!,
            name = executor.name ?: ""
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getAllExecutors(
        @PageableDefault(size = 10) pageable: Pageable
    ): ResponseEntity<Page<ExecutorResponse>> {
        val executorPage = executorService.getAllExecutors(pageable)
        val responsePage = executorPage.map { executor ->
            ExecutorResponse(
                id = executor.id!!,
                name = executor.name ?: ""
            )
        }
        return ResponseEntity.ok(responsePage)
    }

    @PutMapping("/{id}")
    fun updateExecutor(@PathVariable id: Long, @RequestBody executorRequest: ExecutorRequest): ResponseEntity<ExecutorResponse> {
        return try {
            val updatedExecutor = executorService.updateExecutor(id, executorRequest)
            val response = ExecutorResponse(
                id = updatedExecutor.id!!,
                name = updatedExecutor.name ?: ""
            )
            ResponseEntity.ok(response)
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}
