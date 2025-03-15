package project.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import project.model.task.Task
import java.time.LocalDateTime


@Repository
interface TaskRepository : JpaRepository<Task, Long> {

    @Query("""
        SELECT t FROM Task t 
        JOIN ListTable l ON t.id = l.task.id 
        WHERE l.executor.id = :executorId
    """)
    fun findAllByExecutorId(@Param("executorId") executorId: Long, pageable: Pageable): Page<Task>


    @Query("""
        SELECT t FROM Task t
        JOIN ListTable l ON t.id = l.task.id
        WHERE l.executor.id = :executorId
        AND t.startTime > :afterTime
        ORDER BY t.startTime ASC
    """)
    fun findAllByExecutorIdAndStartTimeAfter(
        @Param("executorId") executorId: Long,
        @Param("afterTime") afterTime: LocalDateTime
    ): List<Task>
}