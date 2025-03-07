package project.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.model.Message

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun findByUserId(userId: Long): List<Message>
}