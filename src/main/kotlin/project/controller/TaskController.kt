package project.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.model.task.Task
import project.service.GptService
import project.service.TaskService
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RestController
@RequestMapping("/tasks")
class TaskController(
    private val taskService: TaskService,
    private val gptService: GptService
    ) {

    @GetMapping()
    fun getTasks(): List<Task>{
        return taskService.getAllTasks()
    }

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable id: Long) = taskService.getTaskById(id)

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<Unit> {
        return try {
            taskService.deleteTask(id)
            ResponseEntity.noContent().build()
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping()
    fun postTask(
        @RequestParam("name") taskName: String,
        @RequestParam("description") taskDescription: String,
        @RequestParam(value = "start-time", defaultValue = "None") taskStartTime: String,
        @RequestParam("duration") taskDuration: Long,
        @RequestParam(value = "gpt-description", defaultValue = "None") taskGptDescription: String,
        @RequestParam("executor-id") executorId: Long
    ): ResponseEntity<Any> {
        return try {
            val finalStartTime: LocalDateTime = if (taskStartTime != "None") {
                LocalDateTime.parse(taskStartTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            } else {
                gptService.getOptimalStartTime(taskName, taskDescription, Duration.ofSeconds(taskDuration), taskGptDescription, executorId)
            }
            val task = Task(
                id = null,
                name = taskName,
                description = taskDescription,
                startTime = finalStartTime,
                duration = Duration.ofSeconds(taskDuration),
                isDone = false
            )
            taskService.createTask(task)

            ResponseEntity.status(HttpStatus.OK).body("Task successfully added with start time: $finalStartTime")
        } catch (e: DateTimeParseException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in start time format: ${e.message}")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: ${e.message}")
        }
    }



}