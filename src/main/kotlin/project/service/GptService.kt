package project.service

import org.springframework.stereotype.Service
import project.model.task.Task
import java.time.Duration
import java.time.LocalDateTime
import org.springframework.beans.factory.annotation.Value
import okhttp3.*
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@Service
class GptService(
    private val taskService: TaskService,
    private val messageService: MessageService,
    private val objectMapper: ObjectMapper
) {

    @Value("\${openai.api.key}")
    private lateinit var apiKey: String

    private val client = OkHttpClient()
    private val url = "https://api.openai.com/v1/chat/completions"

    fun getOptimalStartTime(
        taskName: String,
        taskDescription: String,
        taskDuration: Duration,
        gptDescription: String,
        executorId: Long
    ): LocalDateTime {
        val now = LocalDateTime.now()
        val futureTasksText = taskService.getTasksForChatGPT(executorId, now).joinToString("\n")
        val pastMessagesText = messageService.getMessagesForChatGPT(executorId)
        val prompt = buildPrompt(taskName, taskDescription, taskDuration, futureTasksText, pastMessagesText, gptDescription)
        return LocalDateTime.parse(sendChatGPTRequest(prompt))
    }

    private fun buildPrompt(
        taskName: String,
        taskDescription: String,
        taskDuration: Duration,
        futureTasks: String,
        pastMessages: String,
        gptDescription: String
    ): String {
        return """
        The user already has the following scheduled tasks:
        $futureTasks

        Here are the user's previous messages:
        $pastMessages

        New task:
        **Task:** $taskName
        **Description:** $taskDescription
        **GPT Additional Context:** $gptDescription
        **Duration:** $taskDuration

        Based on the schedule and past discussions, suggest the optimal start time for this task.
        Respond only in ISO 8601 format (e.g., 2024-03-10T09:00:00).
        """.trimIndent()
    }

    private fun sendChatGPTRequest(prompt: String): String {
        val requestBody = objectMapper.writeValueAsString(
            mapOf(
                "model" to "gpt-4",
                "messages" to listOf(
                    mapOf("role" to "system", "content" to "You are a scheduling assistant."),
                    mapOf("role" to "user", "content" to prompt)
                ),
                "temperature" to 0.7
            )
        ).toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw RuntimeException("Error in ChatGPT request: ${response.body?.string()}")
            }
            val chatResponse = objectMapper.readTree(response.body?.string())
            return chatResponse["choices"]?.get(0)?.get("message")?.get("content")?.asText()
                ?: throw RuntimeException("ChatGPT did not return a response")
        }
    }
}
