package project.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.model.Executor
import project.service.ExecutorService

@RestController
@RequestMapping("/executors")
class UserController(private val executorService: ExecutorService) {

    @PostMapping("/create")
    fun createExecutor(@RequestParam("name") name: String): ResponseEntity<Any> {
        return try {
            val executor = executorService.createExecutor(name)
            ResponseEntity.status(HttpStatus.CREATED).body("Executor created with ID: ${executor.id}")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating executor: ${e.message}")
        }
    }
}
