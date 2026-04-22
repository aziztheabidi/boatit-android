package com.boatit.boatsharing.features.chat.domain.usecase

import com.abanapps.socailqrscanner.data_layer.model.ChatMessage
import com.boatit.boatsharing.features.chat.model.ActiveVoyagersResponse
import com.boatit.boatsharing.features.chat.model.ComplainRequest
import com.boatit.boatsharing.features.chat.model.FollowRequest
import com.boatit.boatsharing.features.chat.model.FollowResponse

class FetchVoyagersUseCase(
    private val fetchVoyagers: suspend () -> Result<ActiveVoyagersResponse>,
) {
    suspend operator fun invoke(): Result<ActiveVoyagersResponse> {
        return fetchVoyagers()
    }
}

class FollowVoyagerUseCase(
    private val followVoyager: suspend (FollowRequest) -> Result<FollowResponse>,
) {
    suspend operator fun invoke(request: FollowRequest): Result<FollowResponse> {
        return followVoyager(request)
    }
}

class ComplainVoyagerUseCase(
    private val complainVoyager: suspend (ComplainRequest) -> Result<FollowResponse>,
) {
    suspend operator fun invoke(request: ComplainRequest): Result<FollowResponse> {
        return complainVoyager(request)
    }
}

class ListenForMessagesUseCase(
    private val listenForMessages: (String, String, (List<ChatMessage>) -> Unit) -> Unit,
) {
    operator fun invoke(
        chatId: String,
        currentUserId: String,
        onMessagesUpdated: (List<ChatMessage>) -> Unit,
    ) {
        listenForMessages(chatId, currentUserId, onMessagesUpdated)
    }
}

class SendChatMessageUseCase(
    private val sendMessage: (String, String, String) -> Unit,
) {
    operator fun invoke(
        chatId: String,
        senderId: String,
        message: String,
    ) {
        sendMessage(chatId, senderId, message)
    }
}

class MarkMessagesAsReadUseCase(
    private val markMessagesAsRead: (String, String) -> Unit,
) {
    operator fun invoke(
        chatId: String,
        currentUserId: String,
    ) {
        markMessagesAsRead(chatId, currentUserId)
    }
}
