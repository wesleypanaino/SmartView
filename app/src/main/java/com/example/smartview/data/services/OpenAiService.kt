package com.example.smartview.data.services

import com.example.smartview.BuildConfig
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class Usage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)

data class Message(
    val role: String,
    val content: String
)

data class ChatCompletion(
    val model: String,
    val messages: List<Message>,
    val max_tokens: Int
    /*
    todo add these later
    temperature: Float, This parameter helps control the randomness of the model's output. A higher value (closer to 1) makes the output more random, while a lower value (closer to 0) makes it more deterministic.
    top_p: Float,This parameter is used for nucleus sampling, a method that chooses the next word from a subset of the vocabulary, where the subset is determined by top_p. The subset includes the smallest set of words whose cumulative probability exceeds the value of top_p.
    frequency_penalty:Float,This can be used to penalize more frequent tokens. The penalty should be between 0 and 1.
    presence_penalty :Float This can be used to penalize new tokens. The penalty should be between 0 and 1.
     */
)

data class CompletionResponse(
    val choices: List<Choice>,
    val created: Long,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: Usage,
)

data class Choice(
    val message: Message,
    val finish_reason: String,
    val index: Int
)

//todo move these request to an intermediate server
interface OpenAiService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer ${BuildConfig.OPENAI_API_KEY}"
    )
    //@POST("/v1/engines/davinci-codex/completions")
    @POST("/v1/chat/completions")
    suspend fun createChatCompletion(@Body chatCompletion: ChatCompletion): CompletionResponse
}