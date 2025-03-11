package project.service

import org.springframework.stereotype.Service
import project.model.executor.Executor
import project.repository.ExecutorRepository
import java.util.Optional

@Service
class ExecutorService(
    private val executorRepository: ExecutorRepository
) {
    fun createExecutor(executor: Executor): Executor {
        return executorRepository.save(executor)
    }

    fun findAllExecutors(): List<Executor> {
        return executorRepository.findAll()
    }

    fun findById(id: Long): Optional<Executor> {
        return executorRepository.findById(id)
    }

    fun updateExecutor(executor: Executor): Executor {
        return executorRepository.save(executor)
    }

    fun deleteExecutor(id: Long) {
        val executor = executorRepository.findById(id)
            .orElseThrow { NoSuchElementException("Executor not found") }
        executorRepository.delete(executor)
    }
}
