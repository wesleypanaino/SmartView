package com.example.smartview.utils

import com.example.smartview.data.services.Message

class OpenAiUtils {
    companion object {
        private val SYSTEM_MESSAGE = Message(
            role = "system",
            content =
            """"TEMPLATE"""
        )

        fun getSystemMessage(): Message {
            return SYSTEM_MESSAGE
        }

        fun getUserMessage(content: String): Message {
            return Message(
                role = "user",
                content = content
            )
        }

        fun getMaxTokens():Int{
            return 800
        }
    }
}