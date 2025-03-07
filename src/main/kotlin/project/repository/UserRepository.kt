package project.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.model.User

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByName(name: String): User?
}