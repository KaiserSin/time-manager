package project.service

import org.springframework.stereotype.Service
import project.model.Task
import project.repository.TaskRepository

@Service
class TaskService(private val taskRepository: TaskRepository) {

    fun getAllTasks(): List<Task> = taskRepository.findAll()

    fun getTaskById(id: Long): Task? = taskRepository.findById(id).orElse(null)
}