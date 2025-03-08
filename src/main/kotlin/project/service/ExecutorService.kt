package project.service

import org.springframework.stereotype.Service
import project.model.Executor
import project.repository.ExecutorRepository

@Service
class ExecutorService(private val executorRepository: ExecutorRepository) {

    fun createExecutor(name: String): Executor {
        val executor = Executor(name = name)
        return executorRepository.save(executor)
    }
}
