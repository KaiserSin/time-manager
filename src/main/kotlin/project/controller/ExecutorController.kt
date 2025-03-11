package project.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.model.executor.Executor
import project.model.executor.dto.ExecutorRequest
import project.model.executor.dto.ExecutorResponse
import project.service.ExecutorService

@RestController
@RequestMapping("/executors")
class ExecutorController(
    private val executorService: ExecutorService
) {
    @PostMapping
    fun createExecutor(
        @RequestBody requestDto: ExecutorRequest
    ): ResponseEntity<Any> {
        return try {
            val executor = Executor(
                name = requestDto.name
            )
            val savedExecutor = executorService.createExecutor(executor)
            val response = ExecutorResponse(
                id = savedExecutor.id!!,
                name = savedExecutor.name ?: ""
            )
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating executor: ${e.message}")
        }
    }

    @GetMapping("/{id}")
    fun getExecutor(@PathVariable id: Long): ResponseEntity<Any> {
        val executorOpt = executorService.findById(id)
        return if (executorOpt.isPresent) {
            val executor = executorOpt.get()
            val response = ExecutorResponse(
                id = executor.id!!,
                name = executor.name ?: ""
            )
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Executor with ID $id not found")
        }
    }

    @GetMapping
    fun getAllExecutors(): ResponseEntity<List<ExecutorResponse>> {
        val executors = executorService.findAllExecutors()
        val responseList = executors.map { ex ->
            ExecutorResponse(
                id = ex.id!!,
                name = ex.name ?: ""
            )
        }
        return ResponseEntity.ok(responseList)
    }

    @PutMapping
    fun updateExecutor(
        @RequestBody requestDto: ExecutorRequest
    ): ResponseEntity<Any> {
        return try {
            val idFromRequest = requestDto.id
                ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("ID must be provided for update")
            val existingExecutor = executorService.findById(idFromRequest)
                .orElseThrow { NoSuchElementException("Executor with ID $idFromRequest not found") }
            val updatedExecutor = existingExecutor.copy(
                name = requestDto.name
            )
            val savedExecutor = executorService.updateExecutor(updatedExecutor)
            val response = ExecutorResponse(
                id = savedExecutor.id!!,
                name = savedExecutor.name ?: ""
            )
            ResponseEntity.ok(response)
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating executor: ${e.message}")
        }
    }

    @DeleteMapping("/{id}")
    fun deleteExecutor(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            executorService.deleteExecutor(id)
            ResponseEntity.ok("Executor with ID $id deleted successfully")
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Executor with ID $id not found")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting executor: ${e.message}")
        }
    }
}
