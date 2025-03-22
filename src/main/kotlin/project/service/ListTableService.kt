package project.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import project.model.listtable.ListTable
import project.model.task.Task
import project.model.user.User
import project.repository.ListTableRepository

@Service
class ListTableService(
    private val listTableRepository: ListTableRepository
) {

    @Transactional
    suspend fun addTaskToExecutor(task: Task, user: User): ListTable {
        val listTableEntry = ListTable(task = task, user = user)
        return listTableRepository.save(listTableEntry)
    }

    fun getTasksByUser(userId: Long): List<ListTable> {
        return listTableRepository.findAllByUserId(userId)
    }

    fun getUsersByTask(taskId: Long): List<ListTable> {
        return listTableRepository.findAllByTaskId(taskId)
    }

    @Transactional
    fun removeTaskFromExecutor(id: Long) {
        listTableRepository.deleteById(id)
    }
}
