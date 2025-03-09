package project.service

import org.springframework.stereotype.Service
import project.model.task.Task
import project.repository.TaskRepository
import project.repository.ListTableRepository
import java.time.LocalDateTime

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val listTableRepository: ListTableRepository
) {

    fun getAllTasks(): List<Task> = taskRepository.findAll()

    fun getTaskById(id: Long): Task? = taskRepository.findById(id).orElse(null)

    suspend fun createTask(task: Task): Task = taskRepository.save(task)

    fun deleteTask(id: Long) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id)
        } else {
            throw NoSuchElementException()
        }
    }

    fun getTasksForChatGPT(executorId: Long, startTime: LocalDateTime? = null): List<String> {
        val tasks = if (startTime != null) {
            listTableRepository.findByExecutorId(executorId)
                .map { it.task }
                .filter { it.startTime.isAfter(startTime) }
        } else {
            listTableRepository.findByExecutorId(executorId)
                .map { it.task }
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
