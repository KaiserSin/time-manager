package project.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.model.task.dto.TaskRequest
import project.model.task.dto.TaskResponse
import project.service.TaskService

@RestController
@RequestMapping("/tasks")
class TaskController(
    private val taskService: TaskService
) {

    @PostMapping
    fun createTask(@RequestBody taskRequest: TaskRequest): ResponseEntity<TaskResponse> {
        return try {
            val createdTask = taskService.createTask(taskRequest)
            val response = TaskResponse(
                id = createdTask.id,
                name = createdTask.name,
                description = createdTask.description,
                startTime = createdTask.startTime,
                duration = createdTask.duration.seconds,
                isDone = createdTask.isDone
            )
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            taskService.deleteTask(id)
            ResponseEntity.ok("Task with ID $id has been successfully deleted.")
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with ID $id not found.")
        }
    }

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable id: Long): ResponseEntity<TaskResponse> {
        val task = taskService.getTaskById(id)
            ?: return ResponseEntity.notFound().build()

        val response = TaskResponse(
            id = task.id,
            name = task.name,
            description = task.description,
            startTime = task.startTime,
            duration = task.duration.seconds,
            isDone = task.isDone
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/executor/{executorId}")
    fun getTasksByExecutorId(
        @PathVariable executorId: Long,
        @PageableDefault(size = 10) pageable: Pageable
    ): ResponseEntity<Page<TaskResponse>> {
        val taskPage = taskService.getTasksByExecutorId(executorId, pageable)
        val responsePage = taskPage.map { task ->
            TaskResponse(
                id = task.id,
                name = task.name,
                description = task.description,
                startTime = task.startTime,
                duration = task.duration.seconds,
                isDone = task.isDone
            )
        }
        return ResponseEntity.ok(responsePage)
    }

    @PutMapping("/{id}")
    fun updateTask(@PathVariable id: Long, @RequestBody taskRequest: TaskRequest): ResponseEntity<TaskResponse> {
        return try {
            val updatedTask = taskService.updateTask(id, taskRequest)
            val response = TaskResponse(
                id = updatedTask.id,
                name = updatedTask.name,
                description = updatedTask.description,
                startTime = updatedTask.startTime,
                duration = updatedTask.duration.seconds,
                isDone = updatedTask.isDone
            )
            ResponseEntity.ok(response)
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}
