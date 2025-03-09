package project.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import project.model.task.Task
import java.time.LocalDateTime

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    @Query(
        """
    SELECT t FROM Task t
    JOIN ListTable l ON t.id = l.task.id
    WHERE l.executor.id = :executorId
    AND (COALESCE(:startTime, t.startTime) = t.startTime OR t.startTime >= :startTime)
    """
    )
    fun findAllByExecutorId(
        @Param("executorId") executorId: Long,
        @Param("startTime") startTime: LocalDateTime? = null
    ): List<Task>


}
