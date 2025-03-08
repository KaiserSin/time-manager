package project.service

import org.springframework.stereotype.Service
import project.model.Message
import project.repository.MessageRepository

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val executorService: ExecutorService
) {

    fun sendMessage(userId: Long, sender: String, text: String): Message {
        val user = executorService.findById(userId).orElseThrow { RuntimeException("User not found") }
        val message = Message(user = user, sender = sender, text = text)
        return messageRepository.save(message)
    }

    fun getMessagesForChatGPT(userId: Long): String {
        val messages = messageRepository.findByUserId(userId)
        return messages.joinToString("\n") { "**${it.sender}:** ${it.text}" }
    }

    data class MessageResponse(
        val sender: String,
        val text: String
    )
}
