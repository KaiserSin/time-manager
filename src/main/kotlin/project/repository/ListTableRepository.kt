package project.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.model.listtable.ListTable

@Repository
interface ListTableRepository : JpaRepository<ListTable, Long> {
    fun findByTaskId(taskId: Long): List<ListTable>
    fun findByExecutorId(executorId: Long): List<ListTable>
}
