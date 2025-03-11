package com.boatit.boatsharing.ui.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abanapps.socailqrscanner.data_layer.model.ChatMessage
import com.boatit.boatsharing.ui.chat.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun listenForMessages(chatId: String, currentUserId: String) {
        repository.listenForMessages(chatId, currentUserId) { newMessages ->
            _messages.value = newMessages
        }
    }

    fun sendMessage(chatId: String, senderId: String, message: String) {
        repository.sendMessage(chatId, senderId, message)
    }

    fun markMessagesAsRead(chatId: String, currentUserId: String) {
        repository.markMessagesAsRead(chatId, currentUserId)
    }
}

