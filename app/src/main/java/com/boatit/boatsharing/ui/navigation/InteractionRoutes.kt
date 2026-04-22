package com.boatit.boatsharing.ui.navigation

import android.net.Uri

object InteractionRoutes {
    const val CHAT_ID_ARG = "chatId"
    const val CURRENT_USER_ID_ARG = "currentUserId"
    const val NAME_ARG = "name"
    const val SENDER_ID_ARG = "senderId"
    const val FEEDBACK_VOYAGE_ID_ARG = "value"

    val chatPattern: String = "${NavigationManager.CHAT_SCREEN}/{$CHAT_ID_ARG}/{$CURRENT_USER_ID_ARG}/{$NAME_ARG}/{$SENDER_ID_ARG}"
    val voyagerFeedbackPattern: String = "${NavigationManager.VOYAGER_FEEDBACK_SCREEN}/{$FEEDBACK_VOYAGE_ID_ARG}"
    val captainFeedbackPattern: String = "${NavigationManager.CAPTAIN_FEEDBACK_SCREEN}/{$FEEDBACK_VOYAGE_ID_ARG}"

    fun chat(
        chatId: String,
        currentUserId: String,
        name: String,
        senderId: String,
    ): String {
        return "${NavigationManager.CHAT_SCREEN}/${Uri.encode(
            chatId,
        )}/${Uri.encode(currentUserId)}/${Uri.encode(name)}/${Uri.encode(senderId)}"
    }

    fun voyagerFeedback(voyageId: String?): String {
        return "${NavigationManager.VOYAGER_FEEDBACK_SCREEN}/${Uri.encode(voyageId ?: "null")}"
    }

    fun captainFeedback(voyageId: String?): String {
        return "${NavigationManager.CAPTAIN_FEEDBACK_SCREEN}/${Uri.encode(voyageId ?: "null")}"
    }
}
