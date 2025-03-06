package project.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import project.model.Task
import project.service.TaskService

@RestController
@RequestMapping("/calendar")
class MainController(private val taskService: TaskService) {

    @GetMapping()
    fun getСalendar(): List<Task>{
        return taskService.getAllTasks()
    }

    @GetMapping("/oneEvent")
    fun getEvent(@RequestParam("event-id") event_id: Long): Task?{
        return taskService.getTaskById(event_id)
    }

    @PostMapping("/newEvent")
    fun postCalendar(){

    }


}