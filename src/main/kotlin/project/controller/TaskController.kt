package project.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.model.Task
import project.service.TaskService
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RestController
@RequestMapping("/tasks")
class TaskController(private val taskService: TaskService) {

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
    fun postTask(@RequestParam("name") task_name: String,
                 @RequestParam("description") task_description: String,
                 @RequestParam(value = "start-time", defaultValue = "None") task_start_time: String,
                 @RequestParam("duration") task_duration: Long,
                 @RequestParam(value = "gpt-description", defaultValue = "None") task_gpt_description: String): ResponseEntity<Any>{
        if (task_start_time!="None"){
            try {
                val task = Task(null,
                    task_name,
                    task_description,
                    LocalDateTime.parse(task_start_time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    Duration.ofSeconds(task_duration))
                taskService.createTask(task)
                return ResponseEntity.status(HttpStatus.OK).body("Task is successfully added")
            }
            catch (e: DateTimeParseException){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in start time format. Can not parse it. Error: ${e.message}")
            }

        }

    }

}