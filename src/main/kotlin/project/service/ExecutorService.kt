package project.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import project.model.executor.Executor
import project.model.executor.dto.ExecutorRequest
import project.repository.ExecutorRepository

@Service
class ExecutorService(
    private val executorRepository: ExecutorRepository
) {

    fun createExecutor(executorRequest: ExecutorRequest): Executor {
        val executor = Executor(name = executorRequest.name)
        return executorRepository.save(executor)
    }

    fun getExecutorById(id: Long): Executor? {
        return executorRepository.findById(id).orElse(null)
    }

    fun deleteExecutor(id: Long) {
        if (executorRepository.existsById(id)) {
            executorRepository.deleteById(id)
        } else {
            throw NoSuchElementException("Executor with id $id not found")
        }
    }

    fun getAllExecutors(pageable: Pageable): Page<Executor> {
        return executorRepository.findAll(pageable)
    }

    fun updateExecutor(id: Long, executorRequest: ExecutorRequest): Executor {
        val existingExecutor = executorRepository.findById(id).orElseThrow {
            NoSuchElementException("Executor with id $id not found")
        }

        val updatedExecutor = existingExecutor.copy(
            name = executorRequest.name
        )

        return executorRepository.save(updatedExecutor)
    }
}
