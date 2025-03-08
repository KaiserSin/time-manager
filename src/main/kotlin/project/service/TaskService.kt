package project.service

import org.springframework.stereotype.Service
import project.model.task.Task
import project.repository.TaskRepository
import java.time.LocalDateTime

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

    fun getTasksForChatGPT(executorId: Long, startTime: LocalDateTime? = null): List<String> {
        val tasks = if (startTime != null) {
            taskRepository.findAllByExecutorId(executorId, startTime)
        } else {
            taskRepository.findAllByExecutorId(executorId)
        }

        return tasks.map { task ->
            """
            **Задача:** ${task.name}
            **Описание:** ${task.description}
            **Начало:** ${task.startTime}
            **Длительность:** ${task.duration}
            **Статус:** ${if (task.isDone) "✅ Выполнено" else "⏳ В процессе"}
            """.trimIndent()
        }
    }
}
