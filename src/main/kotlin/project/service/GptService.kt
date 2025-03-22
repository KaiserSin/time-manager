package project.service

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

@Service
class GptService(
    private val taskService: TaskService,
    private val settingService: SettingService,
    private val objectMapper: ObjectMapper
) {

    @Value("\${openai.api.key}")
    private lateinit var apiKey: String

    private val log = LoggerFactory.getLogger(GptService::class.java)
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
        val futureTasksText = taskService.getTasksForChatGPT(executorId, now)
            .joinToString("\n")
        val settingsText = settingService.getSettingsForChatGPT(executorId)
            .joinToString("\n")

        val prompt = buildPrompt(
            taskName, taskDescription, taskDuration,
            futureTasksText, settingsText, gptDescription, now
        )

        val responseText = sendChatGPTRequest(prompt)
        return parseDateTime(responseText)
    }

    private fun buildPrompt(
        taskName: String,
        taskDescription: String,
        taskDuration: Duration,
        futureTasks: String,
        settings: String,
        gptDescription: String,
        now: LocalDateTime
    ): String {
        return """
            Scheduled tasks (after $now):
            $futureTasks
        
            User's preferences:
            $settings
            
            New Task:
            - Name: $taskName
            - Description: $taskDescription
            - Additional context: $gptDescription
            - Duration: $taskDuration
            
            Please provide the optimal start time (ISO 8601, e.g. 2024-03-10T09:00:00) after $now.
            Reply with only the datetime, no extra text.
        """.trimIndent()
    }

    private fun sendChatGPTRequest(prompt: String): String {
        log.info("Sending request to ChatGPT...")

        val requestBody = objectMapper.writeValueAsString(
            mapOf(
                "model" to "gpt-3.5-turbo",
                "messages" to listOf(
                    mapOf(
                        "role" to "system",
                        "content" to "You are a scheduling assistant. Always respond with only a valid ISO 8601 datetime, without any additional text."
                    ),
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

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val body = response.body?.string()
                    log.error("ChatGPT request failed: $body")
                    throw RuntimeException("Error in ChatGPT request: $body")
                }
                val chatResponse = objectMapper.readTree(response.body?.string())
                val content = chatResponse["choices"]?.get(0)?.get("message")?.get("content")?.asText()
                if (content == null) {
                    log.error("ChatGPT response is missing expected fields")
                    throw RuntimeException("ChatGPT did not return a valid message content")
                }
                log.debug("Response from ChatGPT: $content")
                return content
            }
        } catch (e: Exception) {
            log.error("Failed to call ChatGPT API", e)
            throw RuntimeException("ChatGPT API call error", e)
        }
    }

    private fun parseDateTime(responseText: String): LocalDateTime {
        return try {
            LocalDateTime.parse(responseText)
        } catch (e: DateTimeParseException) {
            log.error("ChatGPT returned an invalid date format: $responseText", e)
            throw RuntimeException("ChatGPT returned an invalid date format: $responseText", e)
        }
    }
}
