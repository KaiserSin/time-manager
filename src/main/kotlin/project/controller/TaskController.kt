package project.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.model.executor.Executor
import project.model.task.Task
import project.model.task.dto.TaskRequest
import project.model.task.dto.TaskResponse
import project.service.GptService
import project.service.ListTableService
import project.service.TaskService
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RestController
@RequestMapping("/tasks")
class TaskController(
    private val taskService: TaskService,
    private val gptService: GptService,
    private val listTableService: ListTableService
) {

    @GetMapping
    fun getTasks(): List<Task> {
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

    @PostMapping
    suspend fun postTask(@RequestBody taskRequest: TaskRequest): ResponseEntity<Any> {
        return try {
            val finalStartTime: LocalDateTime = if (!taskRequest.startTime.isNullOrBlank()) {
                LocalDateTime.parse(taskRequest.startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            } else {
                gptService.getOptimalStartTime(
                    taskRequest.name,
                    taskRequest.description,
                    Duration.ofSeconds(taskRequest.duration),
                    taskRequest.gptDescription ?: "None",
                    taskRequest.executorId
                )
            }

            val task = taskService.createTask(
                Task(
                    id = null,
                    name = taskRequest.name,
                    description = taskRequest.description,
                    startTime = finalStartTime,
                    duration = Duration.ofSeconds(taskRequest.duration),
                    isDone = false
                )
            )

            val executor = Executor(id = taskRequest.executorId)
            listTableService.addTaskToExecutor(task, executor)

            val taskResponse = TaskResponse(
                id = task.id,
                name = task.name,
                description = task.description,
                startTime = task.startTime,
                duration = task.duration.seconds,
                isDone = task.isDone
            )

            ResponseEntity.status(HttpStatus.CREATED).body(taskResponse)
        } catch (e: DateTimeParseException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in start time format: ${e.message}")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: ${e.message}")
        }
    }

    @PutMapping("/{id}")
    suspend fun updateTask(
        @PathVariable id: Long,
        @RequestBody taskRequest: TaskRequest
    ): ResponseEntity<Any> {
        return try {
            val existingTask = taskService.getTaskById(id)
                ?: return ResponseEntity.notFound().build()

            val finalStartTime: LocalDateTime = if (taskRequest.startTime.isNullOrBlank()) {
                gptService.getOptimalStartTime(
                    taskRequest.name,
                    taskRequest.description,
                    Duration.ofSeconds(taskRequest.duration),
                    taskRequest.gptDescription ?: "None",
                    taskRequest.executorId
                )
            } else {
                LocalDateTime.parse(
                    taskRequest.startTime,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                )
            }

            val updatedTask = existingTask.copy(
                name = taskRequest.name,
                description = taskRequest.description,
                startTime = finalStartTime,
                duration = Duration.ofSeconds(taskRequest.duration),
                isDone = taskRequest.isDone
            )

            val savedTask = taskService.updateTask(updatedTask)
            val executor = Executor(id = taskRequest.executorId)
            listTableService.addTaskToExecutor(savedTask, executor)


            val taskResponse = TaskResponse(
                id = savedTask.id,
                name = savedTask.name,
                description = savedTask.description,
                startTime = savedTask.startTime,
                duration = savedTask.duration.seconds,
                isDone = savedTask.isDone
            )

            ResponseEntity.ok(taskResponse)
        } catch (e: DateTimeParseException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error in start time format: ${e.message}")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: ${e.message}")
        }
    }


}
