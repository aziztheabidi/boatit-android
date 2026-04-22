package com.boatit.boatsharing.features.chat.viewmodel

import androidx.lifecycle.ViewModel
import com.abanapps.socailqrscanner.data_layer.model.ChatMessage
import com.boatit.boatsharing.features.chat.domain.usecase.ListenForMessagesUseCase
import com.boatit.boatsharing.features.chat.domain.usecase.MarkMessagesAsReadUseCase
import com.boatit.boatsharing.features.chat.domain.usecase.SendChatMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatViewModel(
    private val listenForMessagesUseCase: ListenForMessagesUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val markMessagesAsReadUseCase: MarkMessagesAsReadUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun listenForMessages(
        chatId: String,
        currentUserId: String,
    ) {
        listenForMessagesUseCase(chatId, currentUserId) { newMessages ->
            _messages.value = newMessages
        }
    }

    fun sendMessage(
        chatId: String,
        senderId: String,
        message: String,
    ) {
        sendChatMessageUseCase(chatId, senderId, message)
    }

    fun markMessagesAsRead(
        chatId: String,
        currentUserId: String,
    ) {
        markMessagesAsReadUseCase(chatId, currentUserId)
    }
}
