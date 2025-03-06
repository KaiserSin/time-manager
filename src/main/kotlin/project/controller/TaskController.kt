package project.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.model.Task
import project.service.TaskService

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

//    @PostMapping()
//    fun postTask(){
//
//    }
}