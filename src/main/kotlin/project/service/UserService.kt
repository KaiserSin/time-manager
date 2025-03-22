package project.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import project.exception.EntityNotFoundException
import project.model.user.User
import project.model.user.dto.UserRequest
import project.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun createUser(userRequest: UserRequest): User {
        val user = User(name = userRequest.name)
        return userRepository.save(user)
    }

    fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun getUserByIdOrThrow(id: Long): User {
        return getUserById(id)
            ?: throw EntityNotFoundException("User with id $id not found")
    }

    @Transactional
    fun deleteUser(id: Long) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
        } else {
            throw EntityNotFoundException("User with id $id not found")
        }
    }

    fun getAllUsers(pageable: Pageable): Page<User> {
        return userRepository.findAll(pageable)
    }

    @Transactional
    fun updateUser(id: Long, userRequest: UserRequest): User {
        val existingExecutor = getUserByIdOrThrow(id)
        val updatedExecutor = existingExecutor.copy(
            name = userRequest.name
        )
        return userRepository.save(updatedExecutor)
    }
}
