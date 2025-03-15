package project.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.model.setting.Setting

@Repository
interface SettingRepository : JpaRepository<Setting, Long> {
    fun findByExecutorId(executorId: Long, pageable: Pageable): Page<Setting>

    fun findByExecutorId(executorId: Long): List<Setting>
}
