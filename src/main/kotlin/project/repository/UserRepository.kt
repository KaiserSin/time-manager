package project.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.model.user.User
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long>{

    fun findByEmail(email: String): Optional<User>

    fun existsByEmail(email: String): Boolean
}
