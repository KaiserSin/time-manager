package project.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import project.exception.EntityNotFoundException
import project.model.task.Task
import project.model.task.dto.TaskRequest
import project.repository.ExecutorRepository
import project.repository.TaskRepository
import java.time.Duration
import java.time.LocalDateTime

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val executorRepository: ExecutorRepository
) {

    /**
     * Создать новую задачу (Task).
     */
    fun createTask(taskRequest: TaskRequest): Task {
        // Проверяем, что executor существует
        val executor = executorRepository.findById(taskRequest.executorId)
            .orElseThrow {
                EntityNotFoundException("Executor with id ${taskRequest.executorId} not found")
            }

        // Преобразуем стартовое время и продолжительность
        val startTime = requireNotNull(taskRequest.startTime) {
            "startTime cannot be null for task creation"
        }

        val task = Task(
            name = taskRequest.name,
            description = taskRequest.description,
            startTime = LocalDateTime.parse(startTime),
            duration = Duration.ofSeconds(taskRequest.duration),
            isDone = taskRequest.isDone
        )
        return taskRepository.save(task)
    }

    fun getTaskById(id: Long): Task? {
        return taskRepository.findById(id).orElse(null)
    }

    fun getTaskByIdOrThrow(id: Long): Task {
        return getTaskById(id)
            ?: throw EntityNotFoundException("Task with id $id not found")
    }

    fun deleteTask(id: Long) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id)
        } else {
            throw EntityNotFoundException("Task with id $id not found")
        }
    }

    fun getTasksByExecutorId(executorId: Long, pageable: Pageable): Page<Task> {
        return taskRepository.findAllByExecutorId(executorId, pageable)
    }

    fun updateTask(id: Long, taskRequest: TaskRequest): Task {
        val existingTask = getTaskByIdOrThrow(id)

        val startTime = requireNotNull(taskRequest.startTime) {
            "startTime cannot be null for task update"
        }

        val updatedTask = existingTask.copy(
            name = taskRequest.name,
            description = taskRequest.description,
            startTime = LocalDateTime.parse(startTime),
            duration = Duration.ofSeconds(taskRequest.duration),
            isDone = taskRequest.isDone
        )
        return taskRepository.save(updatedTask)
    }

    fun getTasksForChatGPT(executorId: Long, afterTime: LocalDateTime): List<Task> {
        return taskRepository.findAllByExecutorIdAndStartTimeAfter(executorId, afterTime)
    }
}
