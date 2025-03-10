package project.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.model.Setting

@Repository
interface SettingRepository: JpaRepository<Setting, Long> {
    fun findByExecutorId(executorId: Long): List<Setting>
}