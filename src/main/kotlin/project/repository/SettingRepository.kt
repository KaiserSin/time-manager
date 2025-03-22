package project.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.model.setting.Setting

@Repository
interface SettingRepository : JpaRepository<Setting, Long> {
    fun findByUserId(userId: Long, pageable: Pageable): Page<Setting>

    fun findByUserId(userId: Long): List<Setting>
}
