package project.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
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

    fun createTask(taskRequest: TaskRequest): Task {
        val executor = executorRepository.findById(taskRequest.executorId).orElseThrow {
            NoSuchElementException("Executor with id ${taskRequest.executorId} not found")
        }

        val task = Task(
            name = taskRequest.name,
            description = taskRequest.description,
            startTime = LocalDateTime.parse(taskRequest.startTime),
            duration = Duration.ofSeconds(taskRequest.duration),
            isDone = taskRequest.isDone
        )

        return taskRepository.save(task)
    }

    fun getTaskById(id: Long): Task? {
        return taskRepository.findById(id).orElse(null)
    }

    fun deleteTask(id: Long) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id)
        } else {
            throw NoSuchElementException("Task with id $id not found")
        }
    }

    fun getTasksByExecutorId(executorId: Long, pageable: Pageable): Page<Task> {
        return taskRepository.findAllByExecutorId(executorId, pageable)
    }

    fun updateTask(id: Long, taskRequest: TaskRequest): Task {
        val existingTask = taskRepository.findById(id).orElseThrow {
            NoSuchElementException("Task with id $id not found")
        }

        val updatedTask = existingTask.copy(
            name = taskRequest.name,
            description = taskRequest.description,
            startTime = LocalDateTime.parse(taskRequest.startTime),
            duration = Duration.ofSeconds(taskRequest.duration),
            isDone = taskRequest.isDone
        )

        return taskRepository.save(updatedTask)
    }
}
