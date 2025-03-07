package project.service

import org.springframework.stereotype.Service
import project.model.Message
import project.repository.MessageRepository
import project.repository.UserRepository

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) {

    fun sendMessage(userId: Long, sender: String, text: String): Message {
        val user = userRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val message = Message(user = user, sender = sender, text = text)
        return messageRepository.save(message)
    }

    fun getMessagesByUser(userId: Long): List<MessageResponse> {
        return messageRepository.findByUserId(userId).map { message ->
            MessageResponse(sender = message.sender, text = message.text)
        }
    }

    data class MessageResponse(
        val sender: String,
        val text: String
    )
}