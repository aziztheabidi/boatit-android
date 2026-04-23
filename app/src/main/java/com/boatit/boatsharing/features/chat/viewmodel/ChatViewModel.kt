package com.boatit.boatsharing.features.chat.viewmodel

import com.abanapps.socailqrscanner.data_layer.model.ChatMessage
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.chat.domain.usecase.ListenForMessagesUseCase
import com.boatit.boatsharing.features.chat.domain.usecase.MarkMessagesAsReadUseCase
import com.boatit.boatsharing.features.chat.domain.usecase.SendChatMessageUseCase

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
) : UiState

sealed interface ChatUiEvent : UiEvent {
    data class Listen(
        val chatId: String,
        val currentUserId: String,
    ) : ChatUiEvent

    data class SendMessage(
        val chatId: String,
        val senderId: String,
        val message: String,
    ) : ChatUiEvent

    data class MarkRead(
        val chatId: String,
        val currentUserId: String,
    ) : ChatUiEvent

    data class MessagesUpdated(val messages: List<ChatMessage>) : ChatUiEvent
}

sealed interface ChatUiEffect : UiEffect {
    data object NoOpEffect : ChatUiEffect
}

class ChatViewModel(
    private val listenForMessagesUseCase: ListenForMessagesUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val markMessagesAsReadUseCase: MarkMessagesAsReadUseCase,
) : BaseViewModel<ChatUiState, ChatUiEvent, ChatUiEffect>(ChatUiState()) {
    override fun onEvent(event: ChatUiEvent) {
        when (event) {
            is ChatUiEvent.Listen -> listenForMessages(event.chatId, event.currentUserId)
            is ChatUiEvent.SendMessage ->
                sendChatMessageUseCase(event.chatId, event.senderId, event.message)
            is ChatUiEvent.MarkRead -> markMessagesAsReadUseCase(event.chatId, event.currentUserId)
            is ChatUiEvent.MessagesUpdated -> updateState { copy(messages = event.messages) }
        }
    }

    fun listenForMessages(
        chatId: String,
        currentUserId: String,
    ) {
        listenForMessagesUseCase(chatId, currentUserId) { newMessages ->
            onEvent(ChatUiEvent.MessagesUpdated(newMessages))
        }
    }

    fun sendMessage(
        chatId: String,
        senderId: String,
        message: String,
    ) {
        onEvent(ChatUiEvent.SendMessage(chatId, senderId, message))
    }

    fun markMessagesAsRead(
        chatId: String,
        currentUserId: String,
    ) {
        onEvent(ChatUiEvent.MarkRead(chatId, currentUserId))
    }
}
