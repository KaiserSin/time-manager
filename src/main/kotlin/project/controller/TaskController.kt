package project.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import project.model.task.dto.TaskRequest
import project.model.task.dto.TaskResponse
import project.service.TaskService
import project.util.OnCreate
import project.util.OnUpdate
import project.util.toResponse

@RestController
@RequestMapping("/tasks")
class TaskController(
    private val taskService: TaskService
) {

    @PostMapping
    fun createTask(
        @Validated(OnCreate::class)@RequestBody taskRequest: TaskRequest
    ): ResponseEntity<TaskResponse> {
        val createdTask = taskService.createTask(taskRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask.toResponse())
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<String> {
        taskService.deleteTask(id)
        return ResponseEntity.ok("Task with ID $id has been successfully deleted.")
    }

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable id: Long): ResponseEntity<TaskResponse> {
        val task = taskService.getTaskByIdOrThrow(id)
        return ResponseEntity.ok(task.toResponse())
    }

    @GetMapping("/executor/{executorId}")
    fun getTasksByExecutorId(
        @PathVariable executorId: Long,
        @PageableDefault(size = 10) pageable: Pageable
    ): ResponseEntity<Page<TaskResponse>> {
        val taskPage = taskService.getTasksByExecutorId(executorId, pageable)
        val responsePage = taskPage.map { it.toResponse() }
        return ResponseEntity.ok(responsePage)
    }

    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @Validated(OnUpdate::class) @RequestBody taskRequest: TaskRequest
    ): ResponseEntity<TaskResponse> {
        val updatedTask = taskService.updateTask(id, taskRequest)
        return ResponseEntity.ok(updatedTask.toResponse())
    }
}
