package project.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import project.exception.EntityNotFoundException
import project.model.task.Task
import project.model.task.dto.TaskRequest
import project.repository.TaskRepository
import project.repository.UserRepository
import java.time.Duration
import java.time.LocalDateTime

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val listTableService: ListTableService
) {

    @Transactional
    suspend fun createTask(taskRequest: TaskRequest): Task {
        val user = userRepository.findById(taskRequest.userId)
            .orElseThrow {
                EntityNotFoundException("User with id ${taskRequest.userId} not found")
            }
        val startTime = requireNotNull(taskRequest.startTime) {
            "startTime cannot be null"
        }
        val task = Task(
            name = taskRequest.name ?: "",
            description = taskRequest.description ?: "",
            startTime = LocalDateTime.parse(startTime),
            duration = Duration.ofSeconds(taskRequest.duration ?: 60),
            isDone = taskRequest.isDone ?: false
        )
        val savedTask = taskRepository.save(task)
        listTableService.addTaskToExecutor(savedTask, user)
        return savedTask
    }

    fun getTaskById(id: Long): Task? {
        return taskRepository.findById(id).orElse(null)
    }

    fun getTaskByIdOrThrow(id: Long): Task {
        return getTaskById(id)
            ?: throw EntityNotFoundException("Task with id $id not found")
    }

    @Transactional
    fun deleteTask(id: Long) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id)
        } else {
            throw EntityNotFoundException("Task with id $id not found")
        }
    }

    fun getTasksByUserId(userId: Long, pageable: Pageable): Page<Task> {
        return taskRepository.findAllByUserId(userId, pageable)
    }

    @Transactional
    fun updateTask(id: Long, taskRequest: TaskRequest): Task {
        val existingTask = getTaskByIdOrThrow(id)
        val startTime = requireNotNull(taskRequest.startTime) {
            "startTime cannot be null for update"
        }

        val updatedTask = existingTask.copy(
            name = taskRequest.name ?: "",
            description = taskRequest.description ?: "",
            startTime = LocalDateTime.parse(startTime),
            duration = Duration.ofSeconds(taskRequest.duration ?: 60),
            isDone = taskRequest.isDone ?: false
        )

        val savedTask = taskRepository.save(updatedTask)

        return savedTask
    }

    fun getTasksForChatGPT(userId: Long, afterTime: LocalDateTime): List<Task> {
        return taskRepository.findAllByUserIdAndStartTimeAfter(userId, afterTime)
    }
}
