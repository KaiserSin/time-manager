package project.service

import org.springframework.stereotype.Service
import project.model.task.Task
import project.repository.TaskRepository

@Service
class TaskService(private val taskRepository: TaskRepository) {

    fun getAllTasks(): List<Task> = taskRepository.findAll()

    fun getTaskById(id: Long): Task? = taskRepository.findById(id).orElse(null)

    fun createTask(task: Task): Task = taskRepository.save(task)

    fun deleteTask(id: Long) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id)
        } else {
            throw NoSuchElementException()
        }
    }
}
