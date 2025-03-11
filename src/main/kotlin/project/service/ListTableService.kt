package project.service

import org.springframework.stereotype.Service
import project.model.executor.Executor
import project.model.list_table.ListTable
import project.model.task.Task
import project.repository.ListTableRepository

@Service
class ListTableService(private val listTableRepository: ListTableRepository) {

    suspend fun addTaskToExecutor(task: Task, executor: Executor): ListTable {
        val listTableEntry = ListTable(task = task, executor = executor)
        return listTableRepository.save(listTableEntry)
    }

    fun getTasksByExecutor(executorId: Long): List<ListTable> {
        return listTableRepository.findByExecutorId(executorId)
    }

    fun getExecutorsByTask(taskId: Long): List<ListTable> {
        return listTableRepository.findByTaskId(taskId)
    }

    fun removeTaskFromExecutor(id: Long) {
        listTableRepository.deleteById(id)
    }
}
